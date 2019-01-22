package com.ling.blutoothlibrary.blehandle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import com.ling.blutoothlibrary.bleenum.BLEinter;
import com.ling.blutoothlibrary.bleslistener.BlutoothListener;
import com.ling.blutoothlibrary.bleslistener.TimeListener;
import com.ling.blutoothlibrary.bleutils.HLog;
import com.ling.blutoothlibrary.bleutils.ThreadUtil;
import com.ling.blutoothlibrary.timeTask.CountDownTimerHY;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 创建时间 ：2017/9/30.
 * <br/>作者 ：红叶岭谷
 * <br/>说明 ：该类用于蓝牙5.0+的链接操作集成
 */
public class BLEHandle {
    private static final String TAG = "com.ling.android5.0blelibs.blehandle.BLEHandle";
    /**
     * 蓝牙设备管理器
     */
    private BluetoothManager bluetoothManager;
    /**
     * 蓝牙扫描messageId
     */
    private static final int SCAN_TAG = 20171008;
    /**
     * 蓝牙链接messageId
     */
    protected static final int COUNT_TAG = 20171007;
    /**
     * 超时时间
     */
    private long OVERTIME = 0;
    /**
     * 蓝牙单包发送间隔时间
     */
    private long slp = 50;
    private BlutoothListener.ScanDevicesListener scanListener;
    /**
     * 指定的扫描AMC
     */
    private String AimsMAC = null;
    private List<BluetoothDevice> mListScanDe;
    private BluetoothLeScanner leScanner;
    private Context devContext;
    private static BLEMessage bleMessage;
    private static BLEHandle instance;

    private BLEHandle() {
    }

    /**
     * 获取蓝牙操作的对象
     *
     * @return
     */
    public static synchronized BLEHandle getinstance() {
        if (instance == null) {
            instance = new BLEHandle();
        }
        if (bleMessage == null) {
            bleMessage = BLEMessage.getInstance();
        }
        return instance;
    }

    /**
     * 查看蓝牙是否开启
     *
     * @param mContext
     * @param autoOpen 是否自动打开蓝牙
     * @return
     */
    public boolean bleEnabled(Context mContext, boolean autoOpen) {
        try {
            if (mContext != null) {
                BluetoothAdapter mBluetoothAdapter = getBluetoothAdapter(mContext);
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    if (autoOpen) {
                        return mBluetoothAdapter.enable();
                    }
                    return false;
                }
                return true;
            } else {
                HLog.e(TAG, "Context=" + mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取蓝牙管理器
     *
     * @return
     */
    private BluetoothAdapter getBluetoothAdapter(Context mContext) {
        try {
            if (mContext != null) {
                if (bluetoothManager == null) {
                    bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                }
                return bluetoothManager.getAdapter();
            } else {
                HLog.e(TAG, "Context=" + mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断APP是否支持低功耗蓝牙
     *
     * @param mContext
     * @return
     */
    public boolean supported(Context mContext) {

        try {
            // Use this check to determine whether BLE is supported on the device.
            // Then
            // you can selectively disable BLE-related features.
            if (mContext != null) {
                return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
            } else {
                HLog.e(TAG, "Context=" + mContext);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 验证字符串蓝牙地址,如“00:43:A8:23:10:F0”
     * <br/>大写字母字符必须是有效的。
     *
     * @param address 蓝牙地址
     * @return 如果地址是有效的是真, 否则假
     */
    public boolean checkBluetoothAddress(String address) {
        try {
            if (address != null) {
                return BluetoothAdapter.checkBluetoothAddress(address);
            } else {
                HLog.e(TAG, "address=" + address);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取手机蓝牙地址
     * @param context
     * @return
     */
    public String getDeviceMac(Context context){
        return getBluetoothAdapter(context).getAddress();
    }

    /**
     * 返回的集合 BluetoothDevice对象,结合本地适配器(配对).
     *
     * @param mContext
     * @return
     */
    public Set<BluetoothDevice> bondedDevices(Context mContext) {
        try {
            if (mContext != null) {
                return getBluetoothAdapter(mContext).getBondedDevices();
            } else {
                HLog.e(TAG, "Context=" + mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置蓝牙扫描超时时间
     *
     * @param scanTime
     * @return
     */
    public BLEHandle setBLEOverTime(long scanTime) {
        this.OVERTIME = scanTime;
        return instance;
    }

    /**
     * 设置查询方式
     *
     * @param selectType true 标识通过指定的服务uuid,false标识通过遍历查询读写uuid
     * @return
     */
    public BLEHandle setSelectT(boolean selectType) {
        bleMessage.setSelectType(selectType);
        return instance;
    }

    /**
     * 设置蓝牙单包发送间隔
     *
     * @param itemDataTime 时间整形
     * @return
     */
    public BLEHandle setBLESendItemDataTime(long itemDataTime) {
        this.slp = itemDataTime;
        return instance;
    }

    /**
     * 设置蓝牙服务的UUID
     *
     * @param serviceUUID 使用的服务的UUID
     * @return
     */
    public BLEHandle setServiceUUID(UUID serviceUUID) {
        bleMessage.addServiceUUID(serviceUUID);
        return instance;
    }

    /**
     * 设置蓝牙设备的读写UUID
     *
     * @param notifUUID 设备通知客户端的UUID
     * @param writeUUID 客户端发送数据到设备的UUID
     * @return
     */
    public BLEHandle setDevicesWriteUUID(UUID notifUUID, UUID writeUUID) {
        bleMessage.addSendAndResponseUUID(notifUUID, writeUUID);
        return instance;
    }

    /**
     * 扫描周围低功耗蓝牙设备
     *
     * @param mContext
     * @param blutoothCall 扫描的监听接口
     * @param mac          指定的扫描对象
     *                     <br/>备注 ：不可以混淆
     */
    public void scanBlutoothDevices(final Context mContext, String mac, BlutoothListener.ScanDevicesListener blutoothCall) {
        try {
            this.scanListener = blutoothCall;
            this.AimsMAC = mac;
            this.devContext = mContext;
            disConnect();
            leScanner = getBluetoothAdapter(mContext).getBluetoothLeScanner();
            if (leScanner != null) {
                HLog.e(TAG, "Thread=ing");
                Runnable runnableScanDev = new Runnable() {
                    @Override
                    public void run() {
                        leScanner.startScan(scanCallback);
                    }
                };
                ThreadUtil.getInstance().addThreadPool(runnableScanDev);
                if (OVERTIME > 0) {
                    CountDownTimerHY.startCount(SCAN_TAG, OVERTIME, new TimeListener.CountDownListener() {
                        @Override
                        public void countTimeEnd(int tag) {
                            if (SCAN_TAG == tag) {
                                disConnect();
                                stopScanBlutoothDevices();
                                scanListener.overTime();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙停止扫描
     */
    public void stopScanBlutoothDevices() {
        if (leScanner != null) {
            leScanner.stopScan(scanCallback);
        }
    }


    /**
     * 蓝牙扫描回调接口
     */
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (scanListener != null) {
                if (AimsMAC != null) {
                    if (AimsMAC.equals(result.getDevice().getAddress())) {
                        CountDownTimerHY.stopCount(SCAN_TAG);
                        scanListener.onScanResult(callbackType, result);
                        stopScanBlutoothDevices();
                    }
                } else {
                    CountDownTimerHY.stopCount(SCAN_TAG);
                    scanListener.onScanResult(callbackType, result);
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            if (scanListener != null) {
                scanListener.onBatchScanResults(results);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            if (scanListener != null) {
                scanListener.onScanFailed(errorCode);
            }
        }
    };

    /**
     * 连接蓝牙设备(条件MAC)
     *
     * @param mContext       上下文关系
     * @param mac            设备的Mac
     * @param mCountCallBack 回调接口
     */
    public void countBlutooth(final Context mContext, String mac, final BlutoothListener.CountListener mCountCallBack) {
        try {
            if (mContext != null && mac != null && !mac.isEmpty()) {
                stopScanBlutoothDevices();
                BluetoothDevice device = getBluetoothAdapter(mContext).getRemoteDevice(mac);
                countDevices(mContext, device, mCountCallBack);
                if (OVERTIME > 0) {
                    CountDownTimerHY.startCount(COUNT_TAG, OVERTIME, new TimeListener.CountDownListener() {
                        @Override
                        public void countTimeEnd(int tag) {
                            if (COUNT_TAG == tag) {
                                mCountCallBack.onConnectedResult(BLEinter.CountList.COUNT_TIME_OUT);
                            }
                        }
                    });
                }
            } else {
                HLog.e(TAG, "参数为空 countBlutooth(Context,String,BlutoothListener.CountListener)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接蓝牙(条件device对象)
     *
     * @param mContext
     * @param device         蓝牙设备
     * @param mCountCallBack
     */
    public void countBlutooth(final Context mContext, BluetoothDevice device, final BlutoothListener.CountListener mCountCallBack) {
        try {
            if (mContext != null && device != null) {
                stopScanBlutoothDevices();
                countDevices(mContext, device, mCountCallBack);
                if (OVERTIME > 0) {
                    CountDownTimerHY.startCount(COUNT_TAG, OVERTIME, new TimeListener.CountDownListener() {
                        @Override
                        public void countTimeEnd(int tag) {
                            if (COUNT_TAG == tag) {
                                mCountCallBack.onConnectedResult(BLEinter.CountList.COUNT_TIME_OUT);
                            }
                        }
                    });
                }
            } else {
                HLog.e(TAG, "参数为空 countBlutooth(Context,String,BlutoothListener.CountListener)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙连接(私有化)
     *
     * @param mContext
     * @param device         蓝牙设备
     * @param mCountCallBack
     */
    private void countDevices(Context mContext, BluetoothDevice device, BlutoothListener.CountListener mCountCallBack) {
        bleMessage.countBlutooth(mContext, device, mCountCallBack);
    }

    /**
     * 蓝牙发送数据
     *
     * @param data       数据
     * @param mWriteData
     */
    public void writeCharacteristic(final byte[] data, final BlutoothListener.NotificeDataListener mWriteData) {
        Runnable runnableSendData = new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayInputStream streamData = null;
                    try {
                        if (data != null) {
                            streamData = new ByteArrayInputStream(data);
                            byte[] sendData = new byte[20];
                            if (data.length > 20) {
                                while (streamData.read(sendData) != -1) {
                                    bleMessage.writeCharacteristic(sendData, mWriteData);
                                    sendData = new byte[20];
                                    Thread.sleep(slp);
                                }
                            } else {
                                streamData.read(sendData);
                                bleMessage.writeCharacteristic(sendData, mWriteData);
                            }
                        } else {
                            HLog.e(TAG, "参数为空 writeCharacteristic(byte[],BlutoothListener.NotificeDataListener)");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (streamData != null) {
                                streamData.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadUtil.getInstance().addThreadPool(runnableSendData);
    }

    /**
     * 设置通知的UUID
     * @param serviceUUID 服务UUID
     * @param notifUUID 通知UUID
     * @param notificeData 收到的通知数据
     */
    public void notificationData(UUID serviceUUID,UUID notifUUID,final BlutoothListener.NotificeDataListener notificeData){
        bleMessage.setNotification(serviceUUID,notifUUID,notificeData);
    }
    /**
     * 断开蓝牙链接
     */
    public void disConnect() {
        bleMessage.disConnect();
    }
}

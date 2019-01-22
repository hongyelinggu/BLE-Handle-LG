package com.ling.blutoothlibrary.blehandle;

import android.bluetooth.*;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.ling.blutoothlibrary.bleenum.BLEinter;
import com.ling.blutoothlibrary.bleslistener.BlutoothListener;
import com.ling.blutoothlibrary.bleutils.HLog;
import com.ling.blutoothlibrary.bleutils.ThreadUtil;
import com.ling.blutoothlibrary.timeTask.CountDownTimerHY;

import java.util.List;
import java.util.UUID;

/**
 * 创建时间 ：2017/10/9.
 * 作者 ：红叶岭谷
 */
class BLEMessage {
    final String TAG = "com.ling.android50blelibs.blehandle.BLEMessage";
    private final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    private static BLEMessage instance;
    private BluetoothGatt connectGatt;
    private BlutoothListener.CountListener countListener;
    private BluetoothGattCharacteristic gattCharacteristic;
    private BlutoothListener.NotificeDataListener mThordadWriteData;
    /**
     * 是否搜索到读写的UUID
     */
    private boolean write = false, notif = false;
    /**
     * 设备使用的服务
     */
    private UUID MESSAGE_SERVICE_UUID = UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb");
    /**
     * 书写的UUID
     */
    private UUID WRITE_UUID = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");
    /**
     * 通知的UUID
     */
    private UUID READ_UUID = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    /**
     * 是否有服务uuid
     */
    private boolean hasSer = false;


    protected static synchronized BLEMessage getInstance() {
        if (instance == null) {
            instance = new BLEMessage();
        }
        return instance;
    }

    /**
     * 设置蓝牙设备服务UUID
     *
     * @param service
     * @return
     */
    protected BLEMessage addServiceUUID(UUID service) {
        MESSAGE_SERVICE_UUID = service;
        return instance;
    }

    /**
     * 含有服务的查找UUID
     * @param selectT
     */
    public void setSelectType(boolean selectT){
        hasSer = selectT;
    }
    /**
     * 添加蓝牙设备的读写UUID
     *
     * @return
     */
    protected BLEMessage addSendAndResponseUUID(UUID readUUID, UUID writeUUID) {
        READ_UUID = readUUID;
        WRITE_UUID = writeUUID;
        return instance;
    }

    /**
     * 蓝牙连接
     *
     * @param device
     */
    long statCountT = 0;

    protected void countBlutooth(final Context context, final BluetoothDevice device, BlutoothListener.CountListener mCountCallBack) {
        if (connectGatt == null && (System.currentTimeMillis() - statCountT) > 1000) {
            this.countListener = mCountCallBack;
            if (MESSAGE_SERVICE_UUID == null) {
                countListener.onConnectedResult(BLEinter.CountList.COUNT_FALL);
                HLog.e(TAG, "请你添加蓝牙设备的服务UUID,调用addServiceUUID()方法");
            } else {
                Runnable runnableCount = new Runnable() {
                    @Override
                    public void run() {
                        connectGatt = device.connectGatt(context, false, callback);
                    }
                };
                ThreadUtil.getInstance().addThreadPool(runnableCount);
            }
        } else {
            HLog.w(TAG, "请断开你的蓝牙链接,并且链接间距大于1s" + "statCountT:" + statCountT + "=connectGatt=" + connectGatt);
        }
    }

    /**
     * 蓝牙设备的回调方法
     */
    private BluetoothGattCallback callback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                HLog.w(TAG, "蓝牙被动链接中断");
                if (connectGatt != null && countListener != null) {
                    countListener.onConnectedResult(BLEinter.CountList.COUNT_DISCONNECTED);
                    disConnect();
                }

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // 客户端配置uuid,如果没有这个是不能够通知客户端接收数据的
            if (BluetoothGatt.GATT_SUCCESS == status) {
                if (hasSer) {
                    BluetoothGattService gattService = gatt.getService(MESSAGE_SERVICE_UUID);
                    selectWNuuid(gatt, CLIENT_CHARACTERISTIC_CONFIG, gattService);
                } else {
                    List<BluetoothGattService> services = gatt.getServices();
                    for (BluetoothGattService gattService : services) {
                        List<BluetoothGattCharacteristic> characteristics = gattService.getCharacteristics();
                        for (BluetoothGattCharacteristic characteristic:characteristics) {
                            UUID uuid = characteristic.getUuid();
                            if (WRITE_UUID.toString().equals(uuid.toString()) || READ_UUID.toString().equals(uuid.toString())){
                                selectWNuuid(gatt, CLIENT_CHARACTERISTIC_CONFIG, gattService);
                            }
                        }
                    }

                }
                if (write && notif) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CountDownTimerHY.stopCount(BLEHandle.COUNT_TAG);
                            countListener.onConnectedResult(BLEinter.CountList.COUNT_SUCCESS);
                        }
                    }, 100);
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (mThordadWriteData != null) {
                mThordadWriteData.notificationData(characteristic.getValue());
            }
        }
    };

    /**
     * 获取UUID
     * @param gatt
     * @param CLIENT_CHARACTERISTIC_CONFIG
     * @param gattService
     */
    private void selectWNuuid(BluetoothGatt gatt, String CLIENT_CHARACTERISTIC_CONFIG, BluetoothGattService gattService) {
        if (WRITE_UUID != null) {
            gattCharacteristic = gattService.getCharacteristic(WRITE_UUID);
            write = true;
        }
        if (READ_UUID != null) {
            BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(READ_UUID);
            gatt.setCharacteristicNotification(characteristic, true);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
            notif = true;
        }
    }

    /**
     * 设置蓝牙接收通知数据
     * @param notifUUID
     * @param mWriteData
     */
    protected void setNotification(UUID serviceUUID,UUID notifUUID,BlutoothListener.NotificeDataListener mWriteData){
        BluetoothGattService serviceGatt = null;
        if (serviceUUID!=null){
            serviceGatt = connectGatt.getService(serviceUUID);
        }
        if (notifUUID!=null){
            BluetoothGattCharacteristic characteristic = serviceGatt.getCharacteristic(notifUUID);
            connectGatt.setCharacteristicNotification(characteristic, true);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                connectGatt.writeDescriptor(descriptor);
            }
        }
        if (mWriteData!=null){
            mThordadWriteData = mWriteData;
        }
    }

    /**
     * 填写数据发送 到蓝牙设备
     */
    protected void writeCharacteristic(byte[] values, BlutoothListener.NotificeDataListener mWriteData) {
        this.mThordadWriteData = mWriteData;
        if (gattCharacteristic == null || connectGatt == null) {
            HLog.w(TAG, "gattCharacteristic=" + gattCharacteristic);
            HLog.w(TAG, "connectGatt=" + connectGatt);
        } else {
            gattCharacteristic.setValue(values);
            connectGatt.writeCharacteristic(gattCharacteristic);
        }
    }

    /**
     * 断开连接
     */
    protected boolean disConnect() {
        if (connectGatt != null) {
            HLog.i(TAG, "蓝牙连接主动断开发送指令");
            connectGatt.disconnect();
            connectGatt.close();
            connectGatt = null;
            mThordadWriteData = null;
            statCountT = 0;

            countListener = null;
            gattCharacteristic = null;
            /**
             * 是否搜索到读写的UUID
             */
            write = false;
            notif = false;
        }
        return true;
    }
}

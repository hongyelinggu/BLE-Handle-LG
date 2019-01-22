package com.ling.blutoothlibrary.bleslistener;

import android.bluetooth.le.ScanResult;
import com.ling.blutoothlibrary.bleenum.BLEinter;

import java.util.List;

/**
 * <br/>创建时间 ：2017/9/30.
 * <br/>作者 ：红叶岭谷
 * <br/>备注 ：不可以混淆
 */
public class BlutoothListener {

    /**
     * =====================================
     * 此接口是用于蓝牙设备的扫描
     * =====================================
     */
    public interface ScanDevicesListener {
        /**
         * 扫描返回结果集
         *
         * @param results
         */
        void onBatchScanResults(List<ScanResult> results);

        /**
         * 扫描失败
         *
         * @param errorCode
         */
        void onScanFailed(int errorCode);

        /**
         * 蓝牙扫描的单个对象
         *
         * @param callbackType
         * @param result
         */
        void onScanResult(int callbackType, ScanResult result);

        /**
         * 扫描超时时间
         */
        void overTime();
    }

    /**
     * =====================================
     * 此接口是蓝牙发送数据
     * =====================================
     */
    public interface NotificeDataListener {
        /**
         * 通知数据
         */
        void notificationData(byte[] data);
    }

    /**
     * =====================================
     * 此接口为蓝牙设备连接
     * =====================================
     */
    public interface CountListener {

        /**
         * 蓝牙连接结果
         */
        void onConnectedResult(BLEinter.CountList countResult);

    }

}

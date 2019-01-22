package com.ling.blutoothlibrary.dialogshow;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

/**
 * <br/>创建时间 ：2017/11/30.
 * <br/>作者 ：红叶岭谷
 * <br/>说明 ：该类是蓝牙扫描时显示用的对象
 * <br/>备注 ：不可以混淆
 */
public class BluetoothDeviceListBean {
    private BluetoothDevice device;
    private ScanResult scanResult;

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BluetoothDeviceListBean that = (BluetoothDeviceListBean) o;

        return device != null ? device.equals(that.device) : that.device == null;
    }

    @Override
    public int hashCode() {
        return device != null ? device.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BluetoothDeviceListBean{" +
                "device=" + device +
                ", scanResult=" + scanResult +
                '}';
    }
}

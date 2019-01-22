package com.ling.blutoothlibrary.bleslistener;

import android.bluetooth.le.ScanResult;
import com.ling.blutoothlibrary.bleenum.DialogEnum;
import com.ling.blutoothlibrary.dialogshow.BluetoothDeviceListBean;

/**
 * 创建时间 ：2017/11/30.
 * <br/>作者 ：红叶岭谷
 * <br/>备注 ：不可以混淆
 */
public class AlertListener {
    /**
     * 确认取消对话框事件
     */
    public interface AlertDialogListener {
        /**
         * 点击事件
         * @param dialogEnum
         */
        void buttonOnClick(DialogEnum dialogEnum);
    }

    /**
     * list对话框事件
     */
    public interface AlertListDialogListener{
        /**
         * 取消事件
         */
        void negativeButton();

        /**
         * 点击事件
         * @param bean
         */
        void itemDAO(ScanResult bean);
    }
}

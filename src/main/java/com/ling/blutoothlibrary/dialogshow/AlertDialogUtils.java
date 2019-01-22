package com.ling.blutoothlibrary.dialogshow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ling.blutoothlibrary.R;
import com.ling.blutoothlibrary.bleenum.DialogEnum;
import com.ling.blutoothlibrary.blehandle.BLEHandle;
import com.ling.blutoothlibrary.bleslistener.AlertListener;

/**
 * <br/>创建时间 ：2017/11/30.
 * <br/>作者 ：红叶岭谷
 * <br/>说明 ：对话框所有方法都必须在UI线程调用使用
 * <br/>备注 ：不可以混淆
 */
public class AlertDialogUtils {
    private Context dialogContext;
    private static AlertDialogUtils dialogUtils;
    /**
     * 不确定进度条
     */
    private ProgressDialog dialog;
    /**
     * list对话框
     */
    private AlertDialog listDialog;
    private DialogListAdapter listAdapter;
    /**
     * 操作对象
     */
    private static BLEHandle bleHandle;
    private AlertDialogUtils() { }

    /**
     * 显示枚举
     */
    public enum AlertDialogTypes {
        /**
         * 确定和取消按钮都有
         */
        ALLOW_TO_CANCEL("确定和取消按钮都有", 1),
        /**
         * 只有一个确定按钮
         */
        SYSTEM_IS_CANCELED("只有一个确定按钮", 2);
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private AlertDialogTypes(String name, int index) {
            this.name = name;
            this.index = index;
        }

        //覆盖方法
        @Override
        public String toString() {
            return this.index + "_" + this.name;
        }
    }
    public static synchronized AlertDialogUtils getInstance() {
        if (dialogUtils == null) {
            dialogUtils = new AlertDialogUtils();
        }
        if (bleHandle==null){
            bleHandle = BLEHandle.getinstance();
        }
        return dialogUtils;
    }

    /**
     * 显示对话框
     * @param context
     * @param disappear 是否点击返回键消失
     * @param message 提示信息
     * @param types 消失枚举
     * @param positiveTxt 确定按钮显示文本
     * @param negativeTxt 消失按钮显示文本
     * @param listener 监听回调方法
     */
    public void showDialog(final Context context,boolean disappear, final int message, final AlertDialogTypes types, final String positiveTxt, final String negativeTxt, final AlertListener.AlertDialogListener listener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setCancelable(disappear);
            builder.setPositiveButton(positiveTxt == null ? "确定" : positiveTxt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (listener != null) {
                        listener.buttonOnClick(DialogEnum.DIALOG_POSITIVE);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            });
            if (types == AlertDialogTypes.ALLOW_TO_CANCEL) {
                builder.setNegativeButton(negativeTxt == null ? "取消" : negativeTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.buttonOnClick(DialogEnum.DIALOG_NEGATIVE);
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                });
            }
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 显示对话框
     *
     * @param mContext
     * @param showTxt  显示的文字
     */
    public void showProgressDialog(Context mContext, int showTxt) {
        try {
            if (dialog != null) {
                disDialog();
            }
            dialog = ProgressDialog.show(mContext, null, mContext.getResources().getString(showTxt), true);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带有进度的对话框
     *
     * @param mContext
     * @param max      最大值
     * @param progress 当前值
     */
    public void showProgressBar(Context mContext, int max, int progress) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(mContext);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMessage(mContext.getResources().getString(R.string.STRING_LOADING));
                dialog.setMax(max);
                dialog.setProgress(progress);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                dialog.setProgress(progress);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 蓝牙扫描显示列表对话框
     * @param mContext
     * @param deviceBean 蓝牙对象
     * @param listener
     */
    public void showListDialog(Context mContext, BluetoothDeviceListBean deviceBean, final AlertListener.AlertListDialogListener listener) {
        try {
            String bleName = deviceBean.getDevice().getName();
            if (bleName==null || bleName.isEmpty() && bleName.length()<17) {
                return;
            }
            if (listDialog == null) {
                View layoutView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog, null);
                listAdapter = new DialogListAdapter(mContext);
                AlertDialog.Builder listDialogBuilder = new AlertDialog.Builder(mContext);
                listDialogBuilder.setNegativeButton(R.string.STRING_CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bleHandle.stopScanBlutoothDevices();
                        if (listener!=null) {
                            listener.negativeButton();
                        }
                        dismessListDialog();
                    }
                });
                listDialogBuilder.setView(layoutView);
                ListView listView = (ListView) layoutView.findViewById(R.id.lv_dialog);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ScanResult itemDevice = (ScanResult) adapterView.getItemAtPosition(i);
                        if (listener!=null) {
                            bleHandle.stopScanBlutoothDevices();
                        }
                        listener.itemDAO(itemDevice);
                        dismessListDialog();
                    }
                });
                listDialogBuilder.setCancelable(false);
                listDialog = listDialogBuilder.show();
                listAdapter.addBlutoothDevice(deviceBean);
            }else{
                listAdapter.addBlutoothDevice(deviceBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 注销list对话框
     */
    private void dismessListDialog() {
        try {
            if (listDialog != null) {
                listDialog.dismiss();
                listAdapter = null;
                listDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁对话框
     */
    public void disDialog() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

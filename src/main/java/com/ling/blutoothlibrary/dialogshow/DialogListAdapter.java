package com.ling.blutoothlibrary.dialogshow;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ling.blutoothlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 ：2017/11/30.
 * <br/>作者 ：红叶岭谷
 */
class DialogListAdapter extends BaseAdapter {
    private List<BluetoothDeviceListBean> adapterDeviceListBeen = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public DialogListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }
    /**
     * 添加数据
     *
     * @param deviceBean
     */
    protected void addBlutoothDevice(BluetoothDeviceListBean deviceBean) {
        if (!adapterDeviceListBeen.contains(deviceBean)) {
            adapterDeviceListBeen.add(deviceBean);
        }else {
            for (BluetoothDeviceListBean device : adapterDeviceListBeen) {
                if (device.hashCode() == deviceBean.hashCode()) {
                    device.setScanResult(deviceBean.getScanResult());
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
    /**
     * 清空数据
     */
    protected void clear(){
        adapterDeviceListBeen.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return adapterDeviceListBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return adapterDeviceListBeen.get(i).getScanResult();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.device_adapter_item, null);
        }
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            holder.deviceName = (TextView) convertView.findViewById(R.id.item_device_name);
            holder.deviceMac = (TextView) convertView.findViewById(R.id.item_device_mac);
            holder.deviceRssi = (TextView) convertView.findViewById(R.id.item_device_rssi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothDeviceListBean itemDeviceBean = adapterDeviceListBeen.get(i);
        BluetoothDevice itemDevice = itemDeviceBean.getDevice();
        holder.deviceName.setText(itemDevice.getName());
        holder.deviceMac.setText(itemDevice.getAddress());
        holder.deviceRssi.setText(String.valueOf(itemDeviceBean.getScanResult().getRssi()));
        return convertView;
    }
    private class ViewHolder {
        private TextView deviceName;
        private TextView deviceMac;
        private TextView deviceRssi;
    }
}

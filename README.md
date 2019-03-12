# LG Android5.0+蓝牙开发封装API 

标签： Android5.0+ 低功耗蓝牙开发 封装

---

### 1. 简述

该包封装了低功耗蓝牙开发的所有API,在android中使得开发者有更多的时间去考虑自己的逻辑而不再通讯蓝牙开发中



### 2. 如何快速使用

导入蓝牙开发包 ![此处输入图片的描述][1]

示例：

```
//Gradle
--------------------------------------------------
compile 'com.chaoqianhong.androidBLE:blutoothlibrary:1.1.1'
============================

Maven
--------------------------------------------------
<dependency>
  <groupId>com.chaoqianhong.androidBLE</groupId>
  <artifactId>blutoothlibrary</artifactId>
  <version>1.1.1</version>
  <type>pom</type>
</dependency>
```



### 3. 修改 AndroidManifest.xml 文件

```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
	<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
	<uses-permission android:name="android.permission.BLUETOOTH"/>
	
	<uses-feature android:name="android.hardware.bluetooth.le"
				  android:required="true"/>
```

### 4. 初始化蓝牙封装对象

```
BLEHandle getinstance = BLEHandle.getinstance();
```

### 5.方法概要

| 限定符和类型 |  方法和说明 |
| --------   | -----  |
|boolean bleEnabled(Context mContext, boolean autoOpen) |查看蓝牙是否开启 |
|java.util.Set<BluetoothDevice> bondedDevices(Context mContext) |返回的集合 BluetoothDevice对象,结合本地适配器(配对) |
|boolean checkBluetoothAddress(java.lang.String address) |验证字符串蓝牙地址,如“00:43:A8:23:10:F0”,大写字母字符必须是有效的。|
|void countBlutooth(Context mContext, BluetoothDevice device, BlutoothListener.CountListener mCountCallBack) |连接蓝牙(条件device对象) |
| void countBlutooth(Context mContext, java.lang.String mac, BlutoothListener.CountListener mCountCallBack) |连接蓝牙设备(条件MAC)
|void disConnect() |断开蓝牙链接 |
|java.lang.String getDeviceMac(Context context) |获取手机蓝牙地址| 
|static BLEHandle getinstance() |获取蓝牙操作的对象| 
|void notificationData(java.util.UUID serviceUUID, java.util.UUID notifUUID, BlutoothListener.NotificeDataListener notificeData)|设置通知的UUID |
|void scanBlutoothDevices(Context mContext, java.lang.String mac, BlutoothListener.ScanDevicesListener blutoothCall) |扫描周围低功耗蓝牙设备| 
|BLEHandle setBLEOverTime(long scanTime) |设置蓝牙扫描超时时间 |
|BLEHandle setBLESendItemDataTime(long itemDataTime) |设置蓝牙单包发送间隔 |
|BLEHandle setDevicesWriteUUID(java.util.UUID notifUUID, java.util.UUID writeUUID) |设置蓝牙设备的读写UUID | 
|BLEHandle setSelectT(boolean selectType) |设置查询方式 |
|BLEHandle setServiceUUID(java.util.UUID serviceUUID) |设置蓝牙服务的UUID| 
|void stopScanBlutoothDevices() |蓝牙停止扫描 |
|boolean supported(Context mContext)|判断APP是否支持低功耗蓝牙 |
|void writeCharacteristic(byte[] data, BlutoothListener.NotificeDataListener mWriteData)|蓝牙发送数据 |

更多信息请下载文档查看


  [1]: https://img.shields.io/badge/%20Gradle-V%201.1.1-brightgreen.svg

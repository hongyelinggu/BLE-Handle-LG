package com.ling.blutoothlibrary.bleenum;

/**
 * 创建时间 ：2017/12/6.
 * <br/>作者 ：红叶岭谷
 * <br/>说明 ：这是一个蓝牙枚举类
 * <br/>备注 ：不可以混淆
 */
public class BLEinter {
    /**
     * 蓝牙连接
     * @author : 红叶岭谷
     *
     * @date : 2017年12月5日
     *
     * <br/>蓝牙连接结果
     */
    public enum CountList{
        /**
         * 蓝牙连接成功
         */
        COUNT_SUCCESS("蓝牙连接成功",0),
        /**
         * 蓝牙连接断开
         */
        COUNT_DISCONNECTED("蓝牙连接断开",1),
        /**
         * 蓝牙连接失败
         */
        COUNT_FALL("蓝牙连接失败",2),
        /**
         * 蓝牙连接超时
         */
        COUNT_TIME_OUT("蓝牙连接超时",3);
        private String name;
        private int idex;
        private CountList(String enumName,int enumIdex){
            this.name = enumName;
            this.idex = enumIdex;
        }
    }
}

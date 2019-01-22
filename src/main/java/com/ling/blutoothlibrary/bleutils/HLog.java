package com.ling.blutoothlibrary.bleutils;

import android.util.Log;

/**
 * 创建时间 ：2017/10/11.
 * <br/>作者 ：红叶岭谷
 * <br/>日志信息打印
 * <br/>备注 ：不可以混淆
 */
public class HLog {

    public static void i(String packName,String message){
        Log.i(packName,message);
    }
    public static void w(String packName,String message){
        Log.w(packName,message);
    }
    public static void e(String packName,String message){
        Log.e(packName,message);
    }
}

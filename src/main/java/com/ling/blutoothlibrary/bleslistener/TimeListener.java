package com.ling.blutoothlibrary.bleslistener;

/**
 *  创建时间 ：2017/10/9.
 * <br/>作者 ：红叶岭谷
 * <br/>倒计时回调方法
 * <br/>备注 ：不可以混淆
 */
public class TimeListener {
    public interface CountDownListener {
        /**
         * 计时时间到
         *
         * @param tag 返回计时时间的唯一识别号
         */
        void countTimeEnd(int tag);
    }
    public interface HandlerDownListener{
        /**
         * 计时时间到
         */
        void countTimeEnd();
    }
}

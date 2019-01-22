package com.ling.blutoothlibrary.timeTask;

import android.os.Handler;
import com.ling.blutoothlibrary.bleslistener.TimeListener;

/**
 * ===========================================
 * <br/>创建时间 ：2017/11/19.
 * <br/>作者 ：红叶岭谷
 * <br/>描述:该类用来进行时间倒计时需要new对象调启动函数
 * <br/>描述:结束计时函数必须通过对象
 * <br/>备注 ：不可以混淆
 * <br/>======================================
 */
public class HandlerDownTimerHY {
    private Handler handler_M;
    private Runnable runnable_M;
    private long time_M;

    public HandlerDownTimerHY(long time, final TimeListener.HandlerDownListener listener) {
        this.time_M = time;
        handler_M = new Handler();
        runnable_M = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                time_M -= 100;
                if (time_M > 0) {
                    handler_M.postDelayed(runnable_M, 100);
                } else {
                    stopTime();
                    listener.countTimeEnd();
                }
            }
        };
    }

    /**
     * 启动计时
     */
    public void startTime() {
        if (handler_M != null) {
            handler_M.post(runnable_M);
        }
    }

    /**
     * 结束计时
     */
    public void stopTime() {
        if (handler_M != null) {
            handler_M.removeCallbacks(runnable_M);
        }
    }
}

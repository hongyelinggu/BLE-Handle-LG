package com.ling.blutoothlibrary.timeTask;

import android.os.CountDownTimer;
import android.util.SparseArray;
import com.ling.blutoothlibrary.bleslistener.TimeListener;

/**
 * ===========================================
 * <br/>创建时间 ：2017/10/9.
 * <br/>作者 ：红叶岭谷
 * <br/>描述:该类用来进行时间倒计时不需要new对象通过静态调启动函数
 * <br/>描述:结束计时函数必须通过计时唯一识别码
 * <br/>备注 ：不可以混淆
 * <br/>======================================
 */
public class CountDownTimerHY extends CountDownTimer {

    TimeListener.CountDownListener downTime = null;

    private int tag;

    private static SparseArray<CountDownTimerHY> sparseTimeTasks = new SparseArray<CountDownTimerHY>();

    /**
     * 倒计时对象
     *
     * @param taskTag           位置标志
     * @param millisInFuture    总时长
     * @param countDownInterval 回调时间
     * @param taskDownTime      间隔时间
     */
    private CountDownTimerHY(int taskTag, long millisInFuture, long countDownInterval, TimeListener.CountDownListener taskDownTime) {
        super(millisInFuture, countDownInterval);
        this.tag = taskTag;
        this.downTime = taskDownTime;
    }

    @Override
    public void onTick(long l) {

    }

    @Override
    public void onFinish() {
        if (downTime != null) {
            downTime.countTimeEnd(tag);
        }
    }


    /**
     * 启动计时
     * @param TaskTag 计时唯一识别id
     * @param millisInFuture 计时时长
     * @param downTimeListener 计时结束回调接口
     */
    public static void startCount(int TaskTag, long millisInFuture, TimeListener.CountDownListener downTimeListener) {
        CountDownTimerHY timeTask = new CountDownTimerHY(TaskTag, millisInFuture, 1000, downTimeListener);
        timeTask.start();
        sparseTimeTasks.put(TaskTag, timeTask);
    }

    /**
     * 停止计时
     *
     * @param TaskTag 计时唯一标识
     */
    public static void stopCount(int TaskTag) {
        CountDownTimerHY timeTask = sparseTimeTasks.get(TaskTag);
        if (timeTask != null) {
            timeTask.cancel();
            sparseTimeTasks.remove(TaskTag);
        }
    }
}
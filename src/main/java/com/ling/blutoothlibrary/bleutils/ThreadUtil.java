package com.ling.blutoothlibrary.bleutils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 红叶岭谷
 * @create 2018/12/29
 * @Describe
 */
public class ThreadUtil {
    private static ThreadUtil instance;
    private static ThreadPoolExecutor poolExecutor;

    private ThreadUtil() {}

    /**
     * 对获取实例的方法进行同步
     * @return
     */
    public static ThreadUtil getInstance() {
        if (instance == null) {
            synchronized (ThreadUtil.class) {
                if (instance == null) {
                    instance = new ThreadUtil();
                }
                if (poolExecutor==null){
                    poolExecutor = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingDeque<Runnable>(1024), new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return instance;
    }

    /**
     * 终止所有线程
     */
    public void shutdown(){
        if (poolExecutor!=null){
            poolExecutor.shutdown();
        }
    }

    /**
     * 线程池添加线程
     */
    public void addThreadPool(Runnable command){
        if (poolExecutor!=null){
            poolExecutor.execute(command);
        }
    }



}

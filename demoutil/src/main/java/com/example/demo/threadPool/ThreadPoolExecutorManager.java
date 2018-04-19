package com.example.demo.threadPool;

import java.util.concurrent.*;
public class ThreadPoolExecutorManager {


    /****************************初始化数据线程池************************************************************/
    private static int INIT_DATA_THREAD_NUM = 40; // 核心线程数

    private static int INIT_DATA_TASK_LIMIT = 80;   //最大任务数

    private final static int INIT_DATA_MAX_THREAD_NUM = 80; // 可配置的线程数

    private static long INIT_DATA_THREAD_KEEP_ALIVE_TIME = 180L; // 空闲线程存活时间（不会结束核心线程）

    private static BlockingQueue<Runnable> transitionDataQueue = new LinkedBlockingQueue<>(INIT_DATA_TASK_LIMIT); //初始化数据队列

    private static ThreadPoolExecutor transitionDataThreadPoolExecutor= new ThreadPoolExecutor(INIT_DATA_THREAD_NUM, INIT_DATA_MAX_THREAD_NUM,
            INIT_DATA_THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS, transitionDataQueue, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"producers" + r.hashCode());//线程名
        }
    });


    public static java.util.concurrent.ThreadPoolExecutor getproducerDataThreadPoolExecutor(){
        return transitionDataThreadPoolExecutor;
    }



    /****************************初始化数据线程池************************************************************/
    private static int INIT_DATA_THREAD_NUM_BQ = 40; // 核心线程数

    private static int INIT_DATA_TASK_LIMIT_BQ = 40;   //最大任务数

    private final static int INIT_DATA_MAX_THREAD_NUM_BQ = 40; // 可配置的线程数

    private static long INIT_DATA_THREAD_KEEP_ALIVE_TIME_BQ = 180L; // 空闲线程存活时间（不会结束核心线程）

    private static BlockingQueue<Runnable> transitionDataBlockingDeque = new LinkedBlockingQueue<>(INIT_DATA_TASK_LIMIT_BQ); //初始化数据队列

    private static ThreadPoolExecutor transitionDataThreadExecutor= new ThreadPoolExecutor(INIT_DATA_THREAD_NUM_BQ, INIT_DATA_MAX_THREAD_NUM_BQ,
            INIT_DATA_THREAD_KEEP_ALIVE_TIME_BQ, TimeUnit.SECONDS, transitionDataBlockingDeque, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"consumers" + r.hashCode());//线程名
        }
    });

    public static java.util.concurrent.ThreadPoolExecutor getConsumerDataThreadExecutor(){
        transitionDataThreadExecutor.allowCoreThreadTimeOut(true);//使得核心线程有有效时间
        return transitionDataThreadExecutor;
    }




    /****************************初始化队列************************************************************/

    //队列最大存储
    public  static final int BQMAX = 100000;
    public final static BlockingDeque<Object> originalBQ = new LinkedBlockingDeque<>(BQMAX);


    public static Object originalBQPoll() {
        return originalBQ.poll();
    }

    public static int originalBQSize() {
        return originalBQ.size();
    }


    public static void originalBQOffer(Object o) {
        if (o == null) {
            return ;
        }
        try {
            originalBQ.put(o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws Exception {
//        ThreadPoolExecutorManager.getConsumerDataThreadExecutor().execute(
//                new UpdateFullTransitionTrear(transitionMapper,transitionFullMapper)
//        );
    }
}

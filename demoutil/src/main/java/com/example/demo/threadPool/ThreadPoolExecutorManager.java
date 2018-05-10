package com.example.demo.threadPool;

import java.util.concurrent.*;
public class ThreadPoolExecutorManager {


    /****************************初始化数据线程池************************************************************/
    private static int INIT_DATA_THREAD_NUM = 20; // 核心线程数

    private static int INIT_DATA_TASK_LIMIT = 100;   //最大任务数

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
    /**
     * 工作原理

     1、线程池刚创建时，里面没有一个线程。任务队列是作为参数传进来的。不过，就算队列里面有任务，线程池也不会马上执行它们。

     2、当调用 execute() 方法添加一个任务时，线程池会做如下判断：

     a. 如果正在运行的线程数量小于 corePoolSize，那么马上创建线程运行这个任务；

     　　 b. 如果正在运行的线程数量大于或等于 corePoolSize，那么将这个任务放入队列。

     　　 c. 如果这时候队列满了，而且正在运行的线程数量小于 maximumPoolSize，那么还是要创建线程运行这个任务；

     　　 d. 如果队列满了，而且正在运行的线程数量大于或等于 maximumPoolSize，那么线程池会抛出异常，告诉调用者“我不能再接受任务了”。

     3、当一个线程完成任务时，它会从队列中取下一个任务来执行。

     4、当一个线程无事可做，超过一定的时间（keepAliveTime）时，线程池会判断，如果当前运行的线程数大于 corePoolSize，那么这个线程就被停掉。所以线程池的所有任务完成后，它最终会收缩到 corePoolSize 的大小。

     　 　这样的过程说明，并不是先加入任务就一定会先执行。假设队列大小为 10，corePoolSize 为 3，maximumPoolSize 为 6，那么当加入 20 个任务时，
            执行的顺序就是这样的：首先执行任务 1、2、3，然后任务 4~13 被放入队列。这时候队列满了，任务 14、15、16 会被马上执行，而任务 17~20 则会抛出异常。
            最终顺序是：1、2、3、14、15、16、4、5、6、7、8、9、10、11、12、13。下面是一个线程池使用的例子：
      */
    private static int INIT_DATA_THREAD_NUM_BQ = 10; // 核心线程数

    private static int INIT_DATA_TASK_LIMIT_BQ = 30;   //最大任务数

    private final static int INIT_DATA_MAX_THREAD_NUM_BQ = 60; // 可配置的线程数

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

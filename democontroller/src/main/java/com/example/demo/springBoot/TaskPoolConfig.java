package com.example.demo.springBoot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
class TaskPoolConfig {

    /**
     * 核心线程数30：线程池创建时候初始化的线程数
     * 最大线程数500：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     * 缓冲队列2000：用来缓冲执行任务的队列
     * 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
     * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
     * 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，
     * 该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
     *
     * 1、直接丢弃（DiscardPolicy）
     * 2、丢弃队列中最老的任务(DiscardOldestPolicy)。
     * 3、抛异常(AbortPolicy)
     * 4、将任务分给调用线程来执行(CallerRunsPolicy)。
     * 这四种策略是独立无关的，是对任务拒绝处理的四中表现形式。最简单的方式就是直接丢弃任务。
     * 但是却有两种方式，到底是该丢弃哪一个任务，比如可以丢弃当前将要加入队列的任务本身（DiscardPolicy）或者丢弃任务队列中最旧任务（DiscardOldestPolicy）。
     * 丢弃最旧任务也不是简单的丢弃最旧的任务，而是有一些额外的处理。除了丢弃任务还可以直接抛出一个异常（RejectedExecutionException），这是比较简单的方式。
     * 抛出异常的方式（AbortPolicy）尽管实现方式比较简单，但是由于抛出一个RuntimeException，因此会中断调用者的处理过程。
     * 除了抛出异常以外还可以不进入线程池执行，在这种方式（CallerRunsPolicy）中任务将有调用者线程去执行。
     *
     * @return
     */


    @Bean("checkTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(500);
        executor.setQueueCapacity(2000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("check-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }



    /**
     * 异步存储校验结果数据
     */
    @Async("checkTaskExecutor")
    public Future<String> asyncInsertResultTask() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        System.out.println("result任务耗时:" + (end - start));
        return new AsyncResult<>("result任务执行完毕");
    }
}


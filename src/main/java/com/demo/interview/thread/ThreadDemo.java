package com.demo.interview.thread;

import java.util.concurrent.*;

public class ThreadDemo {

    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;
    private TimeUnit unit;
    private BlockingQueue<Runnable> workQueue;
    private ThreadFactory threadFactory;
    private RejectedExecutionHandler handler;

    private ExecutorService executorService = null;

    public void thread(){

        executorService = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,workQueue,threadFactory,handler);

        executorService.submit(()->{});
    }


}

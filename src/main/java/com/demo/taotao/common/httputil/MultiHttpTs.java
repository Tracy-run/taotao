package com.demo.taotao.common.httputil;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiHttpTs {

    @Test
    public void httpTs() throws InterruptedException {

//        request("测试4-1", 1000, 100);
        request("测试4-2", 10000, 100);
//	        request("测试4-3", 100000, 100);
        //不使用http连接池
        //测试4-1 ==>耗时: 3697.0毫秒
        //测试4-2 ==>耗时: 30483.0毫秒

        //使用http连接池
        //测试4-1 ==>耗时: 2023.0毫秒
        //测试4-2 ==>耗时: 10909.0毫秒

    }

    /**
     * @param msg
     * @param requestSize
     * @param threadSize
     * @throws InterruptedException
     */
    private void request(String msg, int requestSize, int threadSize) throws InterruptedException {
        CountDownLatch countDownLatch = null;
        AtomicInteger atomicInteger = null;

        countDownLatch = new CountDownLatch(requestSize);
        atomicInteger = new AtomicInteger(requestSize);

        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        //使用线程池
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(new DownRunnable(countDownLatch, atomicInteger));
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();


        double time = (endTime - startTime);
        System.out.println(msg + " ==>耗时: " + time + "毫秒");
    }


    /**
     * 下载线程
     */
    private class DownRunnable implements Runnable {
        private CountDownLatch countDownLatch;
        private AtomicInteger atomicInteger;
        public static final String host = "https://www.baidu.com";

        public DownRunnable(CountDownLatch countDownLatch, AtomicInteger atomicInteger) {
            super();
            this.countDownLatch = countDownLatch;
            this.atomicInteger = atomicInteger;
        }

        @Override

        public void run() {
            try {
                while (atomicInteger.decrementAndGet() >= 0) {
                    try {
                        String reposne = HttpClientUtils.get(host);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    countDownLatch.countDown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

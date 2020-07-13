package com.demo.interview.thread.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * t1 与 t2   t2添加的list为5的时候 t1运行结束
 *
 *
 */
public class MyCountDownCatch {

    //保证可见性  但不保证原子性
    volatile List<Object> list = new ArrayList<Object>();

    public void add(Object o ){
        list.add(o);
    }

    public int size(){
        return list.size();
    }


    public static void main(String[] args) {
        MyCountDownCatch mylatch = new MyCountDownCatch();

        CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t1启动");
                if(mylatch.size() != 5){
                    try {
                        latch.await();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("t1结束");
            }
        },"thread1").start();


        new Thread(() ->{
            System.out.println("t2启动");
            for(int i =0; i<10; i++){
                mylatch.add(new Object());
                System.out.println("add : " + i);

                if(mylatch.size() == 5){
                    latch.countDown();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("T2结束");
        },"thread2").start();

    }

}

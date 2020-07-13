package com.demo.interview.thread.threads;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 写一个固定容量的同步容器，拥有put和get方法，一个getCount方法
 * 能够支持俩个生产者线程以及10个以上的消费者线程阻塞调用
 *
 *
 * 用lock和condition 实现 可以实现到更加精确的指定线程被唤醒
 *
 */
public class MylockCondition {

    final private LinkedList<Object> lists = new LinkedList<Object>();
    final private int MAX = 10;//最多10个元素
    private int count = 0;

    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();


    /**
     * 生产者
     * @param t
     */
    public void put (Object t){
        try{
            lock.lock();
            while(lists.size() == MAX){
                producer.await();
            }

            lists.add(t);
            ++count;
            consumer.signalAll();//通知消费者线程进行消费
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public Object get(){
        Object t = null;
        try{
            lock.lock();
            while (lists.size() == 0) {
                consumer.await();
            }

            t = lists.removeFirst();
            count--;
            producer.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }

        return t;
    }


    public static void main(String[] args) {
        MylockCondition mylock = new MylockCondition();

        for(int i =0 ;i<10;i++){
            new Thread(() ->{
                for(int j=0;j<5;j++){
                    System.out.println(mylock.get());
                }
            },"consumer" + i).start();
        }

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        for(int n =0;n<2;n++){
            new Thread(() ->{
                for(int m =0;m<25;m++){
                    mylock.put(Thread.currentThread().getName() + " " + m);
                }
            },"producer" + n).start();
        }

    }

}

package com.demo.interview.thread.threads;

import javax.validation.constraints.Max;
import java.util.LinkedList;


/**
 * 写一个固定容量的同步容器，拥有put和get方法，一个getCount方法
 * 能够支持2个生产者线程以及10个以上的消费者线程阻塞调用
 *
 * 使用wait和notify/notifyAll 来实现
 *
 */
public class MySynchronized {


    final private LinkedList<Object> lists = new LinkedList<Object>();
    final private int MAX = 10;
    private int count = 0;

    /**
     * 生产
     * @param o
     */
    public synchronized void put(Object o){
        while(lists.size() == MAX){ //为什么用while 不用if ？？
            try{
              this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        lists.add(o);
        ++count;
        this.notifyAll();//通知消费者线程进行消费

    }

    /**
     * 消费
     * @return
     */
    public synchronized Object get(){
        Object o = null;
        while(lists.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        o = lists.removeFirst();
        count--;
        this.notifyAll();
        return o;
    }


    public static void main(String[] args) {
        MySynchronized mys = new MySynchronized();

        //启动消费者
        for(int i=0;i<10;i++){
            new Thread(() -> {
                for(int j=0;j<5;j++){
                    System.out.println(mys.get());
                }
            },"C" + i).start();
        }
        try {
            Thread.sleep(2000);
        }catch(Exception e){
            e.printStackTrace();
        }

        //启动生产者
        for(int i =0;i<2;i++){
            new Thread(() -> {
                for (int j =0;j < 25;j++){
                    mys.put(Thread.currentThread().getName() + " " + j);
                }
            },"p" + i).start();
        }

    }







}

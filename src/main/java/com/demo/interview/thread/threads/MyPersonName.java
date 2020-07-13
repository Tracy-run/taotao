package com.demo.interview.thread.threads;

import java.util.concurrent.TimeUnit;

public class MyPersonName {

    volatile static person p = new person();

    public static void main(String[] args) {
        new Thread(() -> {
            try{
                TimeUnit.SECONDS.sleep(2);
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println(p.name);
        }).start();

        new Thread(() ->{
            try{
                TimeUnit.SECONDS.sleep(1);
            }catch (Exception e){
                e.printStackTrace();
            }
            p.name = "lisi";
        }).start();
    }


}


class person{

    String name = "zhangsan";
}
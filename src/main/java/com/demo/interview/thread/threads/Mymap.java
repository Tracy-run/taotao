package com.demo.interview.thread.threads;


import java.util.Hashtable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

/**
 * 计时
 */
public class Mymap {



    public static void main(String[] args) {


//    Hashtable<String,String> map = new Hashtable<String,String>();

//    Map<String,String> map = new ConcurrentHashMap<String,String>();

//    Map<String,String> map = new ConcurrentSkipListMap<String,String>();

        Map<String, String> map = new HashMap<String, String>();


        Random r = new Random();
        Thread [] thr = new Thread[1000];

        CountDownLatch latch = new CountDownLatch(1000);

        long start = System.currentTimeMillis();

        for(int i=0;i<thr.length;i++){

            thr[i] = new Thread(() -> {
               for(int j = 0;j<10000;j++){
                   map.put("a" + r.nextInt(100000),"a" + r.nextInt(100000));
                   latch.countDown();
               }
            });
        }

        Arrays.asList(thr).forEach(t -> t.start());

        try {
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println(start - end);
    }


}

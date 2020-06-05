package com.demo.taotao.aqs.current;

import com.demo.taotao.aqs.util.UnsafeInstance;
import sun.misc.Unsafe;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.LockSupport;

public class MyLock {

    /**
     *
     */
    public volatile int state = 0;

    /**
     * 当前加锁状态 记录加锁次数
     */
    public ConcurrentLinkedDeque<Thread> waiters = new ConcurrentLinkedDeque<>();

    /**
     * 当前持有锁的线程
     */
    public Thread lockHolder;

    private static final Unsafe unsafe = UnsafeInstance.reflectGetUnsafe();

    private static /*final*/ long stateOffset;

    static {
        try{
            stateOffset = unsafe.objectFieldOffset(MyLock.class.getDeclaredField("state"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void setLockHolder(Thread lockHolder){
        this.lockHolder = lockHolder;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }

    public boolean acquire(){
        //cas比较与交换  原子算法
        Thread current = Thread.currentThread();

        int c = getState();

        if(c == 0){ //同步器还没有被持有
            // 锁没有被持有 或者 锁被当前唤醒的线程持有
            if((waiters.size() == 0 || current == waiters.peek()) && compareAndSwapState(0,1) ){
                setLockHolder(current);
                return true;
            }
        }
        return false;
    }


    public void lock(){
        //加锁成功
        if(acquire()){
            return;
        }
        //cas比较与交换  原子算法
        Thread current = Thread.currentThread();
        waiters.add(current);

        for(;;){
            //让出cpu使用权
            // Thread.yield();


            if((current == waiters.peek()) && acquire()){
                waiters.poll(); //t2 从等待队列中移除
                return;
            }
            //阻碍当前线程
            LockSupport.park(current); //保存对线程的调用
        }
    }


    public void unlock(){
        if(Thread.currentThread() != lockHolder){
            throw new RuntimeException("lockholder is not current thread");
        }
        int state = getState();
        if(compareAndSwapState(state,0)){
            setLockHolder(null);
            Thread first = waiters.peek();
            if(first != null){
                LockSupport.unpark(first);
            }
        }
    }


    public final boolean compareAndSwapState(int except,int update){
        return unsafe.compareAndSwapInt(this,stateOffset,except,update);
    }

}

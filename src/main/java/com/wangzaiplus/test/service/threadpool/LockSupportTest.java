package com.wangzaiplus.test.service.threadpool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程交替打印
 */
public class LockSupportTest {

    private static final char[] a = "abcd".toCharArray();
    private static final char[] A = "ABCD".toCharArray();

    private static Thread aThread;
    private static Thread AThread;

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private volatile AtomicInteger flag = new AtomicInteger(0);

    /**
     * 基于LockSupport 线程唤醒和挂起操作实现
     */
    public static void method1(){
        aThread = new Thread(() -> {
            for (char c : a) {
                System.out.println(c);
                // 唤醒AThread线程
                LockSupport.unpark(AThread);
                // 挂起当前线程
                LockSupport.park();
            }
        });

        AThread = new Thread(() -> {
            for (char c : A) {
                System.out.println(c);
                LockSupport.park();
                LockSupport.unpark(aThread);
            }
        });

        aThread.start();
        AThread.start();
    }

    /**
     * sync + notify/wait 机制实现
     */
    public void method2(){
        aThread = new Thread(()->{
            synchronized (this){
                for (char c : a){
                    this.notify(); // 唤醒线程
                    System.out.println(c);
                    try {
                        this.wait(); // 阻塞线程
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        AThread = new Thread(() -> {
            synchronized (this){
                for (char c : A) {
                    this.notify();
                    System.out.println(c);
                    try {
                        this.wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        aThread.start();
        AThread.start();
    }

    /**
     * ReentrantLock+Condition 机制实现
     */
    public void  method3(){
        aThread = new Thread(()->{
            for (char c : a){
                try {
                    lock.lock();
                    System.out.println(c);
                    condition.signal();
                    try {
                        condition.await();// 让出锁
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }finally {
                    lock.unlock();
                }
            }
        });
        AThread = new Thread(() ->{
            for (char c : A){
                try {
                    lock.lock();
                    System.out.println(c);
                    condition.signal();
                    try {
                        condition.await(10, TimeUnit.NANOSECONDS);// 让出锁
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }finally {
                    lock.unlock();
                }
            }
        });
        aThread.start();
        AThread.start();
    }

    /**
     * 通过CAS 自旋的方式对两个线程通信
     */
    public void method4(){
        aThread = new Thread(()-> {
            for (char c : a) {
                for (; ; ) {
                    if ((flag.get() & 1) == 0) {
                        break;
                    }
                }
                System.out.println(c);
                flag.compareAndSet(flag.get(), 1);

            }
        });
        AThread = new Thread(() -> {
            for (char c : A) {
                for (; ; ) {
                    if ((flag.get() & 1) == 1) {
                        break;
                    }
                }
                System.out.println(c);
                flag.compareAndSet(flag.get(), 0);
            }
        });
        aThread.start();
        AThread.start();
    }
    public static void main(String[] args) {
        // method1();
        // new LockSupportTest().method2();
        // new LockSupportTest().method3();
        new LockSupportTest().method4();
    }
}

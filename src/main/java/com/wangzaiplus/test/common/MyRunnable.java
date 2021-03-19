package com.wangzaiplus.test.common;

import java.util.Date;

/**
 * @ClassName MyRunnable
 * @Description
 * @Author 李光华
 * @Date 2021/3/11 16:47
 **/
public class MyRunnable implements Runnable{

    private String command;

    public MyRunnable(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }

    private void processCommand(){
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MyRunnable{" +
                "command='" + this.command + '\'' +
                '}';
    }
}

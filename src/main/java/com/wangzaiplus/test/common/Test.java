package com.wangzaiplus.test.common;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName Test
 * @Description 指令重排的问题(这段代码会由于指令重排问题出现另一种结果)
 * @Author 李光华
 * @Date 2021/3/6 17:19
 **/
public class Test {
    private static int a = 0, b = 0;
    private static int x = 0, y = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            // 这是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待
            CountDownLatch latch = new CountDownLatch(1);
            Thread one = new Thread(() -> {
                try {
                    //阻塞等待；使当前线程在锁存器倒数至零之前一直等待，除非线程中断
                    latch.await();
                } catch (InterruptedException e) {

                }
                a = 1;
                x = b;
            });
            Thread other = new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {

                }
                b = 1;
                y = a;
            });
            //启动线程
            one.start();
            other.start();
            //递减锁存器的计数，如果计数达到零，则释放所有等待的线程
            latch.countDown();
            // 等待线程终止
            one.join();
            other.join();

            String result = "第" + i + "次(" + x + "," + y + ")";
            if (x == 0 && y == 0) {
                System.err.println(result);
                break;
            } else {
                System.out.println(result);
            }
        }
    }
}

package com.wangzaiplus.test.common;

/**
 * @ClassName Singleton
 * @Description 手写一个单例
 * @Author 李光华
 * @Date 2021/3/11 15:40
 **/
public class Singleton {

    private static volatile Singleton uniqueInstance;

    private Singleton(){}

    public static Singleton getInstance(){
        if (uniqueInstance == null){
            synchronized (Singleton.class){
                if (uniqueInstance == null){
                    // 这里会有指令重排问题
                    /**
                     * 1.为 uniqueInstance 分配内存空间
                     * 2.初始化 uniqueInstance
                     * 3.将 uniqueInstance 指向分配的内存地址
                     */
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}

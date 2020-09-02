package com.siwuxie095.concurrent.chapter3rd.example1st;

/**
 * @author Jiajing Li
 * @date 2020-09-03 07:16:42
 */
public class Main {

    /**
     * 多线程的团队协作：同步控制
     *
     *  同步控制是并发程序必不可少的重要手段。之前介绍的 synchronized 关键字就是一种最简单的控制方法。
     *  它决定了一个线程是否可以访问临界区资源。同时，Object.wait() 和 Object.notify() 方法起到了
     *  线程等待和通知的作用。这些工具对于实现复杂的多线程协作起到了重要的作用。
     *
     *  其实对于 synchronized、Objects.wait() 和 Object.notify() 方法，JDK 也提供了替代品（或者
     *  说是增强版），那就是重入锁 ReentrantLock 和条件 Condition。
     */
    public static void main(String[] args) {

    }

}

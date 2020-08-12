package com.siwuxie095.concurrent.chapter1st.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-01-07 23:18:54
 */
public class Main {

    /**
     * 并发级别：
     * 由于临界区的存在，多线程之间的并发必须受到控制。根据控制并发的策略，可以把并发的级别
     * 进行分类，大致可以分为五种：阻塞、无饥饿、无障碍、无锁、无等待。
     *
     * （1）阻塞（Blocking）
     *  一个线程是阻塞的，那么在其他线程释放资源之前，当前线程无法继续执行。当使用 synchronized
     *  关键字，或者 ReentrantLock 重入锁时，得到的就是阻塞的线程。
     *
     *  无论是 synchronized 或者 ReentrantLock，都会试图在执行后续代码前，得到临界区的
     *  锁，如果得不到，线程就会被挂起等待，直到占有了所需资源为止。
     *
     *  总结：当出现 synchronized 关键字或 ReentrantLock 重入锁，并发级别就为阻塞。
     */
    public static void main(String[] args) {

    }

}

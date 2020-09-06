package com.siwuxie095.concurrent.chapter3rd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 23:05:37
 */
public class Main {

    /**
     * 自定义线程创建：ThreadFactory
     *
     * 线程池的主要作用是为了线程复用，也就是为了避免线程的频繁创建。但是最开始的那些线程从何而来呢？答案就是
     * ThreadFactory。
     *
     * ThreadFactory 是一个接口，它只有一个方法，用来创建线程：
     *
     * Thread newThread(Runnable r)
     *
     * 当线程池需要新建线程时，就会调用这个方法。
     *
     * 自定义线程池可以帮助我们做不少事。比如，可以跟踪线程池究竟在何时创建了多少线程，也可以自定义线程的名称、
     * 组以及优先级等信息，甚至可以任性地将所有线程设置为守护线程。总之，使用自定义线程池可以更加自由地设置线
     * 程池中所有线程的状态。
     *
     * 以 ThreadFactoryDemo 为例，其中使用了自定义的 ThreadFactory，一方面记录了线程的创建，另一方面将
     * 所有的线程都设置为守护线程，这样当主线程退出后，会强制销毁线程池。
     */
    public static void main(String[] args) {

    }

}

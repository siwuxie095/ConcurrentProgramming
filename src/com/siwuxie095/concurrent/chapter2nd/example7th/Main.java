package com.siwuxie095.concurrent.chapter2nd.example7th;

/**
 * @author Jiajing Li
 * @date 2020-08-28 07:06:36
 */
public class Main {
    /**
     * 线程的基本操作：挂起(suspend)和继续执行(resume)线程
     *
     * 阅读 JDK 有关 Thread 类的 API 文档，不难发现两个看起来非常有用的接口,即线程挂起(suspend)
     * 和继续执行(resume)。这两个操作是一对相反的操作，执行 suspend() 后被挂起的线程，必须要等到
     * resume() 操作后，才能继续执行。乍一看，这对操作就像 stop() 方法一样好用。但实际上，截止到
     * 目前（2020/08/28），这两个方法同样是被标注为废弃的方法（参照版本：Java 8），并不推荐使用。
     *
     * 之所以不推荐使用 suspend() 去挂起线程，是因为 suspend() 在导致线程暂停的同时，并不会去释放
     * 任何锁资源。此时，其他任何线程想要访问被它暂用的锁时，都会被牵连，导致无法正常继续运行。直到
     * 对应的线程上进行了 resume() 操作，被挂起的线程才能继续，从而其他所有阻塞在相关锁上的线程也可
     * 以继续执行。但是，如果 resume() 操作意外地在 suspend() 前就执行了，那么被挂起的线程可能很难
     * 有机会被继续执行。这就进入了类似死锁的状态。并且，更严重的是：它所占用的锁不会被释放，因此可能
     * 会导致整个系统工作不正常。而且对于被挂起的线程，从线程状态上看，居然还是 Runnable，这也会影响
     * 对系统当前状态的判断。
     *
     * 以 BadSuspend 为例，开启 t1 和 t2 两个线程。它们会通过对象锁 u 实现对临界区的访问。在主函数
     * 中，线程 t1 和 t2 启动后，对其进行 resume()，目的是让它们继续得以执行。接着，主函数等待着两个
     * 线程的结束。
     *
     * 但实际运行后发现，两个线程会先后进入临界区，但是程序却不会退出，而是会挂起。通过 jstack 命令
     * 或者 IDEA 自带的 Dump Threads 功能（一个类似照相机的图标），可以看到线程 t2 其实是被挂起的。
     * 但是它的线程状态确实是 RUNNABLE，这很有可能使开发者误判当前系统的状态。同时，虽然主函数中已经
     * 调用了 resume()，但是由于时间先后顺序的原因，那个 resume 并没有生效。这就导致了线程 t2 被永
     * 远挂起，并且永远占用了对象 u 的锁，这对于系统来说极有可能是致命的。
     *
     * "t2" #12 prio=5 os_prio=31 tid=0x00007f8c0081b000 nid=0xa803 runnable [0x00007000084d0000]
     *    java.lang.Thread.State: RUNNABLE
     * 	at java.lang.Thread.suspend0(Native Method)
     * 	at java.lang.Thread.suspend(Thread.java:1032)
     * 	at com.siwuxie095.concurrent.chapter2nd.example7th.BadSuspend$ChangeObjectThread.run(BadSuspend.java:34)
     * 	- locked <0x000000076aef03f0> (a java.lang.Object)
     *
     * 	如果需要一个比较可靠的 suspend() 函数，那应该怎么办？其实可以利用 wait() 和 notify() 以及
     * 	打标记的方式，在应用层面实现 suspend() 和 resume() 的功能。
     *
     * 	以 GoodSuspend 为例，给出一个标记变量 suspendMe，表示当前线程是否被挂起。同时增加了 suspendMe()
     * 	和 resumeMe() 两个方法，分别用于挂起线程和继续执行线程。
     *
     * 	t1 线程会先检查自己是否被挂起，如果是，则执行 wait() 方法进行等待。否则，进行正常的处理。当
     * 	线程继续执行时，resumeMe() 方法被调用，线程 t1 得到一个继续执行的 notify() 通知，并且清除
     * 	了挂起标记，从而得以正常执行。
     *
     * PS：
     * suspend = 暂停线程 = 挂起线程
     * resume = 恢复线程 = 继续执行线程
     */
    public static void main(String[] args) {

    }

}

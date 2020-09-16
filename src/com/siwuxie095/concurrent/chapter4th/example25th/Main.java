package com.siwuxie095.concurrent.chapter4th.example25th;

/**
 * @author Jiajing Li
 * @date 2020-09-16 07:22:42
 */
public class Main {

    /**
     * 死锁
     *
     * 在众多的应用程序中，使用锁的情况一般要多于无锁。因为对于应用来说，如果业务逻辑很复杂，会极大增加无锁的编程难度。
     * 但如果使用锁，就不得不对一个新的问题引起重视，那就是死锁。
     *
     * 通俗的说，死锁就是两个或者多个线程，相互占用对方需要的资源，而都不进行释放，导致彼此之间都相互等待对方释放资源，
     * 产生了无限制等待的现象。死锁一旦发生，如果没有外力介入，这种等待将永远存在，从而对程序产生严重的影响。
     *
     * 用来描述死锁问题的一个有名的场景是 "哲学家就餐" 问题。哲学家就餐问题可以这样表述，假设有五位哲学家围坐在一张圆
     * 形餐桌旁，做以下两件事情之一：吃饭，或者思考。吃东西的时候就停止思考，思考的时候也停止吃东西。餐桌中间有一大碗
     * 意大利面，每两个哲学家之间有一只餐叉。因为用一只餐叉很难吃到意大利面，所以假设哲学家必须用两只餐叉吃东西。他们
     * 只能使用自己左右手边的那两只餐叉。哲学家就餐问题，有时候也用米饭和筷子而不是用意大利面和餐叉来描述，因为很明显，
     * 吃米饭必须用两根筷子。
     *
     * 哲学家从来不交谈，这就很危险，可能产生死锁，每个哲学家都拿着左手的餐叉，永远都在等右边的餐叉（或者相反）。
     *
     * 最简单的情况就是只有两个哲学家，假设是 A 和 B。桌面也只有两个叉子。A 左手拿着其中一只叉子，B 也一样。这样他们
     * 的右手都在等待对方的叉子，并且这种等待会一直持续，从而导致程序永远无法正常执行。
     *
     * 以 DeadLockDemo 为例，模拟两个哲学家互相等待对方的叉子。哲学家 A 先占用叉子 1，哲学家 B 后占用叉子 2，接着
     * 他们就相互等待，都没有办法同时获得两个叉子用餐。
     *
     * 如果在实际环境中，遇到了这种情况，通常的表现就是相关的进程不再工作，并且 CPU 占有率为 0（因为死锁的线程不占用
     * CPU），不过这种表面现象只能用来猜测问题。如果想要确认问题，还需要使用 JDK 提供的一套专业工具。
     *
     *
     * 首先可以使用 jps 命令得到 Java 进程的进程 ID，接着使用 jstack 命令得到线程的线程堆栈。
     *
     * 在 terminal 中输入 jps，输出如下：
     *
     * 54274 Launcher
     * 54275 DeadLockDemo
     * 54278 Jps
     * 5159 Launcher
     * 5144 Launcher
     * 345
     *
     * 在 terminal 中输入 jstack 54275，输出如下：
     *
     * Found one Java-level deadlock:
     * =============================
     * "哲学家B":
     *   waiting to lock monitor 0x00007fae5183d808 (object 0x000000076ada91e8, a java.lang.Object),
     *   which is held by "哲学家A"
     * "哲学家A":
     *   waiting to lock monitor 0x00007fae50018558 (object 0x000000076ada91f8, a java.lang.Object),
     *   which is held by "哲学家B"
     *
     * Java stack information for the threads listed above:
     * ===================================================
     * "哲学家B":
     *         at com.siwuxie095.concurrent.chapter4th.example25th.DeadLockDemo.run(DeadLockDemo.java:47)
     *         - waiting to lock <0x000000076ada91e8> (a java.lang.Object)
     *         - locked <0x000000076ada91f8> (a java.lang.Object)
     * "哲学家A":
     *         at com.siwuxie095.concurrent.chapter4th.example25th.DeadLockDemo.run(DeadLockDemo.java:35)
     *         - waiting to lock <0x000000076ada91f8> (a java.lang.Object)
     *         - locked <0x000000076ada91e8> (a java.lang.Object)
     *
     * Found 1 deadlock.
     *
     * 上面显示了 jstack 的部分输出。可以看到，哲学家 A 和哲学家 B 两个线程发生了死锁，并且可以看到两者相互等待的锁的
     * ID。同时死锁的两个线程均处于 BLOCK 状态。
     *
     * 如果想避免死锁，除了使用无锁的方法外，另外一种有效的做法是使用重入锁，通过重入锁的中断或者限时等待可以有效规避死
     * 锁带来的问题。
     *
     * PS：也可以使用 IDEA 自带的 Dump Threads 功能（一个类似照相机的图标），也可以看到 Java 进程的内部线程及其堆栈。
     */
    public static void main(String[] args) {

    }

}

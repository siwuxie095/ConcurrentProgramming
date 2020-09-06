package com.siwuxie095.concurrent.chapter3rd.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 09:00:22
 */
public class Main {

    /**
     * 倒计数器：CountDownLatch
     *
     * CountDownLatch 是一个非常实用的多线程控制工具类。"CountDown" 在英文中意为倒计数，"Latch" 意为门闩。
     * 如果翻译成倒计数门闩，听起来可能会不知所云。因此，这里简单的称之为倒计数器。在这里，门闩的含义是：把门锁
     * 起来，不让里面的线程跑出来。这个工具通常用来控制线程等待，它可以让某一个线程等待直到倒计数结束，再开始执
     * 行。
     *
     * 对于倒计数器，一种典型的场景就是火箭发射。在火箭发射前，为了保证万无一失，往往要进行各项设备、仪器的检查。
     * 只有等所有的检查完毕后，引擎才能点火。这种场景就非常适合使用 CountDownLatch。它可以使得点火线程等待所有
     * 检查线程全部完工后，再执行。
     *
     * CountDownLatch 的构造函数接收一个整数作为参数，即当前这个倒计数器的计数个数。
     *
     * public CountDownLatch(int count)
     *
     * 以 CountDownLatchDemo 为例，生成一个 CountDownLatch 实例，将计数数量设为 10，表示需要有 10 个线程
     * 完成任务，等待在 CountDownLatch 上的线程才能继续执行。使用了 CountDownLatch.countdown() 方法来通知
     * CountDownLatch 一个线程已经完成了任务，倒计数器做减一。使用了 CountDownLatch.await() 方法要求主线程
     * 等待所有 10 个线程全部完成任务。待 10 个任务全部完成后，主线程才能继续执行。即 主线程在 CountDownLatch
     * 上等待，当所有任务全部完成后，主线程方能继续执行。
     *
     * PS：CountDownLatch 的中文翻译颇多，这里列出来，当听到这些名词时，知道说的是 CountDownLatch 即可。
     * （1）倒计数器（推荐）
     * （2）倒计时器
     * （3）倒计数锁存器
     * （4）同步计数器
     * （5）倒计数锁
     * （6）门锁
     * （7）闭锁（推荐）
     */
    public static void main(String[] args) {

    }

}

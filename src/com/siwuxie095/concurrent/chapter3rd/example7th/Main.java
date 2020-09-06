package com.siwuxie095.concurrent.chapter3rd.example7th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 10:09:19
 */
public class Main {

    /**
     * 循环栅栏：CyclicBarrier
     *
     * CyclicBarrier 是另外一种用于多线程并发控制的实用工具。和 CountDownLatch 非常类似，它也可以实现
     * 线程间的计数等待，但它的功能比 CountDownLatch 更加复杂且强大。
     *
     * CyclicBarrier 可以理解为循环栅栏。栅栏就是一种障碍物，比如，通常在私人宅邸的周围就可以围上一圈栅栏，
     * 防止闲杂人等入内。这里当然就是用来组织线程执行，要求线程在栅栏处等待。前面 Cyclic 意为循环，也就是
     * 说这个计数器可以反复使用。比如，假设将计数器设为 10，那么凑齐第一批 10 个线程后，计数器就会归零，
     * 然后接着凑齐下一批 10 个线程，这就是循环栅栏内在的含义。
     *
     * CyclicBarrier 的使用场景也很丰富。比如，司令下达命令，要求 10 个士兵一起去完成一项任务。这时，就
     * 会要求 10 个士兵先集合报道，接着，一起去执行任务。当 10 个士兵把自己手头上的任务都执行完成了，那么
     * 司令才能对外宣布任务完成。
     *
     * 比 CountDownLatch 略微强大一些，CyclicBarrier 可以接收一个参数作为 barrierAction，它表示当计
     * 数器一次计数完成后，系统会执行的动作。构造函数如下，其中 parties 表示计数总数，也就是参与的线程总数。
     *
     * public CyclicBarrier(int parties, Runnable barrierAction)
     *
     * 以 CyclicBarrierDemo 为例，演示了司令命令士兵完成任务的场景。先创建一个 CyclicBarrier 实例，并
     * 将计数器设置为 10。每一次调用 CyclicBarrier.await() 方法，就会从头开始计数（新一轮计数），每一个
     * 士兵线程都会等待，直到计数器达到指标时，会执行 BarrierAction。
     *
     * 第一次调用 CyclicBarrier.await()，所有士兵线程都会在这一行等待，当计数器达到指标时，表示所有士兵
     * 集结完毕，同时会执行 BarrierAction。然后所有士兵线程继续执行，开始 doWork() 执行任务。
     *
     * 第二次调用 CyclicBarrier.await()，所有士兵线程都会在这一行等待，当计数器达到指标时，表示所有士兵
     * 完成任务，同时会执行 BarrierAction。此时，整个程序执行完毕。
     *
     * PS：每一次调用 CyclicBarrier.await()，所有线程都会等待计数器从零打到满。
     *
     * CyclicBarrier.await() 可能会抛出两个异常。一个是 InterruptedException，也就是在等待过程中，线程
     * 被中断，应该说这是一个非常通用的异常。大部分迫使线程等待的方法都可能会抛出这个异常，使得线程在等待时依然
     * 可以响应外部紧急事件。另外一个则是 CyclicBarrier 特有的 BrokenBarrierException。一旦遇到这个异常，
     * 则表示当前的 CyclicBarrier 已经破损了，可能系统已经没有办法等待所有线程到齐了。如果继续等待，可能就是
     * 徒劳无功的，因此，还是就地散伙，打道回府（即 进行异常处理）。
     *
     * 如下，可以在本例中使第 5 个士兵线程产生中断。
     *
     * if (i == 4) {
     *     soldiers[i].interrupt();
     * }
     *
     * 很可能就会得到一个 1 个 InterruptedException 和 9 个 BrokenBarrierException。其中：
     * （1）InterruptedException 是被中断线程抛出的。
     * （2）BrokenBarrierException 是等待在当前 CyclicBarrier 上的线程抛出的。这个异常可以避免其他 9 个
     * 线程进行永久的、无谓的等待（因为其中一个线程已经被中断，等待是没有结果的）。
     *
     * PS：CyclicBarrier 的中文翻译颇多，这里列出来，当听到这些名词时，知道说的是 CyclicBarrier 即可。
     * （1）循环栅栏（推荐）
     * （2）回环栅栏
     * （3）可重用栅栏
     * （4）栅栏（推荐）
     * （5）循环屏障
     * （5）同步屏障
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter2nd.example6th;

/**
 * @author Jiajing Li
 * @date 2020-08-27 08:10:36
 */
public class Main {
    /**
     * 线程的基本操作：等待(wait)和通知(notify)
     *
     * 为了支持多线程之间的协作，JDK 提供了两个非常重要的接口：
     * （1）线程等待方法 wait()
     * （2）线程通知方法 notify()
     * 这两个方法不是在 Thread 类中，而是在 Object 类中。这也意味着任何对象都可以调用这两个方法。
     *
     * 这两个方法的签名如下：
     * public final void wait() throws InterruptException;
     * public final native void notify()
     *
     * 当在一个对象实例上调用 wait() 方法后，当前线程就会在这个对象上等待。比如线程 A 中，调用了
     * obj.wait() 方法，那么线程 A 就会停止继续执行，而转为等待状态。线程 A 会一直等待到其他线程
     * 调用了 obj.notify() 为止。这时，obj 对象俨然就成为了多个线程之间的有效通信手段。
     *
     * wait() 和 notify() 的工作过程：如果一个线程调用了 Object.wait()，那么它就会进入 Object
     * 对象的等待队列。这个等待队列中，可能会有多个线程，因为系统运行多个线程同时等待某一个对象。当
     * Object.notify() 被调用时，它就会从这个等待队列中，随机选择一个线程，并将其唤醒。值得注意的
     * 是，这个选择是不公平的，并不是先等待的线程会优先被选择，这个选择完全是随机的。
     *
     * 除了 notify() 方法外，Object 对象还有一个类似的 notifyAll() 方法，它和 notify() 的功能
     * 基本一致，但不同的是，它会唤醒在这个等待队列中所有等待的线程，而不是随机选择一个。
     *
     * 这里需要强调的是，Object.wait() 方法并不是随便调用的，它必须包含在对应的 synchronized 语
     * 句中，因为无论是 wait() 或者 notify() 都需要首先获得目标对象的一个监视器（Monitor）。
     *
     * 以 SimpleWaitNotify 为例，开启了两个线程 T1 和 T2。T1 执行了 wait() 方法。注意，在执行
     * wait() 方法前，T1 先申请了 OBJECT 的对象锁。因此，在执行 wait() 方法时，他是持有 OBJECT
     * 的锁的。wait() 方法执行后，T1 会进行等待，并释放 OBJECT 锁。T2 在执行 notify() 之前也会先
     * 获得 OBJECT 的对象锁。这里为了让实验效果明显，特意安排在 notify() 执行之后，让 T2 线程休眠
     * 2 秒，这样做可以更明显的说明，T1 在得到 notify() 通知后，还是会先尝试重新获得 OBJECT 对象锁。
     * 但实际上，在 T2 通知 T1 继续执行后，T1 并不能立即继续执行，而是要等待 T2 执行结束，释放 OBJECT
     * 对象锁，T1 才能继续执行。
     *
     * Object.wait() 和 Thread.sleep() 的异同：
     * （1）同：都可以让线程等待若干时间。
     * （2）异：
     * 1）wait() 可以被唤醒，而 sleep() 只能等待到时间结束。
     * 2）wait() 方法会释放目标对象的锁，而 sleep() 方法不会释放任何资源。
     *
     * 另外需要注意的是，wait() 方法会释放目标对象的锁，notify() 方法不会释放目标对象的锁。
     */
    public static void main(String[] args) {

    }

}

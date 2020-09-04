package com.siwuxie095.concurrent.chapter3rd.example3rd;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-04 07:19:42
 */
public class Main {

    /**
     * 重入锁的好搭档：Condition 条件
     *
     * 如果已经理解了 Object.wait() 和 Object.notify() 方法的话，那么就能很容易地理解 Condition 对象了。
     * 它和 wait()、notify() 方法的作用是大致相同的。但是 wait()、notify() 方法是和 synchronized 关键字
     * 合作使用的，而 Condition 是与重入锁 ReentrantLock 相关联的。通过 Lock 接口（重入锁就实现了这一接口）
     * 的 newCondition() 方法可以生成一个与当前重入锁绑定的 Condition 实例，利用 Condition 对象，就可以
     * 让线程在合适的时间等待，或者在某一个特定的时刻得到通知，继续执行。
     *
     * Condition 接口提供的基本方法如下：
     * void await() throws InterruptedException;
     * void awaitUninterruptibly();
     * long awaitNanos(long nanosTimeout) throws InterruptedException;
     * boolean await(long time, Timeout unit) throws InterruptedException;
     * boolean waitUntil(Date deadline) throws InterruptedException;
     * void signal();
     * void signalAll();
     *
     * 以上方法的含义如下：
     * （1）await() 方法会使当前线程等待，同时释放当前锁，当其他线程中使用 signal() 或者 signalAll() 方法时，
     * 线程会重新获得锁并继续执行。或者当线程被中断时，也能跳出等待。这和 Object.wait() 方法很相似。
     * （2）awaitUninterruptibly 方法与 await() 方法基本相同，但是它并不会在等待过程中响应中断。
     * （3）signal() 方法用于唤醒一个在等待中的线程。相对的 signalAll() 方法会唤醒所有等待中的线程。这和
     * Object.notify()、Object.notifyAll() 方法很类似。
     *
     * 以 ReenterLockCondition 为例，简单的演示了 Condition 的功能。先通过 lock 生成一个与之绑定的
     * Condition 对象。线程 t 在获取锁之后，在 Condition 对象上进行等待。然后由主线程 main 调用 signal()
     * 发出通知，告知等待在 Condition 对象上的线程 t 可以继续执行了。
     *
     * 和 Object.wait()、Object.notify() 方法一样，当线程使用 Condition.await() 时，要求线程持有相关的重入
     * 锁，在 Condition.await() 调用后，这个线程会自动释放这把锁。同理，Condition.signal() 方法调用时也要求
     * 线程先获得相关的重入锁，但是不会自动释放这把锁。在 signal() 方法调用后，系统会从当前 Condition 对象的等
     * 待队列中，唤醒一个线程。一旦线程被唤醒，它会重新尝试获得与之绑定的重入锁，一旦成功获取，就可以继续执行了。
     * 因此，在 signal() 方法调用后，一般需要释放相关的重入锁，谦让给被唤醒的线程，让它可以继续执行（如果不释放锁，
     * 被唤醒的线程无法重新获得锁，也就无法真正的继续执行）。
     *
     * 在 JDK 内部，重入锁和 Condition 对象被广泛的使用，以 ArrayBlockQueue 为例，参见如下两个方法：
     * （1）生产者(producer)调用的 {@link ArrayBlockingQueue#put} 方法。
     * （2）消费者(consumer)调用的 {@link ArrayBlockingQueue#take} 方法。
     */
    public static void main(String[] args) {

    }

}

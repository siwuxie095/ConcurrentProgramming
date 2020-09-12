package com.siwuxie095.concurrent.chapter4th.example5th;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-11 22:36:07
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 有助于提高锁性能的建议：锁分离
     *
     * 如果将读写锁的思想做进一步的延伸，就是锁分离。读写锁根据读写操作功能的不同，进行了有效的锁分离。依据应用程序的
     * 功能特点，使用类似的分离思想，也可以对独占锁进行分离。一个典型的案例就是 LinkedBlockingQueue 的实现。
     *
     * 在 LinkedBlockingQueue 中，take() 和 put() 方法分别实现了从队列中取得数据和往队列中增加数据的功能。虽然
     * 两个方法都对当前队列进行了修改操作，但由于 LinkedBlockingQueue 是基于链表的，因此，两个操作分别作用于队列
     * 的头部和尾部，从理论上说，两者并不冲突（只是理论上，所以还是有可能会冲突，所以使用了 notEmpty 和 notFull 来
     * 进行协调）。
     *
     * 如果使用独占锁，则要求在两个操作进行时获取当前队列的独占锁，那么 take() 和 put() 操作就不能真正的并发，在运
     * 行时，它们会彼此等待对方释放锁资源。在这种情况下，锁竞争会相对比较激烈，从而影响程序在高并发时的性能。
     *
     * 因此，在 JDK 的实现中，并没有采用这样的方式，取而代之的是两把不同的锁，分离了 take() 和 put() 操作。
     *
     *     private final ReentrantLock takeLock = new ReentrantLock();
     *     private final Condition notEmpty = takeLock.newCondition();
     *
     *     private final ReentrantLock putLock = new ReentrantLock();
     *     private final Condition notFull = putLock.newCondition();
     *
     * 以上代码片段，定义了 takeLock 和 putLock，它们分别在 take() 操作和 put() 操作中使用。这样，take() 方法就
     * 和 put() 方法相互独立了，二者之间不存在锁竞争关系，只需要在 take() 和 take() 间、put() 和 put() 间分别对
     * takeLock 和 putLock 进行竞争。通过这种锁分离的方式，就削弱了锁竞争的可能性。
     *
     * take() 方法的源码如下：
     *
     *     public E take() throws InterruptedException {
     *         E x;
     *         int c = -1;
     *         final AtomicInteger count = this.count;
     *         final ReentrantLock takeLock = this.takeLock;
     *         takeLock.lockInterruptibly();
     *         try {
     *             while (count.get() == 0) {
     *                 notEmpty.await();
     *             }
     *             x = dequeue();
     *             c = count.getAndDecrement();
     *             if (c > 1)
     *                 notEmpty.signal();
     *         } finally {
     *             takeLock.unlock();
     *         }
     *         if (c == capacity)
     *             signalNotFull();
     *         return x;
     *     }
     *
     * 值得注意的是，c 是 count 减 1 前的值。
     *
     *
     * put() 方法的源码如下：
     *
     *     public void put(E e) throws InterruptedException {
     *         if (e == null) throw new NullPointerException();
     *         int c = -1;
     *         Node<E> node = new Node<E>(e);
     *         final ReentrantLock putLock = this.putLock;
     *         final AtomicInteger count = this.count;
     *         putLock.lockInterruptibly();
     *         try {
     *             while (count.get() == capacity) {
     *                 notFull.await();
     *             }
     *             enqueue(node);
     *             c = count.getAndIncrement();
     *             if (c + 1 < capacity)
     *                 notFull.signal();
     *         } finally {
     *             putLock.unlock();
     *         }
     *         if (c == 0)
     *             signalNotEmpty();
     *     }
     *
     * 值得注意的是，c 是 count 加 1 前的值。
     *
     * 通过 takeLock 和 putLock 两把锁，LinkedBlockingQueue 实现了取数据（消费）和写数据（生产）的分离，使两者
     * 在真正意义上成为可并发的操作。
     */
    public static void main(String[] args) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    }

}

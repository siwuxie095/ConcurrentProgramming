package com.siwuxie095.concurrent.chapter3rd.example23th;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-10 08:15:07
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 数据共享通道：BlockingQueue
     *
     * 在所有类型的队列中，ConcurrentLinkedQueue 是一个高性能的队列，对于并发程序而言，高性能自然是需要追求的目标。
     * 但多线程的开发模式还会引入一个问题，那就是如何进行多个线程间的数据共享呢？比如，线程 A 希望给线程 B 发一个消息，
     * 用什么方式告知线程 B 是比较合理的呢？
     *
     * 一般来说，总是希望整个系统是松散耦合的（而不是紧密耦合的）。举一个现实中的例子，小区物业希望可以得到一些业主的
     * 意见，设立了一个意见箱，如果对物业有任何要求或者意见都可以投到意见箱中。这时，作为业主并不需要直接找到物业的工
     * 作人员表达你的意见。实际上，物业的工作人员也可能经常发生变动，直接找工作人员未必就方便。但是投递到意见箱的意见
     * 总是会被物业的工作人员看到，不管是否发生了人员变动。这样，就可以很容易表达自己的诉求了。既不需要直接和工作人员
     * 对话，又可以轻松提出自己的建议。
     *
     * 将这个模式映射到程序中，就是说，既希望线程 A 能够通知线程 B，又希望线程 A 不知道线程 B 的存在。这样，将来如果
     * 进行重构或者升级，完全可以不修改线程 A，而直接把线程 B 升级为线程 C，保证系统的平滑过渡。而这中间的 "传递者"
     * 就可以使用 BlockingQueue 来实现。
     *
     * BlockingQueue 是一个接口，并非一个具体的实现。它的主要实现有下面这些：
     *
     * SynchronousQueue
     * ArrayBlockingQueue
     * LinkedBlockingQueue
     * PriorityBlockingQueue
     *
     * BlockingDeque
     * LinkedBlockingDeque
     *
     * PS：BlockingDeque 也是一个接口，实现自 BlockingQueue。
     *
     * 这里主要介绍 ArrayBlockingQueue 和 LinkedBlockingQueue。从名字应该可以得知，前者是基于数组实现的，后者是
     * 基于链表实现的。也正因为如此，ArrayBlockingQueue 更适合做有界队列，因为队列中可容纳的最大元素需要在队列创建
     * 时指定（毕竟数组的动态扩展不太方便）。而 LinkedBlockingQueue 适合做无界队列。或者那些边界值非常大的队列，因
     * 为其内部元素可以动态增加，它不会因为初值容量很大，而一口气吃掉一大半的内存。
     *
     * 而 BlockingQueue 之所以适合作为数据共享的通道，其关键还在于 Blocking 上。Blocking 是阻塞的意思，当服务线程
     * （服务线程指不断获取队列中的消息，进行处理的线程）处理完成队列中的所有消息后，它如何知道下一条消息何时到来呢？
     *
     * 一种最傻瓜化的做法是让这个线程按照一定的时间间隔不停的循环和监控这个队列。这是一种可行的方案，但显然造成了不必要
     * 的浪费，而且循环周期也难以确定。而 BlockingQueue 很好的解决了这个问题。它会让服务线程在队列为空时进行等待，当
     * 有的消息进入队列后，自动将服务线程唤醒。
     *
     * 那它是如何实现的呢？以 ArrayBlockQueue 为例，来一探究竟。ArrayBlockQueue 的内部元素都放置在一个对象数组中：
     *
     * final Object[] items;
     *
     * 向队列中压入元素可以使用 offer() 方法和 put() 方法。对于 offer() 方法，如果当前队列已经满了，它就会立即返回
     * false。如果没有满，则执行正常的入队操作。这个方法比较简单，这里不过多赘述，这里更多需要关注的是 put() 方法。对
     * 于 put() 方法，它也是将元素压入队列末尾，但如果队列满了，它会一直等待，知道队列中有空闲的位置。
     *
     * 从队列中弹出元素可以使用 poll() 方法和 take() 方法。它们都是从队列头部获取一个元素。不同之处在于：如果队列为空，
     * poll() 方法直接返回 null，而 take() 方法会等待，直到队列内有可用元素。
     *
     * 因此，put() 方法和 take() 方法才是体现 Blocking 的关键。为了做好等待和通知两件事，在 ArrayBlockingQueue
     * 内部定义了以下一些字段：
     *
     * final ReentrantLock lock;
     * private final Condition notEmpty;
     * private final Condition notFull;
     *
     * 当执行 take() 操作时，如果队列为空，则让当前线程等待在 notEmpty 上。新元素入队时，则进行一次 notEmpty 上的
     * 通知。下面的代码展示了 take() 的过程。
     *
     *     public E take() throws InterruptedException {
     *         final ReentrantLock lock = this.lock;
     *         lock.lockInterruptibly();
     *         try {
     *             while (count == 0)
     *                 notEmpty.await();
     *             return dequeue();
     *         } finally {
     *             lock.unlock();
     *         }
     *     }
     *
     * 当队列为空时，即 count == 0，就要求当前线程进行等待。当队列中有新元素时，线程会得到一个通知。下面是元素入队时
     * 的一段代码：
     *
     *     private void enqueue(E x) {
     *         // assert lock.getHoldCount() == 1;
     *         // assert items[putIndex] == null;
     *         final Object[] items = this.items;
     *         items[putIndex] = x;
     *         if (++putIndex == items.length)
     *             putIndex = 0;
     *         count++;
     *         notEmpty.signal();
     *     }
     *
     * 当新元素进入队列后，需要通知等待在 notEmpty 上的线程，让它们继续工作。
     *
     * 同理，对于 put() 操作也是一样的，当队列满时，需要让压入线程等待（等待在 notFull 上）。下面的代码展示了 put()
     * 的过程：
     *
     *     public void put(E e) throws InterruptedException {
     *         checkNotNull(e);
     *         final ReentrantLock lock = this.lock;
     *         lock.lockInterruptibly();
     *         try {
     *             while (count == items.length)
     *                 notFull.await();
     *             enqueue(e);
     *         } finally {
     *             lock.unlock();
     *         }
     *     }
     *
     * 当有元素从队列中被挪走，队列中出现空位时，自然也需要通知等待入队的线程：
     *
     *     private E dequeue() {
     *         // assert lock.getHoldCount() == 1;
     *         // assert items[takeIndex] != null;
     *         final Object[] items = this.items;
     *         @SuppressWarnings("unchecked")
     *         E x = (E) items[takeIndex];
     *         items[takeIndex] = null;
     *         if (++takeIndex == items.length)
     *             takeIndex = 0;
     *         count--;
     *         if (itrs != null)
     *             itrs.elementDequeued();
     *         notFull.signal();
     *         return x;
     *     }
     *
     * 上述代码表示从队列中拿走一个元素。当有空闲位置时，通知等待入队的线程。
     */
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(8);
        queue.take();

    }

}

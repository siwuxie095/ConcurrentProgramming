package com.siwuxie095.concurrent.chapter4th.example24th;

/**
 * @author Jiajing Li
 * @date 2020-09-15 22:02:00
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 让线程之间互相帮助：细看 SynchronousQueue 的实现
     *
     * 同步队列 SynchronousQueue 是一个非常特殊的等待队列，它的容量为 0，任何一个对 SynchronousQueue 的写需要等待
     * 一个对 SynchronousQueue 的读，反之亦然。
     *
     * 因此，SynchronousQueue 与其说是一个队列，不如说是一个数据交换通道。那 SynchronousQueue 的奇妙功能是如何实现
     * 的呢？下面会一一道来。
     *
     * PS：SynchronousQueue 内部大量使用了无锁的 CAS 操作。
     *
     * SynchronousQueue 将 put() 和 take() 两个功能截然不同的操作抽象为一个共通的方法 Transferer.transfer()。
     * 从字面上看，这就是数据传递的意思。它的完整签名如下：
     *
     *         abstract E transfer(E e, boolean timed, long nanos);
     *
     * 当参数 e 为非空时，表示当前操作传递给一个消费者，如果为空，则表示当前操作需要请求一个数据。timed 参数决定是否
     * 存在 timeout 时间，nanos 决定了 timeout 的时长。如果返回值非空，则表示数据已经接受或者正常提供，如果为空，
     * 则表示失败（超时或者中断）。
     *
     * SynchronousQueue 内部会维护一个线程等待队列。等待队列中会保存等待线程以及相关数据的信息。比如，生产者将数据放入
     * SynchronousQueue 时，如果没有消费者接收，那么数据本身和线程对象都会打包在队列中等待（因为 SynchronousQueue
     * 容积为 0，没有数据可以正常放入）。
     *
     * Transferer.transfer() 方法的实现是 SynchronousQueue 的核心，它大体分为三个步骤：
     * （1）如果等待队列为空，或者队列中节点的类型和本次操作是一致的，那么将当前操作压入队列等待。比如，等待队列中是读
     * 线程等待，本次操作也是读，因此这两个读都需要等待，进入等待队列的线程可能会被挂起，它们会等待一个 "匹配" 的操作。
     *
     * （2）如果等待队列中的元素和本次操作是互补的（比如等待操作是读，而本次操作是写），那么就插入一个 "完成" 状态的
     * 节点，并且让它 "匹配" 到一个等待节点上。接着弹出这两个节点，并且使得对应的两个线程继续执行。
     *
     * （3）如果线程发现等待队列的节点就是 "完成" 节点，那么帮助这个节点完成任务。其流程和步骤（2）一致。
     *
     * 步骤（1）的实现如下：
     *
     *                 SNode h = head;
     *                 if (h == null || h.mode == mode) {  // empty or same-mode
     *                     if (timed && nanos <= 0) {      // can't wait
     *                         if (h != null && h.isCancelled())
     *                             casHead(h, h.next);     // pop cancelled node
     *                         else
     *                             return null;
     *                     } else if (casHead(h, s = snode(s, e, h, mode))) {
     *                         SNode m = awaitFulfill(s, timed, nanos);
     *                         if (m == s) {               // wait was cancelled
     *                             clean(s);
     *                             return null;
     *                         }
     *                         if ((h = head) != null && h.next == s)
     *                             casHead(h, s.next);     // help s's fulfiller
     *                         return (E) ((mode == REQUEST) ? m.item : s.item);
     *                     }
     *                 }
     *
     * 上述代码中，SNode 表示等待队列中的节点。内部封装了当前线程、next 节点、匹配节点、数据内容等信息。先判断当前
     * 等待队列为空，或者队列中元素的模式与本次操作相同（比如，都是读操作，那么都必须等待）。再生成一个新的节点并置
     * 于队列头部，这个节点就代表当前线程。如果入队成功，则执行 awaitFulfill() 函数。该函数会进行自旋等待，并最终
     * 挂起当前线程。直到一个与之对应的操作产生，将其唤醒。线程被唤醒后（表示已经读取到数据或者自己产生的数据已经被
     * 别的线程读取），尝试帮助对应的线程完成两个头部节点的出队操作（这仅仅是友情帮助）。并在最后，返回读取或者写入
     * 的数据。
     *
     * 步骤（2）的实现如下：
     *
     *                 } else if (!isFulfilling(h.mode)) { // try to fulfill
     *                     if (h.isCancelled())            // already cancelled
     *                         casHead(h, h.next);         // pop and retry
     *                     else if (casHead(h, s=snode(s, e, h, FULFILLING|mode))) {
     *                         for (;;) { // loop until matched or waiters disappear
     *                             SNode m = s.next;       // m is s's match
     *                             if (m == null) {        // all waiters are gone
     *                                 casHead(s, null);   // pop fulfill node
     *                                 s = null;           // use new node next time
     *                                 break;              // restart main loop
     *                             }
     *                             SNode mn = m.next;
     *                             if (m.tryMatch(s)) {
     *                                 casHead(s, mn);     // pop both s and m
     *                                 return (E) ((mode == REQUEST) ? m.item : s.item);
     *                             } else                  // lost match
     *                                 s.casNext(m, mn);   // help unlink
     *                         }
     *                     }
     *                 }
     *
     * 上述代码中，先判断头部节点是否处于 fulfill 模式。如果是，则需要进入步骤（3）。否则，将视自己为对应的 fulfill
     * 线程。再生成一个 SNode 元素，设置为 fulfill 模式并将其压入队列头部。接着，设置 m（原始的队列头部）为 s 的匹配
     * 节点，这个 tryMatch() 操作将会激活一个等待线程，并将 m 传递给那个线程。如果设置成功，则表示数据已经投递完成，
     * 将 s 和 m 两个节点弹出即可。如果 tryMatch() 失败，则表示已经有其他线程帮忙完成了操作，那么简单得删除 m 节点即
     * 可，因为这个节点的数据已经被投递，不需要再次处理，然后，再次跳转到小循环体（无限循环），进行下一个等待线程的匹配
     * 和数据投递，直到队列中没有等待线程为止。
     *
     * 步骤（3）的实现如下（如果线程在执行时，发现头部元素恰好是 fulfill 模式，它就会帮助这个 fulfill 节点尽快被执行）：
     *
     *                 } else {                            // help a fulfiller
     *                     SNode m = h.next;               // m is h's match
     *                     if (m == null)                  // waiter is gone
     *                         casHead(h, null);           // pop fulfilling node
     *                     else {
     *                         SNode mn = m.next;
     *                         if (m.tryMatch(h))          // help match
     *                             casHead(h, mn);         // pop both h and m
     *                         else                        // lost match
     *                             h.casNext(m, mn);       // help unlink
     *                     }
     *                 }
     *
     * 上述代码的执行原理和步骤（2）完全一致。唯一不同的是步骤（3）不会返回，因为步骤（3）所进行的工作是帮助其他线程
     * 尽快投递它们的数据，而自己并没有完成对应的操作。因此，线程进入步骤（3）后，再次进入大循环体（无限循环），从步
     * 骤（1）开始重新判断条件和投递数据。
     *
     * 从整个数据投递的过程中可以看到，在 SynchronousQueue 中，参与工作的所有线程不仅仅是竞争资源的关系。更重要的
     * 是，它们彼此之间还会互相帮助，在一个线程内部，可能会帮助其他线程完成它们的工作。这种模式可以更大程度上减少饥饿
     * 的可能，提高系统整体的并行度。
     */
    public static void main(String[] args) {

    }

}

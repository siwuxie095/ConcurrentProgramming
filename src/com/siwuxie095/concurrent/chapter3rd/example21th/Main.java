package com.siwuxie095.concurrent.chapter3rd.example21th;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-09 22:03:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 高效读写的队列：深度剖析 ConcurrentLinkedQueue
     *
     * 队列 Queue 是常用的数据结构之一。在 JDK 中提供了一个 ConcurrentLinkedQueue 类用来实现高并发的队列。
     * 截止到目前（2020/09/09），ConcurrentLinkedQueue 应该算是高并发环境中性能最好队列。它之所以有很好的
     * 性能，是因为其内部有很复杂的实现。
     *
     * 从 ConcurrentLinkedQueue 的名字可以看出，这个队列使用链表作为其数据结构。作为一个链表，自然需要定义
     * 有关链表内的节点，在 ConcurrentLinkedQueue 中，定义的节点 Node 核心如下：
     *
     *     private static class Node<E> {
     *         volatile E item;
     *         volatile Node<E> next;
     *
     * 其中字段 item 表示目标元素。比如，当列表中存放 String 时，item 就是 String 类型。字段 next 表示当前
     * 节点的下一个节点，这样，每个 Node 就能环环相扣，串在一起了。
     *
     * 对 Node 进行操作时，使用了 CAS 操作。
     *
     *         boolean casItem(E cmp, E val) {
     *             return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
     *         }
     *
     *         void lazySetNext(Node<E> val) {
     *             UNSAFE.putOrderedObject(this, nextOffset, val);
     *         }
     *
     *         boolean casNext(Node<E> cmp, Node<E> val) {
     *             return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
     *         }
     *
     * 方法 casItem() 表示设置当前 Node 的 item 值。它需要两个参数，第一个参数为期望值 cmp，第二个参数为设置
     * 目标值 val。当当前值等于 cmp 时，就会将目标设置为 val。同样，方法 casNext() 也是类似的，只不过它是用来
     * 设置 next 字段，而不是 item 字段。
     *
     * ConcurrentLinkedQueue 内部有两个非常重要的字段：head 和 tail，分别表示链表的头部和尾部，它们都是 Node
     * 类型。对于 head 来说，它永远不会为 null，并且通过 head 以及 succ() 后继方法一定能完整地遍历整个队列，对
     * 于 tail 来说，它自然是表示队列的末尾。
     *
     * ConcurrentLinkedQueue 的内部实现非常复杂，它允许运行时链表处于多个不同的状态。以 tail 为例，一般来说，
     * 总是期望 tail 为链表的末尾，但实际上上，tail 的更新并不是及时的，而是可能产生拖延现象。从代码可以看出，在
     * 添加元素时，tail 的更新会产生滞后，并且每次更新会跳跃两个元素。如下是向队列中添加元素的 offer() 方法。
     *
     *     public boolean offer(E e) {
     *         checkNotNull(e);
     *         final Node<E> newNode = new Node<E>(e);
     *
     *         for (Node<E> t = tail, p = t;;) {
     *             Node<E> q = p.next;
     *             if (q == null) {
     *                 // p is last node
     *                 if (p.casNext(null, newNode)) {
     *                     // Successful CAS is the linearization point
     *                     // for e to become an element of this queue,
     *                     // and for newNode to become "live".
     *                     if (p != t) // hop two nodes at a time
     *                         casTail(t, newNode);  // Failure is OK.
     *                     return true;
     *                 }
     *                 // Lost CAS race to another thread; re-read next
     *             }
     *             else if (p == q)
     *                 // We have fallen off list.  If tail is unchanged, it
     *                 // will also be off-list, in which case we need to
     *                 // jump to head, from which all live nodes are always
     *                 // reachable.  Else the new tail is a better bet.
     *                 p = (t != (t = tail)) ? t : head;
     *             else
     *                 // Check for tail updates after two hops.
     *                 p = (p != t && t != (t = tail)) ? t : q;
     *         }
     *     }
     *
     * 首先值得注意的是，这个方法没有任何锁操作。线程安全完全由 CAS 操作和队列的算法来保证。整个方法的核心是 for
     * 循环，这个循环没有出口，直到尝试成功，这也符合 CAS 操作的流程。当第一次加入元素时，由于队列为空，因此，p.next
     * 为 null，程序进入 q == null 分支，并将 p 的 next 节点赋值为 newNode，也就是将新元素加入到队列中，此时
     * p == t 成立，因此不会执行 casTail() 来更新 tail 末尾。如果 casNext() 成功，程序直接返回，如果失败，则
     * 再进行一次循环，直到成功。因此，增加一个元素后，tail 并不会更新。
     *
     * 当程序试图增加第 2 个元素时，由于 t 还在 head 的位置上（初始时，head = tail），因此，p.next 指向实际的
     * 第一个元素，因此，程序会进入分支 q != null && p != q 的分支，循环开始查找最后一个节点。首先会执行到这一
     * 行代码：p = (p != t && t != (t = tail)) ? t : q，表示取下一个节点或者最后一个节点。显然，p 会等于 q，
     * 即 取下一个节点。此时，p 执行实际的第一个元素，而它的 next 的为 null，所以在进入下一个循环时，会进入 q == null
     * 分支，p 会更新自己的 next，让它指向新加入的节点。如果成功，由于此时 p != t 成立，所以会执行 casTail()，
     * 更新 t 所在的位置，将 t 移动到链表最后。
     *
     * 这样，就只剩下 p == q 的情况。这种情况是由于遇到了哨兵（sentinel）节点导致的。所谓哨兵节点，就是 next 指向
     * 自己的节点。这种节点在队列中的存在价值不大，主要表示要删除的节点，或者空节点。当遇到哨兵节点时，由于无法通过
     * next 取得后续的节点，因此很可能直接返回 head，期望通过从链表头部开始遍历，进一步查找到链表末尾。但一旦在执行
     * 过程中，发生了 tail 被其他线程修改的情况，则进行一次 "打赌"，使用新的 tail 作为链表末尾（这样就比避免了重新
     * 查找 tail 的开销）。
     *
     * 但是这个分支里的代码却可能让人产生疑惑，如下所示：
     *
     * p = (t != (t = tail)) ? t : head
     *
     * 这句代码虽然只有短短一行，但是包含的信息比较多，首先 != 并不是原子操作，它是可以被中断的，这就是说，在执行 !=
     * 时，程序会先取得 t 的值，再执行 t = tail，并取得新的 t 的值，然后比较这两个值是否相等，在单线程环境中，t != t
     * 这种语句显然不会成立。但是在并发环境中，有可能在获得左边的值后，右边的 t 值被其他线程修改。这样 t != t 就可能
     * 成立。如果在比较的过程中，tail 被其他线程修改，当它再次赋值给 t 时，就会导致等式左边的 t 和右边的 t 不同。如
     * 果两个 t 不相同，表示 tail 在中途被其他线程篡改。这时，就可以使用新的 tail 作为链表末尾。
     *
     * 即 tail 没有被修改，就返回 head，并从头部开始，重新查找尾部；tail 被修改，直接使用 tail。
     *
     * 作为简化问题，可以来考察一些 t != t 的字节码（注意这里假设 t 为静态整形变量）：
     *
     * getstatic    #10         // Field t:I
     * getstatic    #10         // Field t:I
     * if_icmpeq    24
     *
     * 可以看到，在字节码层面，t 被先后取了两次，在多线程环境下，自然无法保证两次对 t 的取值会是相同的。
     *
     * 下面来看看哨兵节点是如何产生的，执行如下代码：
     *
     *         ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
     *         queue.offer("1");
     *         queue.poll();
     *
     * 其中 poll() 方法表示弹出队列头部的元素，源码如下：
     *
     *     public E poll() {
     *         restartFromHead:
     *         for (;;) {
     *             for (Node<E> h = head, p = h, q;;) {
     *                 E item = p.item;
     *
     *                 if (item != null && p.casItem(item, null)) {
     *                     // Successful CAS is the linearization point
     *                     // for item to be removed from this queue.
     *                     if (p != h) // hop two nodes at a time
     *                         updateHead(h, ((q = p.next) != null) ? q : p);
     *                     return item;
     *                 }
     *                 else if ((q = p.next) == null) {
     *                     updateHead(h, p);
     *                     return null;
     *                 }
     *                 else if (p == q)
     *                     continue restartFromHead;
     *                 else
     *                     p = q;
     *             }
     *         }
     *     }
     *
     * 由于队列中只有一个元素，根据上文描述，此时 tail 并没有更新，而是指向和 head 相同的位置。而此时，head 本身的
     * item 为 null，其 next 为实际的第一个元素。故在第一个循环中，代码直接进入最后一个 else 分支，将 p 赋值为 q，
     * 而 q 就是 p.next，也就是当前列表中的第一个元素。接着，在第二个循环中，p 的 item 显然不为 null（为字符串 1）。
     * 因此，如果 casItem() 操作成功，代码就会进入 item != null && p.casItem(item, null) 分支，这也意味着 p
     * 的 item 被设置为 null 了（这是弹出元素，自然需要删除）。此时 p != h 也是成立的，所以要执行 updateHead()，
     * 其实现如下：
     *
     *     final void updateHead(Node<E> h, Node<E> p) {
     *         if (h != p && casHead(h, p))
     *             h.lazySetNext(h);
     *     }
     *
     * 可以看到，在 updateHead() 中，就通过 casHead() 将 p 作为新的链表头部，而原有的 head 就通过 lazySetNext()
     * 被设置为哨兵。
     *
     * 这样，一个哨兵节点就产生了，而由于此时原有的 head 和 tail 实际上是同一个元素。因此，再次 offer() 添加元素时，
     * 就会遇到这个 tail，也就是哨兵。这就是 offer() 方法中，判断 p == q 的意义。
     *
     * 综上，可以明显感觉到，不使用锁而单纯地使用 CAS 操作会要求在应用层面保证线程安全，并处理一些可能存在的不一致问题，
     * 大大增加了程序设计和实现的难度。但是它带来的好处就是可以得到性能的飞速提升。因此，在有些场合也是值得的。
     *
     * PS：这里对哨兵节点的定义，略微有些局限，广义上的哨兵节点是指哑元节点（dummy node），在程序中加入它可以简化边界
     * 条件，从而防止对特殊条件的判断，使代码更为简便优雅，在链表中应用最为典型。一般将哑元节点作为第一个节点，或最后一
     * 个节点，但是它其实并不存储任何东西，只是为了操作的方便而引入的。（哑元节点，也称 哑节点 或 哑对象）
     */
    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("1");
        queue.poll();
    }

}

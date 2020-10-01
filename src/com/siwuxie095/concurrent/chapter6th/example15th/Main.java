package com.siwuxie095.concurrent.chapter6th.example15th;

/**
 * @author Jiajing Li
 * @date 2020-10-01 17:40:55
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 读写锁的改进：StampedLock
     *
     * StampedLock 是 Java 8 中引入的一种新的锁机制。简单的理解，可以认为它是读写锁的一个改进版本。读写锁虽然分离
     * 了读和写的功能，使得读与读之间可以完全并发。但是读和写之间依然是冲突的。读锁会完全阻塞写锁，它使用的依然是悲观
     * 的锁策略，如果有大量的读线程，它也有可能引起写线程的 "饥饿"。
     *
     * 而 StampedLock 则提供了一种乐观的读策略。这种乐观的读非常类似于无锁的操作，使得读线程完全不会阻塞写线程。
     *
     * 注意：StampedLock 是不可重入的，而 ReentrantReadWriteLock 则是可重入的。
     *
     *
     *
     * StampedLock 使用示例
     *
     * 以 Point 为例，定义了一个点 Point 类，内部有两个元素 x 和 y，表示点的坐标。同时也定义了 StampedLock 锁。
     *
     * 其中，distanceFromOrigin() 方法是一个只读方法，它只会读取 Point 的 x 和 y 坐标。在读取时，首先使用了
     * StampedLock.tryOptimisticRead() 方法。这个方法表示尝试一次乐观读。它会返回一个类似于时间戳的邮戳整数
     * stamp。这个 stamp 就可以作为这一次锁获取的凭证。
     *
     * 接着，读取 x 和 y 的值。当然，此时并不确定这个 x 和 y 是否一致（在读取 x 的时候，可能其他线程改写了 y 的值，
     * 使得 currentX 和 currentY 处于不一致的状态），因此就必须使用 validate() 方法判断这个 stamp 是否在读过程
     * 中被修改过。如果 stamp 没有被修改过，则认为这次读取是有效的，就可以跳转到 return 语句进行数据处理。反之，如
     * 果 stamp 是不可用的，则意味着在读取过程中，可能被其他线程改写了数据，所以有可能出现了脏读。如果出现这种情况，
     * 可以像处理 CAS 一样在一个死循环中一直使用乐观读，直到成功为止。
     *
     * 也可以升级锁的级别。本例中选择升级锁的级别，将乐观锁变为悲观锁。当判断乐观读失败后，使用 readLock() 获得悲观
     * 的读锁，并进一步读取数据。如果当前对象正在被修改，则读锁的申请可能导致线程挂起。
     *
     * 而另一个 move() 方法则是写入的情况，使用 writeLock() 方法可以申请写锁。这里的含义和读写锁是类似的。
     *
     * 在退出临界区时，不要忘记释放读锁和写锁。
     *
     * 可以看到，StampedLock 通过引入乐观读来增加系统的并行度。
     *
     *
     *
     * StampedLock 的小陷阱
     *
     * StampedLock 的内部实现，使用了类似于 CAS 操作的死循环反复尝试的策略。在它挂起线程时，使用的是 Unsafe.park()
     * 方法，而 park() 方法在遇到线程中断时，会直接返回（注意，不同于 Thread.sleep()，它不会抛出异常）。而在
     * StampedLock 的死循环逻辑中，没有处理有关中断的逻辑。因此，这就会导致阻塞在 park() 上的线程被中断后，会再次
     * 进入循环。而当退出条件得不到满足时，就会发生疯狂占用 CPU 的情况。这一点需要注意。
     *
     * 以 StampedLockCpuDemo 为例，演示了这个问题。其中先开启线程占用写锁，注意，为了演示效果，这里进行长时间等待
     * （等待 600 秒），先不释放锁。接着，开启 3 个读线程，让它们请求读锁。此时，由于写锁的存在，所有读线程最终都会
     * 被挂起。
     *
     * 下面是其中一个读线程在挂起时的信息：
     *
     * "Thread-3@681" prio=5 tid=0x10 nid=NA waiting
     *   java.lang.Thread.State: WAITING
     * 	  at sun.misc.Unsafe.park(Unsafe.java:-1)
     * 	  at java.util.concurrent.locks.StampedLock.acquireRead(StampedLock.java:1286)
     * 	  at java.util.concurrent.locks.StampedLock.readLock(StampedLock.java:428)
     * 	  at com.siwuxie095.concurrent.chapter6th.example15th.StampedLockCpuDemo$HoldCpuReadThread.run(StampedLockCpuDemo.java:21)
     * 	  at java.lang.Thread.run(Thread.java:748)
     *
     * 可以看到，这个线程因为 park() 操作而进入了等待状态，这种状况是正常的。
     *
     * 而在 10 秒后，系统中断了这 3 个读线程，之后，CPU 占用率极有可能会飙升。这是因为中断导致 park() 方法返回，使
     * 线程再次进入运行状态，下面是同一个线程在中断后的信息：
     *
     * "Thread-3@681" prio=5 tid=0x10 nid=NA runnable
     *   java.lang.Thread.State: RUNNABLE
     * 	  at java.util.concurrent.locks.StampedLock.acquireRead(StampedLock.java:1251)
     * 	  at java.util.concurrent.locks.StampedLock.readLock(StampedLock.java:428)
     * 	  at com.siwuxie095.concurrent.chapter6th.example15th.StampedLockCpuDemo$HoldCpuReadThread.run(StampedLockCpuDemo.java:21)
     * 	  at java.lang.Thread.run(Thread.java:748)
     *
     * 此时，这个线程状态是 RUNNABLE。显然，这是不愿意看到的，因为它会一直存在，并耗尽 CPU 资源，直到自己抢占到了锁。
     *
     *
     *
     * 有关 StampedLock 的实现思想
     *
     * StampedLock 的内部实现是基于 CLH 锁的。CLH 锁是一种自旋锁，它保证没有饥饿发生，并且可以保证 FIFO 的服务顺序。
     *
     * CLH 锁的基本思想如下：锁维护一个等待线程队列，所有申请锁，但是没有成功的线程都记录在这个队列中。每一个节点（一个
     * 线程代表一个线程），保存一个标记位（locked），用于判断当前线程是否已经释放锁。
     *
     * PS：CLH 锁实际上是一个阶段公平锁（Phase-Fair Lock）。
     *
     * 当一个线程试图获得锁时，取得当前等待队列的尾部节点作为其前序节点，并使用类似如下代码判断前序节点是否已经成功释放
     * 锁：
     *
     * while(pred.locked) {
     *
     * }
     *
     * 只要前序节点（pred）没有释放锁，则表示当前线程还不能继续执行，因此会自旋等待。
     *
     * 反之，如果前序线程已经释放锁，则当前线程可以继续执行。
     *
     * 释放锁时，也遵循这个逻辑，线程会将自身节点的 locked 位置标记为 false，那么后续等待的线程就能继续执行了。
     *
     * StampedLock 正是基于这种思想，但是实现上更为复杂。在 StampedLock 内部，会维护一个等待链表队列：
     *
     *     // Wait nodes
     *     static final class WNode {
     *         volatile WNode prev;
     *         volatile WNode next;
     *         volatile WNode cowait;    // list of linked readers
     *         volatile Thread thread;   // non-null while possibly parked
     *         volatile int status;      // 0, WAITING, or CANCELLED
     *         final int mode;           // RMODE or WMODE
     *         WNode(int m, WNode p) { mode = m; prev = p; }
     *     }
     *
     *     // Head of CLH queue
     *     private transient volatile WNode whead;
     *     // Tail (last) of CLH queue
     *     private transient volatile WNode wtail;
     *
     * 上述代码中，WNode 为链表的基本元素，每一个 WNode 表示一个等待线程。字段 whead 和 wtail 分别指向等待链表的
     * 头部和尾部。
     *
     * 另外一个重要的字段为 state：
     *
     *     private transient volatile long state;
     *
     * 字段 state 表示当前锁的状态。数据类型为 long 型，有 64 位，其中倒数第 8 位表示写锁状态，如果该位为 1，表示
     * 当前由写锁占用。
     *
     * 对于一次乐观读的操作，它会执行如下操作：
     *
     *     private static final int LG_READERS = 7;
     *     private static final long WBIT  = 1L << LG_READERS;
     *
     *     public long tryOptimisticRead() {
     *         long s;
     *         return (((s = state) & WBIT) == 0L) ? (s & SBITS) : 0L;
     *     }
     *
     * 一次成功的乐观读必须保证当前锁没有写锁占用。其中 WBIT 用来获取写锁状态位，值为 0X80。如果成功，则返回当前
     * state 的值（末尾 7 位清零，末尾 7 位表示当前正在读取的线程数量）。
     *
     * 如果在乐观读后，有线程申请了写锁，那么 state 的状态就会改变：
     *
     *     public long writeLock() {
     *         long s, next;  // bypass acquireWrite in fully unlocked case only
     *         return ((((s = state) & ABITS) == 0L &&
     *                  U.compareAndSwapLong(this, STATE, s, next = s + WBIT)) ?
     *                 next : acquireWrite(false, 0L));
     *     }
     *
     * 在执行 compareAndSwapLong() 时，通过加上 WBIT（0X80），设置写锁位为 1。这样，就会改变 state 的取值。
     * 那么在乐观读确认（validate）时，就会发现这个改动，而导致乐观读失效。
     *
     *     private static final long RBITS = WBIT - 1L;
     *     private static final long SBITS = ~RBITS;
     *
     *     public boolean validate(long stamp) {
     *         U.loadFence();
     *         return (stamp & SBITS) == (state & SBITS);
     *     }
     *
     * 上述 validate() 方法会比较当前 stamp 和发生乐观读时取得的 stamp，如果不一致，则宣告乐观读失败。
     *
     * 乐观读失败后，则可以升级锁级别，使用悲观读锁。
     *
     *     private static final long ABITS = RBITS | WBIT;
     *     private static final long RFULL = RBITS - 1L;
     *     private static final long STATE;
     *     private static final long RUNIT = 1L;
     *
     *     public long readLock() {
     *         long s = state, next;  // bypass acquireRead on common uncontended case
     *         return ((whead == wtail && (s & ABITS) < RFULL &&
     *                  U.compareAndSwapLong(this, STATE, s, next = s + RUNIT)) ?
     *                 next : acquireRead(false, 0L));
     *     }
     *
     * 悲观读会先尝试设置 state 状态，它会将 state 加 1（前提是读线程数量没有溢出，对于读线程数量溢出的情况，会
     * 使用辅助的 readerOverflow 进行统计，这里不多赘述），用于统计读线程的数量。如果失败，则进入 acquireRead()
     * 二次尝试获取锁。
     *
     * 在 acquireRead() 中，线程会在不同条件下进行若干次自旋，试图通过 CAS 操作获得锁。如果自旋宣告失败，则会启用
     * CLH 队列，将自己加到队列中。之后再进行自旋，如果发现自己成功获得了读锁，则会进一步把自己 cowait 队列中的读线
     * 程全部激活（使用 Unsafe.unpark() 方法）。
     *
     * 如果最终依然无法成功获得读锁，则会使用 Unsafe.park() 方法挂起当前线程。
     *
     * 方法 acquireWrite() 和 acquireRead() 非常类似，也是通过自旋尝试、加入等待队列、直至最终 Unsafe.park()
     * 挂起线程的逻辑进行的。释放锁时，与加锁动作相反，以 unlockWrite() 为例：
     *
     *     private static final long ORIGIN = WBIT << 1;
     *
     *     public void unlockWrite(long stamp) {
     *         WNode h;
     *         if (state != stamp || (stamp & WBIT) == 0L)
     *             throw new IllegalMonitorStateException();
     *         state = (stamp += WBIT) == 0L ? ORIGIN : stamp;
     *         if ((h = whead) != null && h.status != 0)
     *             release(h);
     *     }
     *
     * 先将写标记位清零，如果 state 发生溢出，则退回初始值。
     *
     * 接着，如果等待队列不为空，则从等待队列中激活一个线程（绝大部分情况下是第一个等待线程）继续执行。
     */
    public static void main(String[] args) {

    }

}

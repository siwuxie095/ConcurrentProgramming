package com.siwuxie095.concurrent.chapter4th.example17th;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jiajing Li
 * @date 2020-09-13 18:40:39
 */
public class Main {

    /**
     * 无锁的线程安全整数：AtomicInteger
     *
     * 为了让程序员能够受益于 CAS 等 CPU 指令，JDK 并发包中有一个 atomic 包，里面实现了一些直接使用 CAS 操作
     * 的线程安全的类型（也称原子类）。
     *
     * 其中最常用的一个类，应该就是 AtomicInteger，可以把它看作是一个整数。但是与 Integer 不同，它是可变的，
     * 并且是线程安全的。对其进行修改等任何操作，都是通过 CAS 指令进行的。
     *
     * 这里简单列举一下 AtomicInteger 的主要方法，对于其他原子类，操作也是非常类似的：
     *
     * public final int get()                                           // 取得当前值
     * public final void set(int newValue)                              // 设置当前值
     * public final int getAndSet(int newValue)                         // 设置新值，并返回旧值
     * public final boolean compareAndSet(int expect, int update)       // 如果当前值为 expect，则设置为 update
     * public final int getAndIncrement()                               // 当前值加 1，返回旧值
     * public final int getAndDecrement()                               // 当前值减 1，返回旧值
     * public final int getAndAdd(int delta)                            // 当前值增加 delta，返回旧值
     * public final int incrementAndGet()                               // 当前值加 1，返回新值
     * public final int decrementAndGet()                               // 当前值减 1，返回新值
     * public final int addAndGet(int delta)                            // 当前值增加 delta，返回新值
     *
     * 就内部实现来说，AtomicInteger 中保存一个核心字段：
     *
     * private volatile int value;
     *
     * 它就代表了 AtomicInteger 的当前实际取值。此外还有一个：
     *
     * private static final long valueOffset;
     *
     * 它保存着 value 字段在 AtomicInteger 对象在内存中的偏移量。这个偏移量是实现 AtomicInteger 的关键。如下
     * 是 valueOffset 的初始化过程：
     *
     *     static {
     *         try {
     *             valueOffset = unsafe.objectFieldOffset
     *                 (AtomicInteger.class.getDeclaredField("value"));
     *         } catch (Exception ex) { throw new Error(ex); }
     *     }
     *
     * unsafe 变量的类型是 sun.misc.Unsafe，它是 JDK 内部使用的工具类，主要实现了平台相关的操作。它通过暴露一
     * 些 Java 意义上说 "不安全" 的功能给 Java 层代码，来让 JDK 能够更多的使用 Java 代码来实现一些原本是平台相
     * 关的、需要使用 native 语言（例如 C 或 C++）才可以实现的功能。该类不应该在 JDK 核心类库之外使用。总而言之，
     * 这段代码主要是为了获取 value 在堆内存中的偏移量。AtomicInteger 的原子操作都是靠内存偏移量来实现的。
     *
     * 以 AtomicIntegerDemo 为例，其中使用了 incrementAndGet() 方法，这会使用 CAS 操作将自己加 1，同时返回
     * 新值（这里忽略了）。运行代码，会看到程序输出了 100_000。这说明程序正常执行，没有错误。如果不是线程安全，i
     * 的值应该会小于 100_000 才对。
     *
     * 使用 AtomicInteger 会比使用锁具有更好的性能（这里不做具体的对比）。下面来关注一下 incrementAndGet()
     * 的内部实现（基于 JDK 7 分析，JDK 7 和 JDK 8 的实现有所不同）：
     *
     *     public final int incrementAndGet() {
     *         for(;;) {
     *             int current = get();
     *             int next = current + 1;
     *             if(compareAndSet(current, next))
     *                  return next;
     *         }
     *     }
     *
     * 其中 get() 方法非常简单，就是返回内部数据 value。
     *
     *     public final int get() {
     *         return value;
     *     }
     *
     * 这里让人印象深刻的就是其中的 for 循环，咋一看可能会觉得很奇怪，为什么连设置值那么简单的一个操作都需要一个
     * 死循环呢？原因就是 CAS 操作未必是成功的，因此对于不成功的情况，就需要进行不断的尝试。先是通过 get() 取得
     * 期望值，接着加 1 后得到新值 next。这样就得到了 CAS 操作必须的两个参数：期望值和新值。使用 compareAndSet()
     * 方法将新值 next 写入，成功的条件是在写入的时刻，当前值应该要等于刚刚取得的期望值 current。如果不是这样，
     * 就说明 AtomicInteger 的值在写入之前，又被其他线程修改过了。所以当前线程看到的就是一个过期状态，再去写入
     * 新值就会失败，即 compareAndSet() 返回失败，需要进行下一次重试，直到成功。
     *
     * 以上就是 CAS 操作的基本思想。无论程序多么复杂，其基本原理总是不变的。
     *
     * 和 Atomic 类似的类还有 AtomicLong 用来代表 long 型，AtomicBoolean 用来表示 boolean 型，AtomicReference
     * 用来表示对象引用。
     *
     * PS：其实 incrementAndGet() 方法在 JDK 8 中虽然实现方式不同，但也是比较好理解的
     *
     *     public final int incrementAndGet() {
     *         return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
     *     }
     *
     * 其中 unsafe.getAndAddInt(this, valueOffset, 1) 实际上就已经做了加 1 操作，只是它返回的是旧值，所以
     * 需要再做加 1 才能在外层返回新值。下面是 getAndAddInt() 的反编译代码（无法获得源码）：
     *
     *     public final int getAndAddInt(Object var1, long var2, int var4) {
     *         int var5;
     *         do {
     *             var5 = this.getIntVolatile(var1, var2);
     *         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
     *
     *         return var5;
     *     }
     *
     * 其中：
     * （1）var1 即 value 当前值（旧值）所代表的对象，这里是 AtomicInteger 对象。
     * （2）var2 即 valueOffset 内存偏移量。
     * （3）var4 即 增加的值，这里是 1。
     * （4）var5 即 期望值。
     * （5）var5 + var4 即 新值。
     *
     * getIntVolatile() 即是从内存中根据 valueOffset 内存偏移量取出数据，然后通过 compareAndSwapInt() 将
     * 期望值和内存偏移量处的当前值进行比较，如果一样，就修改为新值。
     */
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
    }

}

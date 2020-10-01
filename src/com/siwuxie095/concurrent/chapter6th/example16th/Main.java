package com.siwuxie095.concurrent.chapter6th.example16th;

/**
 * @author Jiajing Li
 * @date 2020-10-01 21:15:30
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 原子类的增强
     *
     * 无锁的原子类操作使用系统的 CAS 指令，有着远远超越锁的性能。那是否有可能在性能上更上一层楼呢？答案是肯定的。
     * 在 Java 8 中引入了 LongAdder 类，这个类也在 java.util.concurrent.atomic 包下，因此，可以推测，它也
     * 是使用了 CAS 指令。
     *
     *
     *
     * 更快的原子类：LongAdder
     *
     * AtomicInteger 等原子类的基本实现机制就是在一个死循环内，不断尝试修改目标值，直到修改成功。如果竞争不激烈，
     * 那么修改成功的概率就很高，否则，修改失败的概率就很高。在大量修改失败时，这些原子操作就会进行更多次的循环尝试，
     * 因此性能就会受到影响。
     *
     * 那么当竞争激烈的时候，应该如何进一步提高系统的性能呢？一种基本方案就是可以使用热点分离，将竞争的数据进行分解。
     * 基于这个思路，应该就可以想到一种对传统的 AtomicInteger 等原子类的改进方法。虽然在 CAS 操作中没有锁，但是
     * 像减小锁粒度这种分离热点的思想依然可以使用。一种可行的方案就是仿造 ConcurrentHashMap，将热点数据分离。比
     * 如，可以将 AtomicInteger 的内部核心数据 value 分离成一个数组，每个线程访问时，通过哈希等算法映射到其中一
     * 个数字进行计数，而最终的计数结果，则为这个数组的求和累加。其中，热点数据 value 被分离在多个单元 cell，每个
     * cell 独自维护内部的值，当前对象的实际值由所有 cell 累计合成，这样，热点就进行了有效的分离，提高了并行度。
     * LongAdder 正是使用了这种思想。
     *
     * 在实际的操作中，LongAdder 并不会一开始就动用数组进行处理，而是将所有数据都先记录在一个称为 base 的变量中。
     * 如果在多线程条件下，修改 base 都没有冲突，那么也没有必要扩展为 cell 数组。但是，一旦 base 修改发生冲突，
     * 就会初始化 cell 数组，使用新的策略。如果使用 cell 数组更新后，发现在某一个 cell 上的更新依然发生冲突，那
     * 么系统就会尝试创建新的 cell，或者将 cell 的数量加倍，以减少冲突的可能。
     *
     * 下面简单分析一下 increment() 方法（将 LongAdder 自增 1）的内部实现：
     *
     *     public void increment() {
     *         add(1L);
     *     }
     *
     *     public void add(long x) {
     *         Cell[] as; long b, v; int m; Cell a;
     *         if ((as = cells) != null || !casBase(b = base, b + x)) {
     *             boolean uncontended = true;
     *             if (as == null || (m = as.length - 1) < 0 ||
     *                 (a = as[getProbe() & m]) == null ||
     *                 !(uncontended = a.cas(v = a.value, v + x)))
     *                 longAccumulate(x, null, uncontended);
     *         }
     *     }
     *
     * increment() 方法会调用 add() 方法。最开始 cells 为 null，所以数据会向 base 增加。但如果对 base 的操作
     * 冲突，则会将冲突标记 uncontended 设为 true。接着，如果判断 cells 数组不可用，或者当前线程对应的 cell 为
     * null，则直接进入 longAccumulate() 方法，该方法主要是根据需要创建新的 cell 或者对 cell 数组进行扩容，以
     * 减少冲突。
     *
     * 下面以 LongAdderDemo 为例简单对 LongAdder、原子类以及同步锁进行性能测试。测试方法是使用多个线程对同一个
     * 整数进行累加，观察使用三种不同方法时所消耗的时间。
     *
     * 其中定义了一些辅助变量，指定了测试线程数量、目标总数以及三个初始值为 0 的整型变量，它们分别使用 LongAdder、
     * AtomicLong 和锁同步的操作对象。
     *
     * 注意，由于 LongAdder 中将单个数值分解为多个不同的段，所以在进行累加后，increment() 方法并不能返回当前的
     * 数值，要取得当前的实际值，需要使用 sum() 方法重新计算。这个计算是需要有额外成本的，但即使加上这个额外成本，
     * LongAdder 的表现还是比 AtomicLong 好。
     *
     * 运行代码，结果如下：
     *
     * LongAdderThread spend: 234 ms, value: 10000001
     * LongAdderThread spend: 234 ms, value: 10000001
     * LongAdderThread spend: 234 ms, value: 10000000
     * AtomicLongThread spend: 181 ms, value: 10000001
     * AtomicLongThread spend: 181 ms, value: 10000000
     * AtomicLongThread spend: 181 ms, value: 10000002
     * SyncThread spend: 300 ms, value: 10000000
     * SyncThread spend: 304 ms, value: 10000001
     * SyncThread spend: 305 ms, value: 10000002
     *
     * 结果略显尴尬，正常来说，LongAdder 的性能会好于 AtomicLong，但这里并没有。
     *
     * PS：但如果使用另一个示例 LongAdderAnotherDemo，依然可以说明 LongAdder 性能好于 AtomicLong。两者对比
     * 之下，说明 sum() 方法还是比较耗成本的。
     *
     * LongAdder 的另外一个优化手段是避免了伪共享。但是，需要注意的是，LongAdder 中并不是直接使用 padding 这种
     * 看起来比较碍眼的做法，而是引入了一种新的注解 @sun.misc.Contended。
     *
     * 对于 LongAdder 中的每个 Cell，定义如下：
     *
     *     @sun.misc.Contended static final class Cell {
     *         volatile long value;
     *         Cell(long x) { value = x; }
     *         final boolean cas(long cmp, long val) {
     *             return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
     *         }
     *
     *         // Unsafe mechanics
     *         private static final sun.misc.Unsafe UNSAFE;
     *         private static final long valueOffset;
     *         static {
     *             try {
     *                 UNSAFE = sun.misc.Unsafe.getUnsafe();
     *                 Class<?> ak = Cell.class;
     *                 valueOffset = UNSAFE.objectFieldOffset
     *                     (ak.getDeclaredField("value"));
     *             } catch (Exception e) {
     *                 throw new Error(e);
     *             }
     *         }
     *     }
     *
     * 可以看到，在 Cell 类上加了注解 @sun.misc.Contended。这将会使得 JVM 自动为 Cell 解决伪共享问题。
     *
     * 当然，自己的代码也也可以使用 @sun.misc.Contended 来解决伪共享问题，但是需要额外如下的使用虚拟机参数，
     * 否则，这个注解将会被忽略：
     *
     * -XX:-RestrictContended
     *
     * 在 chapter5th#example5th 中有一个伪共享的案例，如果不使用 padding 的方式（这种方式不够优雅），则
     * 可以改用该注解来实现 VolatileLong，如下：
     *
     *     @sun.misc.Contended
     *     public final static class VolatileLong {
     *
     *         private volatile long value = 0L;
     *
     *     }
     *
     *
     *
     * LongAdder 的功能增强版：LongAccumulator
     *
     * LongAccumulator 是 LongAdder 的亲兄弟，它们有公共的父类 Striped64。因此，LongAccumulator 内部的优化
     * 方式和 LongAdder 是一样的。它们都将一个 long 型整数进行分割，存储在不同的变量中，以防止多线程竞争。两者的
     * 主要逻辑是类似的，但是 LongAccumulator 是 LongAdder 的功能扩展，对于 LongAdder 来说，它只是每次对给定
     * 的整数执行一次加法，而 LongAccumulator 则可以实现任意函数操作。
     *
     * 可以使用下面的构造方法创建一个 LongAccumulator 实例：
     *
     * public LongAccumulator(LongBinaryOperator accumulatorFunction, long identity)
     *
     * 第一个参数 accumulatorFunction 就是需要执行的二元函数式接口（接收两个 long 型参数并返回 long），第二个
     * 参数是初始值。
     *
     * 以 LongAccumulatorDemo 为例，展示了 LongAccumulator 的使用。它通过多线程访问若干个整数，并返回遇到的
     * 最大的那个数字。
     *
     * 先是构造了 LongAccumulator 实例。因为要过滤最大值，所以传入 Long::max 方法引用。当有数据通过 accumulate()
     * 方法传入 LongAccumulator 后，LongAccumulator 会通过 Long::max 识别最大值并且保存在内部（可能是 base，
     * 也可能是 cell 数组）。然后通过 longValue() 方法对所有的 cell 进行 Long::max 操作，得到最大值。
     */
    public static void main(String[] args) {

    }

}

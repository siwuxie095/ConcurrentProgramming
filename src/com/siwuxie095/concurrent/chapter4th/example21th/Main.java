package com.siwuxie095.concurrent.chapter4th.example21th;

/**
 * @author Jiajing Li
 * @date 2020-09-14 21:54:08
 */
public class Main {

    /**
     * 数组也能无锁：AtomicIntegerArray
     *
     * 除了提供基本数据类型的原子类外，JDK 还提供了数组等复合结构的原子类。当前可用的原子数组有：AtomicIntegerArray、
     * AtomicLongArray、AtomicReferenceArray，分别表示 int 型数组、long 型数组和普通的对象数组。
     *
     * 这里以 AtomicIntegerArray 为例，展示原子数组的使用方式。
     *
     * AtomicIntegerArray 本质上是对 int[] 类型的封装，使用 Unsafe 类通过 CAS 操作控制 int[] 在多线程下的安全性。
     * 它提供了以下几个核心 API：
     *
     * // 获得数组第 i 个下标的元素
     * public final int get(int i)
     * //获得数组的长度
     * public final int length()
     * // 将数组第 i 个下标设置为新值，并返回旧值
     * public final int getAndSet(int i, int newValue)
     * // 进行 CAS 操作，如果第 i 个下标的元素等于预期值，则设置为新值，设置成功返回 true
     * public final boolean compareAndSet(int i, int expect, int update)
     * // 将第 i 个下标的元素加 1，并返回旧值
     * public final int getAndIncrement(int i)
     * // 将第 i 个下标的元素减 1，并返回旧值
     * public final int getAndDecrement(int i)
     * // 将第 i 个下标的元素增加 delta（delta 可以是负数），并返回旧值
     * public final int getAndAdd(int i, int delta)
     * // 将第 i 个下标的元素加 1，并返回新值
     * public final int incrementAndGet(int i)
     * // 将第 i 个下标的元素减 1，并返回新值
     * public final int decrementAndGet(int i)
     * // 将第 i 个下标的元素增加 delta（delta 可以是负数），并返回新值
     * public final int addAndGet(int i, int delta)
     *
     * 以 AtomicIntegerArrayDemo 为例，展示 AtomicIntegerArray 的使用。先声明了一个内含 10 个元素的数组，然后开启
     * 10 个线程，对这 10 个元素进行累加，每个元素各累加 1_000 次。所以可以预测，如果线程安全，数组内的 10 个元素必然都
     * 是 10_000。反之，如果线程不安全，则部分或者全部元素会小于 10_000。
     *
     * 运行代码，输出如下：
     *
     * [10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000]
     *
     * 这说明 AtomicIntegerArray 确实保证了数组的线程安全性。
     */
    public static void main(String[] args) {

    }

}

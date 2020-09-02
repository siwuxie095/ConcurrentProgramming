package com.siwuxie095.concurrent.chapter2nd.example15th;

/**
 * @author Jiajing Li
 * @date 2020-09-02 21:50:15
 */
public class Main {

    /**
     * 程序中的幽灵：并发下的 ArrayList
     *
     * 众所周知，ArrayList 是一个线程不安全的容器。如果在多线程中使用 ArrayList，可能会导致程序出错。那究竟
     * 可能引起哪些问题呢？以 ArrayListMultiThread 为例，t1 和 t2 两个线程同时向一个 ArrayList容器中添加
     * 元素。它们各自添加 1_000_000 个元素，因此，期望最后可以有 2_000_000 个元素在 ArrayList 中。然而实际
     * 运行这段代码，可能会得到三种结果：
     *
     * （1）程序正常结束，ArrayList 的最终大小确实是 2_000_000。这说明即使并行程序有问题，也未必每次都表现
     * 出来。
     *
     * （2）程序抛出异常：java.lang.ArrayIndexOutOfBoundsException。这是因为 ArrayList 在扩容过程中，
     * 内部一致性被破坏，但由于没有锁的保护，另一个线程访问到了不一致的内部状态，导致出现数组越界问题。
     *
     * （3）出现了一个非常隐蔽的错误，比如打印 1000012 作为 ArrayList 的大小。显然，这是由于多线程访问冲突，
     * 使得保存容器大小的变量被多线程不正常的访问，即 两个线程也同时对 ArrayList 中同一个位置进程赋值导致的。
     * 如果出现这种问题，那么很不幸，就得到了一个没有错误提示的错误。并且，这个错误未必是可以复现的。
     *
     * PS: 正常情况下，只会得到第（2）种结果。（1）和（3）只是一种可能。
     *
     * 改进：改进的方法很简单，使用线程安全的 Vector 代替 ArrayList 即可。
     */
    public static void main(String[] args) {

    }

}

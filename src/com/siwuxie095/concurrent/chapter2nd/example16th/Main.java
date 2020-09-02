package com.siwuxie095.concurrent.chapter2nd.example16th;

/**
 * @author Jiajing Li
 * @date 2020-09-02 22:16:34
 */
public class Main {

    /**
     * 程序中的幽灵：并发下诡异的 HashMap
     *
     * 众所周知，HashMap 同样不是线程安全的。当你使用多线程访问 HashMap 时，也可能会遇到意想不到的错误。
     * 不过和 ArrayList 不同，HashMap 的问题似乎更加诡异。
     *
     * 以 HashMapMultiThread 为例，t1 和 t2 两个线程同时对 HashMap 进行 put() 操作。如果一切正常，
     * 期望得到的 map.size() 就是 100_000。但实际上，可能会得到以下四种情况（以 Java 8 为参照）：
     *
     * （1）程序正常结束，并且结果也是符合预期的。HashMap 的大小为 100_000。
     *
     * （2）程序抛出异常：java.lang.ClassCastException: java.util.HashMap$Node cannot be cast
     * to java.util.HashMap$TreeNode。
     *
     * （3）程序正常结束，但结果不符合预期，而是一个小于 100_000 的数字，比如 98_868。
     *
     * （4）程序永远无法结束。
     *
     * 对于前三种情况，和 ArrayList 的情况非常类似，这里不过多解释。而对于第四种情况，乍一看可能会比较
     * 惊讶，看似正常的程序，为什么会结束不了呢？
     *
     * PS：（2）（3）容易复现，（4）需要多运行几次，也可以复现，（1）只是一种可能，基本不可能复现。
     *
     * 注意：请谨慎尝试该代码，由于这段代码很可能占用两个 CPU 核，并使它们的 CPU 占有率达到 100%。如果
     * CPU 性能较弱，可能导致死机。请先保存资料再进行尝试。（PS：这里本人使用的是 MBP，没有出现卡死现象。）
     *
     * 打开任务管理器（Windows）或活动监视器（macOS），会发现这段代码占用了极高的 CPU，最有可能的表示是
     * 占用了两个 CPU 核，并使得这两个核的 CPU 使用率达到 100%，这非常类似于死循环的情况。
     *
     * 通过 jstack 命令或者 IDEA 自带的 Dump Threads 功能（一个类似照相机的图标），可以看到 Java 进程
     * 的内部线程及其堆栈。不难发现，主线程 main 正出于 Waiting 状态，并且这个等待是由于 join() 方法引起
     * 的，是符合预期的。而 t1 和 t2 两个线程都出于 Runnable 状态，并且当前执行语句为 HashMap.put() 方
     * 法。
     *
     * 这个死循环的问题，一旦发生，着实很让人郁闷。值得注意的是，Java 8 虽然对 HashMap 内部实现做了大规模
     * 的调整，但是依然没有规避死循环的问题，只是和 Java 7 导致死循环的原因不一样。
     *
     * 所以在多线程环境下使用 HashMap 依然会有问题（诸如：内部数据不一致、抛异常、死循环），最简单的解决方案
     * 就是使用 ConcurrentHashMap 代替 HashMap（参见 ConcurrentHashMapMultiThread 示例）。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter4th.example14th;

/**
 * @author Jiajing Li
 * @date 2020-09-13 13:21:07
 */
public class Main {

    /**
     * ThreadLocal 对性能有何帮助
     *
     * 为每一个线程分配一个独立的对象对系统性能也许是有帮助的。当然，这也不一定，这完全取决于共享对象的内部逻辑。
     * 如果共享对象对于竞争的处理容易引起性能损失，那就还是应该考虑使用 ThreadLocal 为每一个线程分配单独的对象。
     * 一个典型的案例就是在多线程下产生随机数。
     *
     * 以 RandomDemo 为例，简单测试一下在多线程下产生随机数的性能问题。首先定义了 5 个全局变量：
     *
     * （1）第一个全局变量是每个线程要产生的随机数数量；
     * （2）第二个全局变量是参与工作的线程数量；
     * （3）第三个全局变量是线程池；
     * （4）第四个全局变量是被多线程共享的 Random 实例；
     * （5）第五个全局变量是由 ThreadLocal 封装的 Random；
     *
     * 接着，定义一个工作线程的内部逻辑，它可以工作在两种模式下：
     * （1）第一种是多线程共享一个 Random（mode = 0）；
     * （2）第二种是多线程各分配一个 Random（mode = 1）；
     *
     * 每个线程会产生若干个随机数，完成工作后，记录并返回所消耗的时间。运行代码，结果如下：
     *
     * pool-1-thread-4 spend 2796 ms
     * pool-1-thread-3 spend 2841 ms
     * pool-1-thread-2 spend 2845 ms
     * pool-1-thread-1 spend 2849 ms
     * 多线程访问同一个 Random 实例：11331 ms
     * pool-1-thread-4 spend 109 ms
     * pool-1-thread-2 spend 110 ms
     * pool-1-thread-3 spend 111 ms
     * pool-1-thread-1 spend 112 ms
     * 使用 ThreadLocal 包装 Random 实例：442 ms
     *
     * 很明显，在多线程共享一个 Random 实例的情况下，总耗时达到 11331 毫秒，而在 ThreadLocal 模式下，仅耗时
     * 442 毫秒。
     */
    public static void main(String[] args) {

    }

}

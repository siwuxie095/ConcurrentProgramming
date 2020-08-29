package com.siwuxie095.concurrent.chapter2nd.example8th;

/**
 * @author Jiajing Li
 * @date 2020-08-29 07:44:56
 */
public class Main {

    /**
     * 线程的基本操作：等待线程结束(join)和谦让(yield)
     *
     * 在很多情况下，线程之间的协作和人与人之间的协作非常类似。一种非常常见的合作方式就是分工合作。以软件开发为例，
     * 先由产品做前期的需求调研和分析，再由开发者完成软件开发，然后是测试人员进行软件测试。
     *
     * 将这个关系对应到多线程应用中，很多时候，一个线程的输入可能非常依赖于另外一个或者多个线程的输出，此时，这个
     * 线程就需要等待依赖线程执行完毕，才能继续执行。JDK 提供了 join() 操作来实现这个功能。如下，显示了 2 个
     * join() 方法：
     *
     * public final void join() throws InterruptedException
     * public final synchronized void join(long millis) throws InterruptedException
     *
     * 第一个 join() 方法表示无限等待，它会一直阻塞当前线程，直到目标线程执行完毕。第二个方法给出了一个最大等待
     * 时间，如果超过给定时间目标线程还在执行，当前线程也会因为 "等不及了"，而继续往下执行。
     *
     * 英文 join 的翻译，通常是加入的意思。在这里感觉也比较贴切。因为一个线程要加入另外一个线程，那么最好的方法
     * 就是等着它一起走（看似好像串行了，实际上仍然是并行，只是在 join 点进行等待）。
     *
     * 以 SimpleJoin 为例，如果不使用 join() 等待 AddThread，那么得到的 i 很可能是 0 或者一个非常小的数字。
     * 因为 AddThread 还没开始执行，i 的值就已经被输出了。但在使用 join() 方法后，表示主线程愿意等待 AddThread
     * 执行完毕，跟着 AddThread 一起往前走，故在 join() 返回时，AddThread 已经执行完毕，故 i 总是 10_000_000。
     *
     * 实际上，join() 的本质是让调用线程 wait() 在当前线程对象实例(this)上。下面是 JDK 中 join() 实现的核心
     * 代码片段：
     *
     * while (isAlive()) {
     *     wait(0);
     * }
     *
     * 可以看到，它让调用线程（等待者）在当前线程对象（被等待者）上进行等待。被等待线程执行完成后，在退出前会调用
     * notifyAll() 通知所有的等待线程继续执行。因此，值得注意的一点是：不要在 Thread 对象实例上使用类似 wait()
     * 或者 notify() 等方法，因为这很有可能影响系统 API 的工作，或者被系统 API 所影响。
     *
     *
     * 另外一个比较有趣的方法是 Thread.yield()，它的定义如下：
     *
     * public static native void yield()
     *
     * 这是一个静态方法，一旦执行，它会使当前线程让出 CPU。但要注意，让出 CPU 并不表示当前线程就不执行了。当前
     * 线程在让出 CPU 后，还会进行 CPU 资源的争夺，但是是否能够再次被分配到，就不一定了。因此，对 Thread.yield()
     * 的调用就好像说：我已经完成一些最重要的工作了，我应该休息一下，给其他线程一些工作机会。
     *
     * 如果觉得一个线程不那么重要，或者优先级非常低，而且又害怕它会占用太多的 CPU 资源，那么可以在适当的时候调用
     * Thread.yield()，给予其他线程更多的工作机会。
     */
    public static void main(String[] args) {
    }

}

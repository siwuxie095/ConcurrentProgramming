package com.siwuxie095.concurrent.chapter3rd.example10th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 17:12:23
 */
public class Main {

    /**
     * 不要重复发明轮子：JDK 对线程池的支持
     *
     * 为了能够更好的控制多线程，JDK 提供了一套 Executor 框架，帮助开发者有效地进行线程控制，其本质
     * 就是一个线程池。它的核心成员如下：
     *
     * （1）Executor 接口（顶级接口）
     * （2）ExecutorService 接口（继承自 Executor）
     * （3）AbstractExecutorService 抽象类（实现自 ExecutorService）
     * （4）ScheduledExecutorService 接口（继承自 ExecutorService）
     * （5）ThreadPoolExecutor 类（继承自 AbstractExecutorService）（线程池）
     * （6）Executors 类（线程池工厂）
     *
     * 以上成员均在 java.util.concurrent 包中，是 JDK 并发包中的核心类，其中 ThreadPoolExecutor
     * 表示线程池，Executors 则表示线程池工厂，通过 Executors 可以取得一个拥有特定功能的线程池。
     *
     * Executor 框架提供了各种类型的线程池，主要有以下工厂方法（由 Executors 提供）：
     * public static ExecutorService newFixedThreadPool(int nThreads)
     * public static ExecutorService newSingleThreadExecutor()
     * public static ExecutorService newCachedThreadPool()
     * public static ScheduledExecutorService newSingleThreadScheduledExecutor()
     * public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
     *
     * 以上工厂方法分别返回具有不同工作特性的线程池。这些线程池工厂方法的具体说明如下：
     * （1）newFixedThreadPool() 方法：该方法返回一个固定线程数量的线程池。该线程池中的线程数量始终
     * 不变。当有一个新的任务提交时，线程池中若有空闲线程，则立即执行。若没有，则新的任务会被暂存在一个
     * 任务队列中，待有线程空闲时，便处理在任务队列中的任务。
     *
     * （2）newSingleThreadExecutor() 方法：该方法返回一个只有一个线程的线程池。若多余一个任务被提
     * 交到该线程池，任务会被保存在一个任务队列中，待线程空闲，按先入先出的顺序执行队列中的任务。
     *
     * （3）newCachedThreadPool() 方法：该方法返回一个可根据实际情况调整线程数量的线程池。线程池的
     * 线程数量不确定，但若有空闲线程可以复用，则会优先使用可复用的线程。若所有线程均在工作，又有新的任
     * 务提交，则会创建新的线程处理任务。所有线程在当前任务执行完毕后，将返回线程池进行复用。
     *
     * （4）newSingleThreadScheduledExecutor() 方法：该方法返回一个 ScheduledExecutorService
     * 对象，线程池大小为 1。ScheduledExecutorService 接口在 ExecutorService 接口之上扩展了在给
     * 定时间执行某任务的功能，如在某个固定的延时之后执行，或者周期性执行某个任务。
     *
     * （5）newScheduledThreadPool() 方法：该方法也返回一个 ScheduledExecutorService 对象，但该
     * 线程池可以指定线程数量。
     *
     *
     * 固定大小的线程池
     *
     * 以 FixedThreadPoolDemo 为例，使用 Executors.newFixedThreadPool() 方法，简单展示线程池的
     * 使用。先创建了一个固定大小的线程池，内有 5 个线程。再向该线程池提交了 10 个任务。此后，线程池就
     * 会安排调度这 10 个任务。每个任务都会将自己的执行时间和执行这个线程的 ID 打印出来，并且在这里，
     * 安排每个任务要执行 1 秒。运行一下，不难发现，前 5 个任务和后 5 个任务的执行时间正好相差 1 秒
     * （注意时间戳的单位是毫秒），并且前 5 个任务和后 5 个任务的线程 ID 也是完全一致的。这说明在这
     * 10 个任务中，是分成 2 批次执行的。这也完全符合一个只有 5 个线程的线程池的行为。
     *
     * 而如果使用 Executors.newCachedThreadPool() 生成一个不固定线程数量的线程池，那么提交 10 个
     * 任务时，会直接生成 10 个线程来执行（参加 CachedThreadPoolDemo 示例）。
     *
     *
     * 计划任务
     *
     * 以 ScheduledThreadPoolDemo 为例，使用 Executors.newScheduledThreadPool() 来进行计划任务，
     * 该方法返回一个 ScheduledExecutorService，可以根据时间需要对线程进行调度。它的一些主要方法如下：
     *
     * public ScheduledFuture<?> schedule(Runnable command,
     *                                        long delay, TimeUnit unit)
     *
     * public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
     *                                                   long initialDelay,
     *                                                   long period,
     *                                                   TimeUnit unit)
     *
     * public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
     *                                                      long initialDelay,
     *                                                      long delay,
     *                                                      TimeUnit unit)
     *
     * 与其他几个线程池不同，ScheduledExecutorService 并不一定会立即安排执行任务。它其实是起到了计划
     * 任务的作用。它会在指定的时间，对任务进行调度。
     *
     * 作为说明，这里给出了三个方法：
     * （1）schedule() 方法会在给定延迟 delay 后，对任务进行一次性调度。
     * （2）scheduleAtFixedRate() 方法是对任务进行周期性的调度，调度方式：在初始延迟 initialDelay
     * 之后开始调度，到了给定周期 period 时，检测当前任务是否执行完毕，如果当前任务执行完毕，则下一个
     * 任务立即执行，如果当前任务没有执行完毕，则需要等待当前任务执行完毕后，下一个任务立即执行。
     * （3）scheduleWithFixedDelay() 方法也是对任务进行周期性的调度，调度方式：在初始延迟 initialDelay
     * 之后开始调度，当前任务执行完毕后，等待给定延迟 delay 后，下一个任务立即执行。
     *
     * 对于 FixedRate 来说，任务调度的频率一般是一定的。它是以当前任务的开始执行时间为起点，经过 period
     * 时间，调度下一个任务；而对于 FixedDelay 来说，任务调度的频率一般是不一定的。它是以当前任务的结束
     * 执行时间为起点，经过 delay 时间，调度下一个任务。
     *
     * 在本例中，使用的是 scheduleAtFixedRate() 方法调度一个任务，这个任务会执行 1 秒，调度周期是 2 秒。
     * 也就是说，每 2 秒中，任务会被执行一次。运行代码，也可以看到时间间隔是 2 秒。
     *
     * 而如果任务的执行时间超过了调度时间，并不会出现多个任务堆叠的情况。比如，调度周期还是 2 秒，将任务的
     * 执行时间改为 8 秒，运行代码，就会发现任务的执行周期变成了 8 秒。
     *
     * 基于以上情况，如果采用 scheduleWithFixedDelay() 方法，那么任务的实际执行周期就是 10 秒。
     *
     * 另外一个值得注意的问题是，调度程序实际上并不保证任务会无限期的持续调用。如果任务本身抛出了异常，那么
     * 后续的所有执行都会被中断，因此，如果想让任务持续稳定的执行，那么做好异常处理就非常重要，否则，调度器
     * 很有可能会无疾而终。
     *
     * 注意：如果任务遇到异常，那么后续所有的任务都会停止调度，因此，必须保证异常被及时处理，为周期性任务的
     * 稳定调度提供条件。
     */
    public static void main(String[] args) {

    }

}

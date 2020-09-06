package com.siwuxie095.concurrent.chapter3rd.example11th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 20:07:39
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 刨根究底：核心线程池的内部实现
     *
     * 对于核心的几个线程池，无论是 newFixedThreadPool()、newSingleThreadExecutor()、还是
     * newCachedThreadPool()，虽然看起来创建的线程有着完全不同的功能特点，但其内部实现均使用了
     * ThreadPoolExecutor 实现，下面给出这三个线程池的实现方式：
     *
     * public static ExecutorService newFixedThreadPool(int nThreads) {
     *         return new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     * }
     *
     * public static ExecutorService newSingleThreadExecutor() {
     *         return new FinalizableDelegatedExecutorService
     *             (new ThreadPoolExecutor(1, 1,
     *                                     0L, TimeUnit.MILLISECONDS,
     *                                     new LinkedBlockingQueue<Runnable>()));
     * }
     *
     * public static ExecutorService newCachedThreadPool() {
     *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     * }
     *
     * 由以上线程池的实现代码可以看到，它们都只是 ThreadPoolExecutor 类的封装。为何这个类有如此
     * 强大的功能呢？可以看一下 ThreadPoolExecutor 最重要的构造方法：
     *
     * public ThreadPoolExecutor(int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue,
     *                               ThreadFactory threadFactory,
     *                               RejectedExecutionHandler handler)
     *
     * 其参数含义如下：
     * （1）corePoolSize：指定了线程池中的核心线程数量。
     * （2）maximumPoolSize：指定了线程池中的最大线程数量。
     * （3）keepAliveTime：当线程池中的线程数量超过 corePoolSize 时，多余的空闲线程的存活时间。
     * 即超过 corePoolSize 的空闲线程，在多长时间内，会被销毁。
     * （4）unit：keepAliveTime 的时间单位。
     * （5）workQueue：任务队列，被提交但尚未被执行的任务会进入到此队列中，也称工作队列 或阻塞队列
     * 或等待队列 或并发队列。
     * （6）threadFactory：线程工厂，用于创建线程，一般用默认的即可。
     * （7）handler：拒绝策略。当任务太多，来不及处理时，如何拒绝任务。
     *
     * 参数 workQueue 指被提交但未执行的任务队列，它是一个 BlockingQueue 接口的对象，仅用于存放
     * Runnable 对象。根据队列功能分类，在 ThreadPoolExecutor 的构造方法中可以使用如下四种任务
     * 队列：
     * （1）同步任务队列：该功能由 SynchronousQueue 对象提供。SynchronousQueue 是一个特殊的
     * BlockingQueue。SynchronousQueue 没有容量，每一个插入操作都要等待相应的删除操作，反之，
     * 每一个删除操作都要等待对应的插入操作。如果使用 SynchronousQueue，提交的任务不会被真实的
     * 保存，而总是将新任务提交给线程执行，如果没有空闲的线程，则尝试创建新的线程，如果线程数量已经
     * 达到最大值，则执行拒绝策略。因此，使用 SynchronousQueue，通常要设置很大的 maximumPoolSize
     * 值，否则很容易执行拒绝策略。（PS：同步任务队列，即 一个直接提交的队列）
     *
     * （2）有界任务队列：有界的任务队列可以使用 ArrayBlockingQueue 实现。ArrayBlockingQueue
     * 的构造方法必须带一个容量参数，表示该队列的最大容量。
     *
     * public ArrayBlockingQueue(int capacity)
     *
     * 当使用有界任务队列时，若有新的任务需要执行，如果线程池的实际线程数小于 corePoolSize，则会
     * 优先创建新的线程，如果大于等于 corePoolSize，则会将新任务加入等待队列。若等待队列已满，无法
     * 加入，则在总线程数小于等于 maximumPoolSize 的前提下，创建新的线程执行任务。如果总线程大于
     * maximumPoolSize，则执行拒绝策略。可见，有界任务队列仅当在任务队列状态时，才可能线程数提升到
     * corePoolSize 以上，换言之，除非系统非常繁忙，否则应该确保核心线程数维持在 corePoolSize。
     *
     * （3）无界任务队列：无界任务队列可以通过 LinkedBlockingQueue 实现。与有界任务队列相比，除非
     * 系统资源耗尽，否则无界的任务队列不存在任务入队失败的情况。当有新的任务到来，系统的线程数小于
     * corePoolSize 时，线程池会生成新的线程执行任务，但当系统的线程数达到 corePoolSize 后，就不
     * 会继续增加，若后续有新的任务加入，而又没有空闲的线程资源，则任务直接进入队列等待。若任务创建和
     * 处理的速度差异很大，无界任务队列会保持快速增长，直到耗尽系统内存。
     *
     * （4）优先任务队列：优先任务队列是带有执行优先级的队列。它通过 PriorityBlockingQueue 实现，
     * 可以控制任务的执行先后顺序。它是一个特殊的无界队列。无论是有界队列 ArrayBlockingQueue，还
     * 是无界队列 LinkedBlockingQueue，都是按照先进先出算法处理任务的。而 PriorityBlockingQueue
     * 则可以根据任务自身的优先级顺序先后执行，在确保系统性能的同时，也能有很好的质量保证（总是确保
     * 高优先级的任务先执行）。
     *
     * 回顾 newFixedThreadPool() 方法的实现。它返回了一个 corePoolSize 和 maximumPoolSize
     * 大小一样的，并且使用了 LinkedBlockingQueue 任务队列的线程池。因为对于固定大小的线程池而言，
     * 不存在线程数量的动态变化，因此 corePoolSize 和 maximumPoolSize 可以相等。同时，它使用无
     * 界队列存放无法立即执行的任务，当任务提交非常频繁的时候，该队列可能迅速膨胀，从而耗尽系统资源。
     *
     * newSingleThreadExecutor() 返回的单线程线程池，是 newFixedThreadPool() 方法的一种退化，
     * 只是简单的将线程池数量设置为 1。
     *
     * 对于 newCachedThreadPool()，返回了一个 corePoolSize 为 0，而 maximumPoolSize 为 int
     * 型最大值，并且使用了 SynchronousQueue 任务队列的线程池。如果同时有大量任务被提交，而任务执行
     * 又不那么快时，那么系统便会开启等量的线程处理，这样的做法可能会很快耗尽系统的资源。
     *
     * 注意：使用自定义线程池时，要根据应用的具体情况，选择合适的并发队列作为任务的缓冲。当线程资源紧张
     * 时，不同的并发队列对系统行为和性能的影响均不同。
     *
     * PS：并发队列分为阻塞队列（如：ArrayBlockingQueue）和非阻塞队列（如：ConcurrentLinkedQueue），
     * 这里实际上指的是前者。
     *
     * 这里给出 ThreadPoolExecutor 线程池的核心调度代码，这段代码也充分体现了上述线程池的工作逻辑：
     *
     * public void execute(Runnable command) {
     *         if (command == null)
     *             throw new NullPointerException();
     *
     *         int c = ctl.get();
     *         if (workerCountOf(c) < corePoolSize) {
     *             if (addWorker(command, true))
     *                 return;
     *             c = ctl.get();
     *         }
     *         if (isRunning(c) && workQueue.offer(command)) {
     *             int recheck = ctl.get();
     *             if (! isRunning(recheck) && remove(command))
     *                 reject(command);
     *             else if (workerCountOf(recheck) == 0)
     *                 addWorker(null, false);
     *         }
     *         else if (!addWorker(command, false))
     *             reject(command);
     * }
     *
     * workerCountOf() 方法取得了当前线程池的线程总数，当线程总数小于 corePoolSize 核心线程数时，
     * 会将任务通过 addWorker() 方法直接调度执行，否则会通过 workQueue.offer() 进入等待队列，如
     * 果进入等待队列失败（比如有界队列达到了上限，或者使用了同步队列），则会将任务直接提交给线程池，
     * 如果当前线程数已经达到 maximumPoolSize，则提交失败，开始执行拒绝策略。
     */
    public static void main(String[] args) {

    }

}

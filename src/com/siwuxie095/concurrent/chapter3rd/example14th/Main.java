package com.siwuxie095.concurrent.chapter3rd.example14th;

/**
 * @author Jiajing Li
 * @date 2020-09-07 06:46:28
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 我的应用我做主：扩展线程池
     *
     * 虽然 JDK 中已经实现了稳定的高性能线程池，但如果需要对这个线程池做一些扩展，比如，想监控每个任务执行的开始时间
     * 和结束时间，或者其他一些自定义的增强功能，这时候应该怎么办？
     *
     * 一个好消息是：ThreadPoolExecutor 也是一个可以扩展的线程池。它提供了 beforeExecute()、afterExecute()
     * 和 terminated() 三个方法对线程池进行扩展。
     *
     * 以 beforeExecute()、afterExecute() 为例，在 ThreadPoolExecutor 中的 Worker 的 run() 方法中，调用了
     * runWorker() 方法（不属于 Worker，属于 ThreadPoolExecutor），其内部提供了这样的实现：
     *
     *                     beforeExecute(wt, task);
     *                     Throwable thrown = null;
     *                     try {
     *                         task.run();
     *                     } catch (RuntimeException x) {
     *                         thrown = x; throw x;
     *                     } catch (Error x) {
     *                         thrown = x; throw x;
     *                     } catch (Throwable x) {
     *                         thrown = x; throw new Error(x);
     *                     } finally {
     *                         afterExecute(task, thrown);
     *                     }
     *
     * ThreadPoolExecutor.Worker 是 ThreadPoolExecutor 的内部类，Worker 是一个实现了 Runnable 接口的类。
     * ThreadPoolExecutor 线程池中的工作线程也正是 Worker 实例。runWorker() 方法会被线程池以多线程模式异步
     * 调用，即 runWorker() 方法会同时被多线程访问。因此其 beforeExecute()、afterExecute() 方法也会被多线程
     * 访问。
     *
     * 在默认的 ThreadPoolExecutor 实现中，提供了空的 beforeExecute()、afterExecute() 实现。在实际应用中，
     * 可以对其进行扩展来实现实现对线程池运行状态的跟踪，输出一些有用的调试信息，以帮助系统故障诊断，这对于多线程
     * 程序错误排查是很有帮助的。
     *
     * 以 ExtendThreadPool 为例，演示了对多线程的扩展，并在扩展中记录每一个任务的执行日志。
     *
     * 其中实现了 beforeExecute()、afterExecute()、terminated() 三个方法，这三个方法分别用于记录一个任务的开始
     * 、结束和整个线程池的退出。然后向线程池提交任务，在提交完成后，调用 shutdown() 方法关闭线程池。如果当前正有线
     * 程在执行，shutdown() 方法并不会立即暴力地终止所有任务，它会等待所有任务执行完成后，再关闭线程池，但它并不会
     * 等待所有线程执行完成后再返回，因此，可以简单理解成 shutdown() 只是发送了一个关闭信号而已，但在 shutdown()
     * 方法执行完成后，这个线程就不能再接受其他新的任务了。
     *
     * PS：shutdown() 后只是不再接受新任务，而对于之前的旧任务，不管是正在线程池中运行，还是在任务队列中等待，最终
     * 都会执行完毕。
     */
    public static void main(String[] args) {

    }

}

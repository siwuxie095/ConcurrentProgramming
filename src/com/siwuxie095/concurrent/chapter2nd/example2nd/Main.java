package com.siwuxie095.concurrent.chapter2nd.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-08-24 22:03:19
 */
public class Main {

    /**
     * 线程的生命周期
     *
     * 线程的所有状态都在 Thread 中的 State 枚举中定义（见 {@link Thread#getState）。
     *
     *
     * 共 6 个状态，分别是：
     *
     * （1）NEW 即 初始状态，也称新建状态 或 创建状态。
     *
     * （2）RUNNABLE 即 运行状态，，也称为可运行状态 或 可执行状态。Java 中将 READY 就绪状态和 RUNNING 运行中状态，
     * 统称为 RUNNABLE 运行状态。
     *
     * （3）BOCKED 即 阻塞状态。
     *
     * （4）WAITING 即 等待状态（无时限）。
     *
     * （5）TIMED_WAITING 即 等待状态（有时限）。
     *
     * （6）TERMINATED 即 终止状态，也称 DEAD 死亡状态。
     *
     * PS：这里的中文释义右点混乱，明白大概的意思即可。其实不如直接记英文，说英文来的实在。
     *
     *
     * NEW 状态表示刚刚创建的线程，这种线程还没开始执行。等到线程的 start() 方法调用时，才表示线程开始执行。当线程执行
     * 时，处于 RUNNABLE 状态，表示线程所需的一切资源都已经准备好了。如果线程在执行过程中遇到 synchronized 同步块，就
     * 会进入 BLOCKED 阻塞状态，这时线程就会暂停执行，直到获得请求的锁。WAITING 和 TIMED_WAITING 都表示等待状态，它
     * 们的区别是 WAITING 会进入一个无时间限制的等待，TIMED_WAITING 会进入一个有时限的等待。那等待的线程究竟在等待什么
     * 呢？一般来说，WAITING 的线程正是在等待一些特殊的事件。比如，通过 wait() 方法等待的线程在等待 notify() 方法，而
     * 通过 join() 方法等待的线程则会等待目标线程的终止，一旦等到了期望的事件，线程就会再次执行，进入 RUNNABLE 状态。当
     * 线程执行完毕后，则进入 TERMINATED 状态，表示结束。
     *
     * 注意：从 NEW 状态出发后，线程不能再回到 NEW 状态。同理，处理 TERMINATED 状态的线程也不能再回到 RUNNABLE 状态。
     *
     *
     * 线程生命周期的转换：
     * （1）NEW 到 RUNNABLE：调用线程的 start() 方法启动线程。
     * （2）RUNNABLE 到 BLOCKED：运行的线程运行到同步代码块，如 synchronized 代码块，没有获取到锁。
     * （3）BLOCKED 到 RUNNABLE：阻塞的线程获取到了同步代码块的锁。
     * （4）RUNNABLE 到 WAITING：运行的线程调用了 wait() 等待。
     * （5）WAITING 到 RUNNABLE：等待的线程接到了 notify() 通知。
     * （6）RUNNABLE 到 TIMED_WAITING：同（4）。
     * （7）TIMED_WAITING 到 RUNNABLE：同（5）。
     * （8）RUNNABLE 到 TERMINATED：线程运行结束。
     */
    public static void main(String[] args) {

    }

}

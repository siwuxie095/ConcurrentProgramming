package com.siwuxie095.concurrent.chapter3rd.example8th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 14:23:06
 */
public class Main {

    /**
     * LockSupport 线程阻塞工具
     *
     * LockSupport 是一个非常方便使用的线程阻塞工具类，它可以在线程内任意位置让线程阻塞，和 Thread.suspend()、
     * Thread.resume() 相比，它弥补了由于 resume() 在前发生，导致线程无法继续执行的情况。
     *
     * LockSupport 有两个主要的静态方法：
     * （1）park() 可以挂起当前线程，即 阻塞线程。
     * （2）unpark() 可以恢复当前线程，即 唤醒线程。
     *
     * 类似的还有 parkNanos()、parkUntil() 等方法。它们实现了一个限时的等待。
     *
     * 以 LockSupportDemo 为例，使用 LockSupport 重写了 chapter2nd#example7th 中的 BadSuspend，即 那个
     * 有关 suspend() 永久卡死线程的示例（因为 resume() 在前发生导致的）。
     *
     * 注意，这里只是将原来的 suspend() 和 resume() 方法用 park() 和 unpark() 方法做了替换。当然，这里依然
     * 无法保证 unpark() 方法发生在 park() 方法之后，但是执行这段代码，却发现它自始至终都可以正常的结束，不会
     * 因为 park() 方法而导致线程永久性的挂起。
     *
     * 这是因为 LockSupport 使用类似信号量的机制。它为每一个线程准备了一个许可，如果许可可用，那么 park() 方法
     * 会立即返回，并且消费这个许可（也就是将许可变为不可用），如果许可不可用，就会阻塞。而 unpark() 则使得一个
     * 许可变为可用（但是和信号量不同的是，许可不能累加，你不可能拥有超过一个许可，它永远只有一个）。
     *
     * PS：其实可以理解为一个是否放行的许可，许可可用，即 放行，许可不可用，即 不放行。一开始，许可都是不可用的，
     * 即 不放行，也即 阻塞。所以，如果先执行 park()，许可不可用就会阻塞，再执行 unpark()，许可变为可用，park()
     * 才能返回，同时再次把许可变为不可用。如果先执行 unpark()，许可变为可用，再执行 park()，许可可用，不会阻塞，
     * 会直接返回，同时再次把许可变为不可用。
     *
     * 这个特点使得：即使 unpark() 操作发生在 park() 操作之前，它也可以使下一次的 park() 操作立即返回。这也就是
     * 上述代码可顺利结束的主要原因。
     *
     * 同时，处于 park() 挂起状态的线程不会向 suspend() 那样还给出一个令人费解的 RUNNABLE 状态，它会非常明确的
     * 给出一个 WAITING 状态，甚至还会标注是 park() 引起的。
     *
     * 这使得分析问题时格外方便。此外，如果使用 park(Object) 方法，还可以为当前线程设置一个阻塞对象，这个阻塞对象
     * 会出现在 Thread Dump 中。这样在分析问题时，就更加方便了。比如，直接将本例中的 park() 改为 park(this)，
     * 就可以直接在堆栈中看到当前线程等待的对象，即 ChangeObjectThread 的对象。
     *
     * 除了有定时阻塞的功能外，LockSupport.park() 还能支持中断响应。但是和其他接收中断的方法很不一样，并不会抛出
     * InterruptedException 异常。它只是会默默的返回，并置中断标志位，但是可以从 Thread.interrupted() 等方法
     * 获得中断标志位。
     *
     * 以 InterruptDemo 为例，中断了处于 park() 状态的 t1，t1 可以马上响应这个中断，并且返回。之后在外面等待的
     * t2 才可以进入临界区，并最终由 LockSupport.unpark(t2) 操作使其运行结束。
     */
    public static void main(String[] args) {

    }

}

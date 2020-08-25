package com.siwuxie095.concurrent.chapter2nd.example3rd;

/**
 * @author Jiajing Li
 * @date 2020-08-25 07:13:57
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 线程的基本操作：新建线程
     *
     * 新建线程很简单。只要使用 new 关键字创建一个线程对象，并且将它 start() 启动起来即可。
     *
     * Thread t1 = new Thread();
     * t1.start();
     *
     * 那线程 start() 后，会干什么呢？这才是问题的关键。线程 Thread，有一个 run() 方法，
     * start() 方法就会新建一个线程，并让这个线程执行 run() 方法。
     *
     * 注意，如下代码也能通过编译，也能正常执行。但是却不能新建一个线程，而是在当前线程中调用
     * run() 方法，只是作为一个普通的方法调用。这就是调用 start() 方法和直接调用 run() 方
     * 法的区别。
     *
     * Thread t1 = new Thread();
     * t1.run();
     *
     * 强调：不要用 run() 来开启新线程，它只会在当前线程中，串行执行 run() 中的代码。
     *
     * 默认情况下，Thread 的 run() 方法什么都没有做，因此，这个线程一启动就马上结束了。如果
     * 想让线程做点什么，就必须重写 run() 方法，把你的任务填进去。
     *
     * Thread t1 = new Thread() {
     *      @Override
     *      public void run() {
     *           System.out.println("Hello, I am t1");
     *      }
     * };
     * t1.start();
     *
     * 上述代码使用匿名内部类，重写了 run() 方法，并要求线程在执行时打印 "Hello, I am t1"
     * 的字样。如果没有特别的需要，都可以通过继承 Thread，重写 run() 方法来自定义线程。但考
     * 虑到 Java 是单继承的，也就是说继承本身也是一种很宝贵的资源，因此，也可以使用 Runnable
     * 接口来实现同样的操作。Runnable 是一个单方法接口，它只有一个 run() 方法。
     *
     * public interface Runnable {
     *     public abstract void run();
     * }
     *
     * 此外，Thread 类有一个非常重要的构造方法：
     *
     * public Thread(Runnable target)
     *
     * 它传入一个 Runnable 接口的实例，在 start 方法调用时，新的线程就会执行 Runnable.run()
     * 方法。实际上，默认的 Thread.run() 就是这么做的：
     *
     * public void run() {
     *     if (target != null) {
     *         target.run();
     *     }
     * }
     *
     * 强调：默认的 Thread.run() 就是直接调用内部的 Runnable 接口。因此，使用 Runnable 接口
     * 告诉线程该做什么，更为合理。
     *
     * 实际上，Thread 类本身也是实现了 Runnable 接口。另外，从 Java 8 开始，Runnable 也可以
     * 用 lambda 表达式的形式传入 Thread 构造函数中。
     *
     * PS：整体行文里，新建线程其实是另外启动线程 或另外开启线程的意思，不要和线程状态里的 NEW
     * 状态搞混淆。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter2nd.example5th;

/**
 * @author Jiajing Li
 * @date 2020-08-26 07:37:18
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 线程的基本操作：线程中断
     *
     * 在 Java 中，线程中断是一种重要的线程协作机制。从表面上理解，中断就是让目标线程停止执行的意思，
     * 实际上并非完全如此。在 example4th 已经详细讨论了 Thread.stop() 方法停止线程的害处，并且使
     * 用了一套自有的机制完善线程退出的功能。对此，JDK 提供了更强大的支持，那就是线程中断。
     *
     * 严格地讲，线程中断并不会使线程立即退出，而是给线程发送一个通知，告知目标线程，有人希望你退出了。
     * 至于目标线程接到通知后如何处理，则完全由目标线程自行决定。这点很重要，如果中断后，线程立即无条
     * 件退出，就又会遇到 Thread.stop() 方法的老问题。
     *
     * 与线程中断有关的方法有三个，这三个方法看起来很像，注意不要混淆和误用。
     * （1）public void Thread.interrupt()                // 中断线程
     * （2）public boolean Thread.isInterrupted()         // 判断是否被中断
     * （3）public static boolean Thread.interrupted()    // 判断是否被中断，并清除当前中断状态
     *
     * 进一步解释：
     * （1）第一个方法是一个实例方法，它通知目标线程中断，也就是设置中断标志位。中断标志位表示当前线程
     * 已经被中断了。
     * （2）第二个方法也是一个实例方法，它通过检查中断标志位，判断当前线程是否有被中断。
     * （3）第三个方法是一个静态方法，也是用来判断当前线程的中断状态，但同时会清除当前线程的中断标志位
     * 状态。
     *
     * 在 demo1 中，虽然对 t1 进行了中断，但是在 t1 中并没有中断处理的逻辑，因此，即使 t1 线程被置上
     * 了中断状态，但是这个中断不会发生任何作用。
     *
     * 在 demo2 中，增加了相应的中断处理代码：使用 Thread.isInterrupted() 方法判断当前线程是否被中
     * 断了，如果是，则退出循环体，结束线程。这看起来与 example4th 中增加 stopMe 标记的手法非常类似，
     * 但是中断的功能更为强劲。比如，如果在循环体中，出现了类似于 wait() 或者 sleep() 这样的操作，则
     * 只能通过中断来识别了。
     *
     * 简单介绍一下 Thread.sleep() 方法：
     * public static native void sleep(long millis) throws InterruptedException;
     *
     * Thread.sleep() 方法会让当前线程休眠若干时间，它会抛出一个 InterruptedException 中断异常。
     * InterruptException 不是运行时异常，也就是说程序必须捕获并且处理它，当线程在 sleep() 休眠时，
     * 如果被中断，这个异常就会产生。
     *
     * 在 demo3 中，调用 Thread.sleep() 时，如果线程被中断，则会抛出异常，在 catch 子句部分，由于
     * 已经捕获了中断，所以可以立即退出线程。但在这里并没有这么做，因为也许在这段代码中，还必须进行后续
     * 的处理，保证数据的一致性和完整性，因此，执行了 Thread.interrupt() 方法再次中断自己，置上中断
     * 标志位。这样，在重新进行中断检查时，才能发现当前线程已经被中断了。
     *
     * 注意：Thread.sleep() 方法由于中断而抛出异常，此时，它会清除中断标记，如果不加处理，那么在下一
     * 次循环开始时，就无法捕获这个中断，故在异常处理中，再次设置中断标志位。
     */
    public static void main(String[] args) throws InterruptedException {
        demo1();
        demo2();
        demo3();
    }

    /**
     * demo1
     */
    private static void demo1() throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Thread.yield();
                }
            }
        };
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
    }

    /**
     * demo2
     */
    private static void demo2() throws InterruptedException {
        Thread t2 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Interrupted");
                        break;
                    }
                    Thread.yield();
                }
            }
        };
        t2.start();
        Thread.sleep(2000);
        t2.interrupt();
    }

    /**
     * demo3
     */
    private static void demo3() {
        Thread t3 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Interrupted");
                        break;
                    }
                    Thread.yield();
                }
            }
        };
        t3.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted When Sleep");
            // 设置中断状态
            Thread.currentThread().interrupt();
        }
        t3.interrupt();
    }

}

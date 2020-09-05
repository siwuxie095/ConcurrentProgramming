package com.siwuxie095.concurrent.chapter3rd.example4th;

/**
 * @author Jiajing Li
 * @date 2020-09-04 08:20:50
 */
public class Main {

    /**
     * 允许多个线程同时访问：信号量(Semaphore)
     *
     * 信号量为多线程协作提供了更为强大的控制方法。广义上说，信号量是对锁的扩展。无论是内部锁 synchronized
     * 还是重入锁 ReentrantLock，一次都只允许一个线程访问一个资源，而信号量却可以指定多个线程，同时访问某一
     * 个资源。信号量主要提供了以下构造函数：
     *
     * public Semaphore(int permits)
     * public Semaphore(int permits, boolean fair)          // 第二个参数可以指定是否公平
     *
     * 在构造信号量对象时，必须指定信号量的准入数，即 同时能申请多少个许可。当每个线程每次只申请一个许可时，这
     * 就相当于指定了同时有多少个线程可以访问某一个资源。信号量的主要逻辑方法有：
     *
     * public void acquire()
     * public void acquireUninterruptibly()
     * public boolean tryAcquire()
     * public boolean tryAcquire(long timeout, Timeout unit)
     * public void release()
     *
     * acquire() 方法尝试获得一个准入的许可。若无法获得，则线程会等待，直到有线程释放一个许可或者当前线程被
     * 中断。acquireUninterruptibly() 方法和 acquire() 方法类似，但是不响应中断。tryAcquire() 尝试获
     * 得一个许可，如果成功返回 true，如果失败返回 false，它不会进行等待，会立即返回。release() 用于在线
     * 程访问资源结束后，释放一个许可，以使其他等待许可的线程可以进行资源访问。
     *
     * 以 SemapDemo 为例，先声明了一个包含 5 个许可的信号量，这就意味着同时可以有 5 个线程进入临界区。申请
     * 信号量使用 acquire() 操作，在离开时，则使用 release() 操作释放信号量。这就和释放锁是一个道理。如果
     * 不幸发生了信号量的泄露（申请了但没释放），那么可以进入临界区的线程数量就会越来越少，直到所有的线程均不
     * 可访问。（PS：其实这里申请的是信号量的许可，释放的也是信号量的许可，泄露的也是信号量的许可。）
     *
     * 在本例中，同时开启 20 个线程。运行之后，不难发现，是以 5 个线程为一组，依次输出带有线程 ID 的提示文本。
     *
     * 其实在 Semaphore 的 JDK 官方 Javadoc 中，就有一个有关信号量使用的简单例子，这里把它复制过来（参见
     * Pool 示例）。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter4th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-12 15:59:15
 */
public class Main {

    /**
     * JVM 对锁优化所做的努力：自旋锁
     *
     * 锁膨胀后，虚拟机为了避免线程真实地在操作系统层面挂起，虚拟机还会再做最后的努力 -- 自旋锁。由于
     * 当前线程暂时无法获得锁，但是什么时候获得锁是一个未知数。也许几个 CPU 时钟周期后，就可以得到锁。
     * 如果这样，简单粗暴的挂起线程可能是一种得不偿失的操作。因此，系统会进行一次赌注：它假设在不久的
     * 将来，当前线程可以得到这把锁。因此，JVM 会让当前线程进行空循环（这也是自旋的含义），在经过若干
     * 次循环后，如果可以得到锁，那么就顺利进入临界区。如果还不能获得锁，才会真实地将当前线程在操作系
     * 统层面挂起。
     *
     * 在 JDK 6 中，使用如下 JVM 参数可以开启自旋锁（启用）：
     *
     * -XX:+UseSpinning
     *
     * 同时，使用如下 JVM 参数可以设置自旋锁的等待次数：
     *
     * -XX:PreBlockSpin
     *
     * 从 JDK 7 开始，自旋锁的配置参数被取消，虚拟机不再支持由用户配置自旋锁，自旋锁总是会执行，自旋锁
     * 的等待次数也由虚拟机自动调整。
     */
    public static void main(String[] args) {

    }

}

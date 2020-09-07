package com.siwuxie095.concurrent.chapter3rd.example15th;

/**
 * @author Jiajing Li
 * @date 2020-09-07 08:23:03
 */
public class Main {

    /**
     * 合理的选择：优化线程池线程数量
     *
     * 线程池的大小对系统的性能有一定的影响。过大或者过小的线程数量都无法发挥最优的系统性能，但是线程池大小的确定
     * 也不需要做得非常精确，因为只要避免极大和极小两种情况，线程池的大小对系统的性能并不会影响太大。一般来说，确
     * 定线程池的大小需要考虑 CPU 数量、内存大小等因素。在《Java Concurrency in Practice》一书中给出了一个估
     * 算线程池大小的经验公式：
     *
     * Ncpu = CPU 的数量
     * Ucpu = 目标 CPU 的使用率（0 <= Ucpu <= 1）
     * W/C  = 等待时间与计算时间的比率
     *
     * 为保持处理器达到期望的使用率，最优的线程池大小等于：
     *
     * Nthreads = Ncpu * Ucpu * (1 + W/C)
     *
     * 在 Java 中，可以通过 Runtime.getRuntime().availableProcessors() 取得可用的 CPU 数量。
     *
     */
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

}

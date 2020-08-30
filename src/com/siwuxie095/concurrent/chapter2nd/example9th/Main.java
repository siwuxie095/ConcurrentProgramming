package com.siwuxie095.concurrent.chapter2nd.example9th;

/**
 * @author Jiajing Li
 * @date 2020-08-30 22:16:04
 */
public class Main {

    /**
     * volatile 与 Java 内存模型(JMM)
     *
     * Java 内存模型主要是围绕原子性、可见性和有序性来展开的。为了在适当的场合，确保线程间的原子性、可见性和有序性。
     * Java 使用了一些特殊的操作或者关键字来声明，告诉虚拟机，在这个地方要尤其注意，不能随意变动优化目标指令。关键
     * 字 volatile 就是其中之一。
     *
     * 通过查阅英文字典，不难发现，有关 volatile 的解释中，最常用的解释是 "易变的、不稳定的"，这也正是使用 volatile
     * 关键字的语义。
     *
     * 当你用 volatile 去声明一个变量时，就等于告诉虚拟机，这个变量极有可能会被某些程序或者线程修改。为了确保这个
     * 变量被修改后，应用程序范围内的所有线程都能够 "看到" 这个改动，虚拟机就必须采用一些特殊手段，保证这个变量的
     * 可见性等特点。
     *
     * 比如，根据编译器的优化规则，如果不使用 volatile 声明变量，那么这个变量被修改后，其他线程可能并不会被通知到，
     * 甚至在别的线程中，看到变量的修改顺序都会是反的。但是一旦使用 volatile，虚拟机就会特别小心地处理这种情况。
     *
     * 以 MultiThreadLong 为例，在 32 位系统中，long 类型的读和写是分两次的，为了保证每次写数据时不会被 "写坏"，
     * 最简单的一种方式就是加入 volatile 声明，告诉编译器，这个 long 类型的数据，需要格外小心，因为它会不断的修改。
     * （PS：在 chapter1st#example5th 中也有 MultiThreadLong 示例，区别就是是否加了 volatile 声明。）
     *
     * 虽然 volatile 对于保证操作的原子性有很大的帮助，但是需要注意的是，volatile 并不能替代锁，它也无法保证一些
     * 复合操作的原子性。以 ComplexOperation 为例，volatile 是无法保证 i++ 的原子性操作的。因为如果 i++ 是原子
     * 性的，那么最终的值应该是 100_000（10 个线程各累加 10_000 次）。但实际上，输出总是会小于 100_000。
     *
     * 此外，volatile 也能保证数据的可见性和有序性。以 WithNoVisibility 为例，ReaderThread 只有在 exit 为 true
     * 时，才会退出。在主线程中，开启 ReaderThread 后，就为 number 和 exit 赋值，并期望 ReaderThread 能够看到
     * 这些变化。
     *
     * 在虚拟机的 Client 模式下，由于 JIT 没有做足够的优化，在主线程修改 exit 变量的状态后，ReaderThread 能够发现
     * 这个改动，并退出程序。但是在虚拟机的 Server 模式下，由于系统优化的结果，ReaderThread 无法 "看到" 主线程中的
     * 修改，导致 ReaderThread 永远无法退出，这显然不是想要的结果。这就是一个典型的可见性问题。
     *
     * 注意：可以使用 java -version 命令查看虚拟机的运行模式，只有 32 位虚拟机才支持 Client 和 Server 两种模式，
     * 64 位虚拟机仅支持 Server 模式。
     *
     * 和原子性问题一样，只要简单的使用 volatile 来声明 exit 变量，告诉虚拟机，这个变量可能会在不同的线程中修改。
     * 这样就顺利解决这个问题了。参见 WithVisibility 示例。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter2nd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 20:19:31
 */
public class Main {

    /**
     * 线程安全的概念与 synchronized
     *
     * 并行程序开发的一大关注重点就是线程安全。一般来说，程序并行化是为了获得更高的执行效率，但前提是，
     * 高效率不能以牺牲正确性为代价。如果程序并行化后，连基本的执行结果的正确性都无法保证，那么并行程序
     * 本身也就没有任何意义了。因此，线程安全就是并行程序的根本和根基。比如32位系统下多线程读写 long
     * 型数据，就是一个典型的反例。但是在使用 volatile 关键字后，这种错误的情况有所改善。但是 volatile
     * 并不能真正的保证线程安全。它只能确保一个线程修改了数据后，其他线程能够看到这个改动。但当两个线程
     * 同时修改某一个数据时，却依然会产生冲突。（即 一个线程写值，其他线程可以读到最新值，但是多个线程
     * 同时写值，就会产生冲突）
     *
     * 以 AccountingWithVolatile 为例，演示了一个计数器，两个线程同时对 i 进行累加操作，各执行
     * 10_000_000 次，希望的执行结果是：最终 i 的值可以达到 20_000_000，但事实并非如此。多次运行后，
     * 不难发现，多数时候 i 的最终值会小于 20_000_000。这就是因为两个线程同时对 i 进行写入时，其中一
     * 个线程的结果会覆盖另一个（虽然此时 i 被声明为 volatile 变量）。
     *
     * 比如两个线程同时读到 i 为 0，各自计算得到 i 等于 1，并先后写入这个结果，因此，虽然 i++ 被执行了
     * 两次，但是实际 i 的值只增加了 1。这就是多线程不安全的恶果。
     *
     * 要从根本上解决这个问题，就必须保证多个线程在对 i 进行操作时完全同步。也就是说，当线程 A 在写入时，
     * 线程 B 不仅不能写，同时也不能读。因为在线程 A 写完之前，线程 B 读到的一定是一个过期数据。Java
     * 中，提供了一个重要的关键字 synchronized 来实现这个功能。
     *
     * 关键字 synchronized 的作用是实现线程间的同步。它的工作是对同步的代码加锁（实质是对对象的头部进行
     * 加锁），使得每一次，只能有一个线程进入同步块，从而保证线程间的安全性。
     *
     * 关键字 synchronized 的用法，具体有四种表现形式：
     * （1）直接作用于加锁对象：对给定对象加锁，进入同步代码前要获得给定对象的锁。
     * （2）直接作用于加锁类：对给定类加锁（实际上是对给定类的 Class 对象加锁），进入同步代码前要获得给定类的锁。
     * （3）直接作用于实例方法：相当于对当前实例加锁，进入同步代码前要获得当前实例对锁，也即（1）。
     * （4）直接作用于静态方法：相当于对当前类加锁，进入同步代码前要获得当前类对锁，也即 （2）。
     *
     * 本质上就两种表现形式，作用于给定对象或给定类（实际是：给定类的 Class 对象），也就是说（3）（4）是（1）（2）
     * 的延伸。
     *
     * PS：synchronized 也称为内部锁（或者监视器锁，或者 Monitor 锁），就表现形式来说，分为对象锁和类锁两种。
     *
     * 这里分别以 AccountingWithSync1st、AccountingWithSync2nd、AccountingWithSync3rd、
     * AccountingWithSync4th 为例，展现这四种表现形式。每次当线程进入被 synchronized 包裹的代码段，就都会
     * 请求 synchronized 锁。如果当前有其他线程正持有这把锁，那么新到的线程就必须等待（即 发生了阻塞）。这样，
     * 就保证了每次只能有一个线程执行 i++ 操作。
     *
     * 值得注意的是，当 synchronized 作用于给定对象时，需要让多个线程都关注到同一个对象锁上去，才能够保证线程
     * 安全。以 AccountingWithSyncBad 为例，虽然声明了同步方法，但是执行这段代码的两个线程指向了两个不同的
     * Runnable 实例，每个线程都只关心自己的对象锁。也就是说，这两个线程使用的是两把不同的锁。因此，线程安全是
     * 无法保证的。
     *
     * 除了用于线程同步、确保线程安全（原子性）外，synchronized 还可以保证线程间的可见性和有序性。从可见性来讲，
     * sychronized 可以完全替代 volatile 的功能，只是使用上没那么方便。就有序性而言，由于 synchronized 限制
     * 每次只有一个线程可以访问同步块，因此，无论同步块内的代码如何被乱序执行，只要保证串行语义一致，那么执行结果
     * 总是一样的。而其他访问线程，又必须在获得锁后方能进入代码块读取数据，因此，它们看到的最终结果并不取决于代码
     * 的执行过程，从而有序性问题自然得到了解决。换言之，被 synchronized 限制的多个线程是串行执行的（从同步块的
     * 角度来看）。
     */
    public static void main(String[] args) {

    }

}

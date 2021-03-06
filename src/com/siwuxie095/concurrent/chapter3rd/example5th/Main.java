package com.siwuxie095.concurrent.chapter3rd.example5th;

/**
 * @author Jiajing Li
 * @date 2020-09-06 08:17:21
 */
public class Main {

    /**
     * ReadWriteLock 读写锁
     *
     * ReadWriteLock 是 JDK5 中提供的读写分离锁，读写分离锁可以有效地帮助减少锁竞争，以提升系统性能。
     * 用锁分离的机制来提升性能非常容易理解，比如线程 A1、A2、A3 进行写操作，B1、B2、B3 进行读操作，
     * 如果使用重入锁 ReentrantLock 或者内部锁 synchronized，则理论上说所有的读和读之间、读和写之间、
     * 写和写之间都是串行操作。当 B1 进行读取时，B2、B3 则需要等待锁。由于读操作并不对数据的完整性造成
     * 破坏，这种等待显然是不合理。因此，读写锁就有了发挥功能的余地。
     *
     * 在这种情况下，读写锁允许多个线程同时读，使得 B1、B2、B3 之间真正并行。但是考虑到数据的完整性，写
     * 写操作和读写操作间依然是需要相互等待和持有锁的。总的来说，读写锁的访问约束如下：
     *
     *              读               写
     * 读           非阻塞           阻塞
     * 写           阻塞             阻塞
     *
     * 即：
     * （1）读读不互斥：读读之间不阻塞
     * （2）读写互斥：读会阻塞写，写也会阻塞读
     * （3）写写互斥：写写之间阻塞
     *
     * 如果在系统中，读的操作次数远远大于写操作，则读写锁就可以发挥最大的功效，提升系统的性能。以 ReadWriteDemo
     * 为例，来说明读写锁对性能的帮助。本例中，分别模拟了读操作和写操作，且耗时均为 1 秒。然后开启 18 个读线程，
     * 2 个写线程。由于使用了读写分离，因此，读线程完全并行，而写会阻塞读，所以，这段代码运行大约 2 秒多就能结束。
     * 而如果使用普通的重入锁代替读写锁。那么所有的读和写线程之间都必须相互等待，所以这段代码要运行长达 20 多秒
     * 才能结束。
     */
    public static void main(String[] args) {

    }

}

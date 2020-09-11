package com.siwuxie095.concurrent.chapter4th.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-09-11 08:00:26
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 有助于提高锁性能的建议：减少锁持有时间
     *
     * 锁的竞争必然会导致程序的整体性能下降。为了将这种副作用降到最低，这里提出一些关于使用锁的建议，希望可以
     * 有助于写出性能更为优越的程序。
     *
     * 对于使用锁进行并发控制的应用程序来说，在锁竞争过程中，单个线程对锁的持有时间与系统性能有着直接的关系。
     * 如果线程持有锁的时间很长，那么相对的，锁的竞争程度也就越激烈。可以想象一下，如果要求 100 个人各自填写
     * 自己的身份信息，但是只给一支笔，每个拿着笔的时间越长，总体所花的时间就会越长。如果真的只能有一支笔共享
     * 给 100 个人使用，那么最好的方式就是让每个花尽量少的时间持笔，务必做到想好了再拿起笔写，千万不可拿着笔
     * 才思考这表格应该怎么填。程序开发也是类似，应该尽可能地减少对某个锁的占有时间，以减少线程间互斥的可能。
     *
     * 以下面的代码为例：
     *
     *     public synchronized void syncMethod() {
     *         otherCode1();
     *         mutexMethod();
     *         otherCode2();
     *     }
     *
     * syncMethod() 方法中，假设只有 mutexMethod() 方法有同步的需要，而 otherCode1() 和 otherCode2()
     * 并不需要做同步控制。如果 otherCode1() 和 other2() 分别是重量级的方法，则会花费较长的 CPU 时间。此
     * 时如果并发量较大，使用这种对整个方法做同步的方案，会导致等待线程大量增加。因为一个线程在进入该方法时就
     * 获得内部锁，但只有在所有任务都执行完后，才会释放锁。
     *
     * 一个较为优化的解决方案是，只在必要时进行同步，这样就能明显减少线程持有锁的时间，提高系统的吞吐量。如下
     * 是改进的代码：
     *
     *     public void syncMethod2() {
     *         otherCode1();
     *         synchronized (this) {
     *             mutexMethod();
     *         }
     *         otherCode2();
     *     }
     *
     * 在改进的代码中，只针对 mutexMethod() 方法做了同步，锁占用时间相对较短，因此能有更高的并行度。这种技术
     * 手段在 JDK 的源码包中也可以很容易找到，比如处理正则表达式的 Pattern 类的 matcher() 方法，如下：
     *
     *     public Matcher matcher(CharSequence input) {
     *         if (!compiled) {
     *             synchronized(this) {
     *                 if (!compiled)
     *                     compile();
     *             }
     *         }
     *         Matcher m = new Matcher(this, input);
     *         return m;
     *     }
     *
     * matcher() 方法有条件地进行锁申请，只有在表达式未编译时，进行局部的加锁。这种处理方式大大提高了该方法
     * 的执行效率和可靠性。
     *
     * 注意：减少锁的持有时间有助于降低锁冲突的可能性，进而提升系统的并发能力。
     */
    public static void main(String[] args) {

    }

    public synchronized void syncMethod() {
        otherCode1();
        mutexMethod();
        otherCode2();
    }

    public void syncMethod2() {
        otherCode1();
        synchronized (this) {
            mutexMethod();
        }
        otherCode2();
    }

    private void otherCode1() {}

    private void mutexMethod() {}

    private void otherCode2() {}


}

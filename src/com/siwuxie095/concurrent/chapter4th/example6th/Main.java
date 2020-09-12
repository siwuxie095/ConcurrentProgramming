package com.siwuxie095.concurrent.chapter4th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-12 08:56:38
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 有助于提高锁性能的建议：锁粗化
     *
     * 通常情况下，为了保证多线程间的有效并发，会要求每个线程持有锁的时间尽量短，即 在使用完公共资源后，应该立即释放锁。
     * 只有这样，等待在这个锁上的其他线程才能尽早地获得资源执行任务。但是，凡事都有一个度，如果对同一个锁不停的进行请求、
     * 同步和释放，其本身也会消耗系统宝贵的资源，反而不利于性能的优化。
     *
     * 为此，虚拟机在遇到一连串连续地对同一个锁不断的进行请求和释放时，便会把所有的锁操作整合成对锁的一次请求，从而减少
     * 对锁的请求同步次数，这个操作叫做锁的粗化。比如代码段：
     *
     *     public void demoMethod() {
     *          synchronized(lock) {
     *              // do something ...
     *          }
     *          // 做其他需要同步的工作，但能很快执行完成
     *          synchronized(lock) {
     *              // do something ...
     *          }
     *     }
     *
     * 会被整合成如下形式：
     *
     *     public void demoMethod() {
     *          // 整合成一次锁请求
     *          synchronized(lock) {
     *              // do something ...
     *              // do something ...
     *          }
     *     }
     *
     * 在开发过程中，应该有意识地在合理的场合进行锁的粗化，尤其当循环内请求锁时，以下是一个循环内请求锁的例子，在这种
     * 情况下，每次循环都有申请锁和释放锁的操作。显然，这是没有必要的。
     *
     *     for(int i = 0; i < CIRCLE; i++) {
     *         synchronized(lock) {
     *             // do something ...
     *         }
     *     }
     *
     * 所以，一种更加合理的做法应该是在外层只请求一次：
     *
     *     synchronized(lock) {
     *         for(int i = 0; i < CIRCLE; i++) {
     *              // do something ...
     *         }
     *     }
     *
     * 注意：性能优化就是根据运行时的真实情况对各个资源点进行权衡折中的过程。锁粗化的思想和减少锁持有时间是相反的，但在
     * 不同但场合，它们的效果并不相同。所以需要结合实际情况，进行权衡。
     */
    public static void main(String[] args) {

    }

}

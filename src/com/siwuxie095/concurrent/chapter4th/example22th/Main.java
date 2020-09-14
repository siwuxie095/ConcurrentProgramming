package com.siwuxie095.concurrent.chapter4th.example22th;

/**
 * @author Jiajing Li
 * @date 2020-09-14 22:24:32
 */
public class Main {

    /**
     * 让普通变量也享受原子操作：AtomicIntegerFieldUpdater
     *
     * 有时候，由于初期考虑不周，或者后期的需求变化，一些普通的变量可能会有线程安全的需求。如果改动不大，可以简单地
     * 修改程序中每一个使用或者读取这个变量的地方。但显然，这样并不符合软件设计中的一条重要原则 -- 开闭原则。也就是
     * 系统对功能的增加应该是开放的，而对修改应该是相对保守的。而且如果系统里使用到这个变量的地方特别多，一个个修改
     * 也是一件令人厌烦的事情（况且很多使用场景下可能只是只读的，并无线程安全的强烈要求，完全可以保持原样）。
     *
     * 如果有这种困扰，那其实根本不需要担心，因为在原子包里还有一个实用的工具类 AtomicIntegerFieldUpdater。它可
     * 以在不改动原代码的基础上，让普通的变量也能享受 CAS 操作带来的线程安全性。这样就可以修改极少的代码，来获得线
     * 程安全的保证。
     *
     * 根据数据类型的不同，这个 Updater 有三种，分别是 AtomicIntegerFieldUpdater、AtomicLongFieldUpdater、
     * AtomicReferenceFieldUpdater。顾名思义，它们分别可以对 int、long 和普通对象进行 CAS 修改。
     *
     * 以 AtomicIntegerFieldUpdaterDemo 为例，展示了 AtomicIntegerFieldUpdater 的用法。有这么一个投票的场景：
     * 假设某地要进行一次选举，如果选民投了候选人一票，就记为 1，否则记为 0。最终的选票显然就是所有数据的简单求和。
     *
     * 候选人的得票数量记录在 Candidate.score 中。注意，它是一个普通的 volatile 变量。而 volatile 变量本身并不
     * 是线程安全的。然后定义了一个 AtomicIntegerFieldUpdater 实例，用来对 Candidate.score 进行写入。而后续的
     * allScore 则用来检查 AtomicIntegerFieldUpdater 的正确性。如果 AtomicIntegerFieldUpdater 真的保证了线
     * 程安全，那么最终 Candidate.score 和 allScore 的值必然相等，否则就说明 AtomicIntegerFieldUpdater 根本
     * 没有保证线程安全的写入。在 main() 方法中模拟了计票过程，这里假设有 60% 的人投赞成票，并且投票是随机进行的。
     *
     * 运行代码，不难发现，最终的 Candidate.score 和 allScore 总是相等的。这说明 AtomicIntegerFieldUpdater
     * 很好的保证了 Candidate.class 的线程安全。
     *
     * 虽然 AtomicIntegerFieldUpdater 很好用，但还是有几个注意事项：
     * （1）Updater 只能修改它可见范围内的变量。因为 Updater 使用反射得到这个变量，如果变量不可见，就会出错。比如
     * 如果 score 声明为 private，就是不可行的。
     * （2）为了确保变量被正确的读取，变量必须是 volatile 类型的。如果原有代码中未声明这个类型，那么简单地做一下声明
     * 即可。
     * （3）由于 CAS 操作会通过对象实例中的偏移量直接进行赋值，因此，它不支持 static 字段，也就是说 不支持静态变量。
     * （PS：Unsafe.objectFieldOffset() 不支持静态变量）
     */
    public static void main(String[] args) {

    }

}

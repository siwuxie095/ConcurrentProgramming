package com.siwuxie095.concurrent.chapter4th.example20th;

/**
 * @author Jiajing Li
 * @date 2020-09-14 08:12:41
 */
public class Main {

    /**
     * 带有时间戳的对象引用：AtomicStampedReference
     *
     * AtomicReference 无法解决 ABA 问题的根本原因是对象在修改过程中，丢失了状态信息，导致对象值本身与状态
     * 画上了等号。因此，只要能够记录对象在修改过程中的状态值，就可以很好的解决对象被反复修改导致线程无法正确
     * 判断对象状态的问题。
     *
     * AtomicStampedReference 正是这么做的，它的内部不仅维护了对象值，还维护了一个时间戳（这里把它称为时间
     * 戳，实际上可以使用任何一个整数来表示状态值，也可以称之为 印记、标记、标志、版本号、版本戳）。当 AtomicStampedReference
     * 中对应的对象值被修改时，除了更新数据本身外，还必须要更新时间戳。当 AtomicStampedReference 设置对象值
     * 时，期望的对象值以及时间戳都必须满足当前的对象值以及时间戳，写入才会成功。因此，即使对象值被反复读写后，
     * 写回了原值，只要时间戳发生变化，就能防止不恰当的写入。
     *
     * AtomicStampedReference 的几个 API 在 AtomicReference 的基础上新增了有关时间戳的信息：
     * // 比较并设置 参数依次为：期望值 新值 期望时间戳 新时间戳
     * public boolean compareAndSet(V expectedReference, V newReference, int expectedStamp, int newStamp)
     * // 获得当前对象引用
     * public V getReference()
     * // 获得当前时间戳
     * public int getStamp()
     * // 设置当前对象引用和时间戳
     * public void set(V newReference, int newStamp)
     *
     * 有了 AtomicStampedReference 这个法宝，就再也不用担心对象被写坏了。
     *
     * 以 AtomicStampedReference 为例，来演示这个场景：有一家蛋糕店，为了挽留客户，决定为贵宾卡里余额小于
     * 20 元的客户一次性赠送 20 元，刺激消费者充值和消费。但条件是，每一位客人只能被赠送一次。
     *
     * 首先定义了用户账户余额为 19 元，时间戳为 0，然后开启了 3 个后台线程，不断扫描数据，为满足条件的客户充
     * 值。其中会判断用户余额和时间戳并给予赠送金额。如果已经被其他线程处理，那么当前线程就会失败。
     *
     * 对于赠予线程来说，首先获得了账户的时间戳，后续的赠予操作都是以这个时间戳为依据，如果赠予成功，则修改时间
     * 戳，使得系统不可能发生二次赠予的情况。对于消费线程也是类似，每次操作都使得时间戳加 1，使之不可能重复。
     *
     * 运行代码，得到如下输出：
     *
     * 余额小于 20 元，充值成功，余额：39 元
     * 余额大于 20 元，无须充值
     * 余额大于 20 元，无须充值
     * 大于 10 元
     * 成功消费 10 元，余额：29 元
     * 余额大于 20 元，无须充值
     * 余额大于 20 元，无须充值
     * 大于 10 元
     * 余额大于 20 元，无须充值
     * 成功消费 10 元，余额：19 元
     * 大于 10 元
     * 成功消费 10 元，余额：9 元
     * 没有足够的金额
     * 没有足够的金额
     * 没有足够的金额
     * 没有足够的金额
     *
     * 可以看到，账户只被赠予了一次。
     *
     * PS：通过加入时间戳 或 版本号，即可解决 ABA 问题（因为时间戳和版本号都是递增设计的，一旦修改，不会再回
     * 到原值。如果时间戳和版本号也会回到原值，那么依然会有 ABA 问题）。
     */
    public static void main(String[] args) {

    }

}

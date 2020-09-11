package com.siwuxie095.concurrent.chapter4th.example3rd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jiajing Li
 * @date 2020-09-11 21:14:41
 */
public class Main {

    /**
     * 有助于提高锁性能的建议：减小锁粒度
     *
     * 减小锁粒度也是一种削弱多线程锁竞争的有效手段。这种技术典型的使用场景就是 ConcurrentHashMap 类的实现。
     * 下面具体说说这个类在减小锁粒度上的应用。
     *
     * 对于 HashMap 来说，最重要的两个方法就是 get() 和 put()。一种最自然的想法就是对整个 HashMap 加锁，
     * 必然可以得到一个线程安全的对象。但是这样做，就认为加锁粒度太大。对于 ConcurrentHashMap，它内部进一步
     * 细分了若干个小的 HashMap，称之为段（Segment）。默认情况下，一个 ConcurrentHashMap 被进一步细分为
     * 16 个段。
     *
     * 如果需要在 ConcurrentHashMap 中增加一个新的表项，并不是将整个 HashMap 加锁，而是首先根据 hashcode
     * 得到该表项应该存放到那个段中，然后对该段加锁，并完成 put() 操作。在多线程环境中，如果多个线程同时进行
     * put() 操作，只要被加入的表项不存放在同一个段中，则线程间便可以做到真正的并行（这种技术称之为锁分段技术，
     * 在段上加的锁称之为分段锁）。
     *
     * PS：，
     *
     * 由于默认有 16 个段，因此，如果够幸运的话，ConcurrentHashMap 可以同时接受 16 个线程同时插入（插入不同
     * 的段中），从而大大提高其吞吐量。
     *
     * 但是减小锁粒度会引入一个新的问题：当系统需要取得全局锁时，其消耗的资源会比较多。仍然以 ConcurrentHashMap
     * 类为例，虽然其 put() 方法很好的分离了锁，但是当试图访问 ConcurrentHashMap 全局信息时，就会需要同时取得
     * 所有段的锁方能顺利实施。比如 ConcurrentHashMap 的 size() 方法，它将返回 ConcurrentHashMap 的全部有
     * 效表项的数量，但是要获取这个信息需要取得所有子段的锁。
     *
     * size() 方法在计算总数时，先要获得所有段的锁，然后再求和。然而 ConcurrentHashMap 的 size() 方法并不总
     * 是这样执行，它会先使用无锁的方式求和，如果失败才会尝试这种加锁的方法。
     *
     * 但不管怎么说，在高并发场合下，ConcurrentHashMap 的 size() 的性能依然要差于同步的 HashMap。
     *
     * 综上所述，只有在类似于 size() 这种获取全局信息的方法调用并不频繁时，减小锁粒度的方法才能真正意义上提高系统
     * 吞吐量。
     *
     * 注意：所谓减小锁粒度，就是指缩小锁定对象的范围，从而减少锁冲突的可能性，进而提高系统的并发能力。
     *
     * PS：以上关于 ConcurrentHashMap 的论述，是以 JDK 7 为参照的。实际上，在 JDK 8 中，就放弃了分段锁（使用
     * 重入锁 ReentrantLock 来实现）的使用，转而使用内部锁 synchronized 和 CAS。二者的区别在于：重入锁锁的是
     * 段（Segment），而内部锁锁的是节点（Node）。（在 JDK 8 中，HashMap 和 ConcurrentHashMap 中均引入了红
     * 黑树，用以加快检索速度。）
     */
    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
    }

}

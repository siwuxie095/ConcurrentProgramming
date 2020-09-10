package com.siwuxie095.concurrent.chapter3rd.example24th;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Jiajing Li
 * @date 2020-09-10 22:46:07
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 随机数据结构：跳表(SkipList)
     *
     * 在 JDK 的并发包中，除了常用的哈希表外，还实现了一种有趣的数据结构 -- 跳表。跳表是一种可以用来快速查找的数据结构，
     * 有点类似于平衡书。它们都可以对元素进行快速的查找。但一个重要的区别是：对平衡树的插入和删除往往很可能导致平衡树进行
     * 一次全局的调整。而对跳表的插入和删除只需要对整个数据结构的局部进行操作即可。这样带来的好处是：在高并发的情况下，往
     * 往需要一个全局锁来保证整个平衡树的线程安全。而对于跳表，只需要部分锁即可。这样，在高并发环境下，就可以拥有更好的性
     * 能。就查询的性能而言，跳表的时间复杂度也是 O(logN)。所以在并发数据结构中，JDK 使用跳表来实现一个 Map。
     *
     * 跳表的另外一个特点是随机算法。跳表的本质是同时维护了多个链表，并且链表是分层的。最底层的链表维护了跳表内所有的元素，
     * 每上面一层链表都是下面一层的子集，一个元素插入哪些层是完全随机的。因此，如果运气不好的话，可能会得到一个性能很糟糕
     * 的数据结构。但是在实际工作中，它的表现是非常好的。
     *
     * 跳表内的所有链表的元素都是排序的。查找时，可以从顶级链表开始找。一旦发现被查找的元素大于当前链表的当前节点的取值，
     * 且小于当前链表的下一个节点的取值（或当前链表没有下一个节点了），就会转入下一层链表继续找。也就是说，在查找过程中，
     * 搜索是跳跃式的。整个过程，要比一般链表从头节点开始逐个搜索快很多。
     *
     * 显然，跳表是一种使用空间换时间的算法。
     *
     * 使用跳表实现 Map 和使用哈希算法实现 Map 的另外一个不同之处是：哈希并不会保存元素的顺序，而跳表内所有的元素都是排序
     * 的。因此，在对跳表进行遍历时，会得到一个有序的结果。如果应用程序需要有序性，那么跳表就是不二的选择。
     *
     * 实现跳表这个数据结构的类是 ConcurrentSkipListMap。下面展示了跳表的简单使用：
     *
     *         Map<Integer, Integer> map = new ConcurrentSkipListMap<>();
     *         for (int i = 0; i < 30; i++) {
     *             map.put(i, i);
     *         }
     *         for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
     *             System.out.println(entry.getKey());
     *         }
     *
     * 和 HashMap 不同，对跳表的遍历输出是有序的。
     *
     * 跳表的内部实现有几个关键的数据结构组成。首先是 Node，一个 Node 就是表示一个节点，里面含有两个重要的 key 和 value
     * （就是 Map 的 key 和 value），每个 Node 还会指向下一个 Node，因此，还有一个元素 next。
     *
     *     static final class Node<K,V> {
     *         final K key;
     *         volatile Object value;
     *         volatile Node<K,V> next;
     *
     * 对 Node 的所有操作，使用的是 CAS 方法。
     *
     *         boolean casValue(Object cmp, Object val) {
     *             return UNSAFE.compareAndSwapObject(this, valueOffset, cmp, val);
     *         }
     *
     *         boolean casNext(Node<K,V> cmp, Node<K,V> val) {
     *             return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
     *         }
     *
     * 方法 casValue() 用来设置 value 的值，相对的 casNext() 用来设置 next 字段。
     *
     * 另外一个重要的数据结构是 Index。顾名思义，这个表示索引。它内部包装了 Node，同时增加了向下的引用和向右的引用。
     *
     *     static class Index<K,V> {
     *         final Node<K,V> node;
     *         final Index<K,V> down;
     *         volatile Index<K,V> right;
     *
     * 整个跳表就是根据 Index 进行全网的组织的。
     *
     * 此外，对于每一层的表头，还需要记录当前处于哪一层。为此，还需要一个称为 HeadIndex 的数据结构，表示链表头部的第一个
     * Index。它继承自 Index。
     *
     *     static final class HeadIndex<K,V> extends Index<K,V> {
     *         final int level;
     *         HeadIndex(Node<K,V> node, Index<K,V> down, Index<K,V> right, int level) {
     *             super(node, down, right);
     *             this.level = level;
     *         }
     *     }
     *
     * 这样，核心的内部元素就介绍完了。对于跳表的所有操作，就是组织好这些 Index 之间的连接关系。
     */
    public static void main(String[] args) {
        Map<Integer, Integer> map = new ConcurrentSkipListMap<>();
        for (int i = 0; i < 30; i++) {
            map.put(i, i);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

}

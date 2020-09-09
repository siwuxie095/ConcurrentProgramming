package com.siwuxie095.concurrent.chapter3rd.example20th;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-09-09 08:02:49
 */
public class Main {

    /**
     * 有关 List 的线程安全
     *
     * 在 Java 中，ArrayList 和 Vector 都是使用数组作为其内部实现。两者最大的不同在于 Vector 是线程安全的，而
     * ArrayList 不是。此外，LinkedList 使用链表的数据结构实现了 List。但是很不幸，LinkedList 并不是线程安全
     * 的，不过通过 Collections.synchronizedList() 来对任意 List 进行包装，此时，所得到的 List 对象就是线程
     * 安全的。
     *
     * 注意：ConcurrentLinkedQueue 和 ConcurrentLinkedDeque 内部也是通过链表实现的，而且它们是线程安全的，
     * 所以可以将这两个看作是线程安全的 LinkedList。仅仅是看作和当作，实际上还是有所不同。
     *
     * LinkedList、ConcurrentLinkedQueue、ConcurrentLinkedDeque 三者之间的渊源：
     *
     * （1）LinkedList 实现了 Deque，Deque 实现了 Queue。
     * （2）ConcurrentLinkedQueue 实现了 Queue。
     * （3）ConcurrentLinkedDeque 实现了 Deque，Deque 实现了 Queue。
     *
     * 鉴于以上关系，LinkedList 也可以看作是一个 Deque 或 Queue，但一般来说，还是看作是一个 List。
     *
     * PS：Queue 即 队列，Deque 即 双端队列（double ended queue，一般读作 deck）
     */
    public static void main(String[] args) {
        List list = Collections.synchronizedList(new LinkedList<>());
    }

}

package com.siwuxie095.concurrent.chapter3rd.example18th;

/**
 * @author Jiajing Li
 * @date 2020-09-08 21:13:17
 */
public class Main {

    /**
     * 超好用的工具类：JDK 的并发容器
     *
     * 除了提供诸如同步控制，线程池等基本工具外，为了提高开发者的效率，JDK 还提供了一大批好用的容器类，可以大大减少
     * 开发工作量。众所周知，程序 = 算法 + 数据结构，而这些容器类正是为多线程准备的数据结构，如 List、Set、Map、
     * Queue、Deque 等等。当然，它们都是线程安全的，而且封装的非常完善。
     *
     * JDK 提供的这些容器大部分都在 java.util.concurrent 包中，下面对这些容器类做简单的介绍：
     * （1）ConcurrentHashMap：这是一个高效的并发 HashMap，而且是线程安全的。
     *
     * （2）CopyOnWriteArrayList：这是一个 List，从名字看就是和 ArrayList 是一族的。在读多写少的场合，这个
     * List 的性能非常好，远远好于 Vector。
     *
     * （3）ConcurrentLinkedQueue：高效的并发队列，使用链表实现。可以看作一个线程安全的 LinkedList。
     *
     * （4）BlockingQueue：这是一个接口，JDK 内部通过链表、数组等方式实现了这个接口。表示阻塞队列，非常适合用于作为
     * 数据共享的通道。
     *
     * （5）ConcurrentSkipListMap：跳表的实现。这是一个 Map，使用跳表的数据结构进行快速查找。
     *
     * PS：ConcurrentLinkedQueue 和 BlockingQueue 都属于并发队列，只是前者是非阻塞队列，后者是阻塞队列。
     *
     * 除了以上并发包中的专有数据结构外，java.util 下的 Vector 也是线程安全的（虽然性能和上述专用工具没得比），另外
     * Collections 工具类也可以将任意集合包装成线程安全的集合。
     */
    public static void main(String[] args) {

    }

}

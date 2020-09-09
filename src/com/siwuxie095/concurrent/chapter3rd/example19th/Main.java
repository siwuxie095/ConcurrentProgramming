package com.siwuxie095.concurrent.chapter3rd.example19th;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2020-09-09 07:35:10
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 线程安全的 HashMap
     *
     * 众所周知，普通的 HashMap 在多线程环境中是线程不安全的。如果需要一个线程安全的 HashMap，一种可行的方法
     * 是使用 Collections.synchronizedMap() 方法来包装普通的 HashMap。这样，产生的 HashMap 就是线程安全
     * 的。如下所示：
     *
     * public static Map map = Collections.synchronizedMap(new HashMap<>());
     *
     * PS：Collections.synchronizedMap() 可以包装任意 Map，不只是 HashMap。
     *
     * Collections.synchronizedMap 会生成一个名为 SynchronizedMap 的 Map，它使用委托，将自己所有 Map
     * 相关的功能交给传入的 HashMap 实现，而自己则主要负责保证线程安全。
     *
     * 具体参考如下实现：
     *
     *     private static class SynchronizedMap<K,V>
     *         implements Map<K,V>, Serializable {
     *         private static final long serialVersionUID = 1978198479659022715L;
     *
     *         private final Map<K,V> m;     // Backing Map
     *         final Object      mutex;        // Object on which to synchronize
     *
     * SynchronizedMap 是 Collections 中的一个静态内部类。首先 SynchronizedMap 内包装了一个 Map，然后通过
     * mutex 实现对所包装的 m 的互斥操作。如下所示，所有相关的 Map 操作都会使用 mutex 进行同步，从而实现线程安
     * 全。 mutex 可以传入，也可以是 SynchronizedMap 自己。
     *
     *         public V get(Object key) {
     *             synchronized (mutex) {return m.get(key);}
     *         }
     *
     *         public V put(K key, V value) {
     *             synchronized (mutex) {return m.put(key, value);}
     *         }
     *
     * SynchronizedMap 这个包装的 Map 可以满足线程安全的要求，但是，它在多线程环境中的性能表现并不算太好。无论是
     * 对 Map 的读取或者写入，都需要获得 mutex 锁，这会导致所有对 Map 的操作全部进入等待状态，直到 mutex 锁可用。
     * 如果并发级别不高，一般也够用。但是，在高并发环境中，有必要寻求更好的解决方案。
     *
     * 一个更加专业的并发 HashMap 是 ConcurrentHashMap，位于 java.util.concurrent 包内。它专门为并发进行了
     * 性能优化，因此，更加适合多线程的场合。
     */
    public static void main(String[] args) {
        Map map = Collections.synchronizedMap(new HashMap<>());
    }

}

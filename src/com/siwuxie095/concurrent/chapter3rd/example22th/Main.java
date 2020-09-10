package com.siwuxie095.concurrent.chapter3rd.example22th;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Jiajing Li
 * @date 2020-09-10 07:46:41
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 高效读取：不变模式下的 CopyOnWriteArrayList
     *
     * 在很多应用场景中，读操作可能会远远大于写操作。比如，有些系统级别的信息，往往需要加载或者修改很少的次数，
     * 但是会被系统内所有模块频繁的访问。对于这种场景，最希望看到的就是读操作可以尽可能的快，而写即使慢一些也
     * 没有太大关系。
     *
     * 由于读操作根本不会修改原有的数据，因此对于每次读取都进行加锁其实是一种资源浪费。应该允许系统内多个线程
     * 同时访问 List 的内部数据，毕竟读取操作是安全的。根据读写锁的思想，读锁和读锁之间确实也不冲突。但是，读
     * 操作会受到写操作的阻碍，当写发生时，读就必须等待，否则可能读到不一致的数据。同理，如果读操作正在进行，
     * 程序也不能进行写入。
     *
     * 为了将读取的性能发挥到极致，JDK 中提供了 CopyOnWriteArrayList 类。对它来说，读取是完全不用加锁的，
     * 并且更好的消息是：写入操作也不会阻塞读取操作。只有写入操作和写入操作之间需要进行同步等待。这样一来，读取
     * 操作的性能就会大幅度提升。
     *
     * 那 CopyOnWriteArrayList 是怎么做到的呢？从类名可以看出，所谓 CopyOnWrite 就是在写入操作时，进行一次
     * 自我复制。换句话说，当这个 List 需要修改时，并不修改原有内容（这对于保证当前在读线程的数据一致性非常重要），
     * 而是对原有的数据进行一次复制，将修改的内容写入副本中。写完之后，再将修改完的副本替换原来的数据。这样就可以
     * 保证写操作不会影响读了。
     *
     * 下面的代码展示了有关读取的实现：
     *
     *     private transient volatile Object[] array;
     *
     *     final Object[] getArray() {
     *         return array;
     *     }
     *
     *     private E get(Object[] a, int index) {
     *         return (E) a[index];
     *     }
     *
     *     public E get(int index) {
     *         return get(getArray(), index);
     *     }
     *
     * 不难看出，读取代码没有任何的同步控制和锁操作，理由就是内部数组 array 不会发生修改，只会被另一个 array
     * 替换，因此可以保证数据安全。
     *
     * 和简单的读取操作相比，写入操作就有些麻烦了，如下：
     *
     *     public boolean add(E e) {
     *         final ReentrantLock lock = this.lock;
     *         lock.lock();
     *         try {
     *             Object[] elements = getArray();
     *             int len = elements.length;
     *             Object[] newElements = Arrays.copyOf(elements, len + 1);
     *             newElements[len] = e;
     *             setArray(newElements);
     *             return true;
     *         } finally {
     *             lock.unlock();
     *         }
     *     }
     *
     * 首先写操作使用锁，当然这个锁仅限于控制写-写的情况。其重点在于进行了内部元素的完整复制。因此，会生成一个新
     * 的数组 newElements。然后，将新的元素加入 newElements。接着，使用 setArray() 将新的数组替换老的数组，
     * 修改就完成了。并且修改完成后，读取线程可以立即察觉到这个修改，因为 array 变量使用了 volatile 声明。
     */
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("item");
        list.get(0);
    }

}

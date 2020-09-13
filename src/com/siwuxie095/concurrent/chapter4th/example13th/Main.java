package com.siwuxie095.concurrent.chapter4th.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-13 08:36:35
 */
@SuppressWarnings("all")
public class Main {

    /**
     * ThreadLocal 的实现原理
     *
     * 如果想知道 ThreadLocal 是如何保证对象只被当前线程所访问，自然需要深入探索一下 ThreadLocal
     * 的内部实现。
     *
     * 第一个需要关注的就是 ThreadLocal 的 set() 方法和 get() 方法。这里从 set() 方法先说起：
     *
     *     public void set(T value) {
     *         Thread t = Thread.currentThread();
     *         ThreadLocalMap map = getMap(t);
     *         if (map != null)
     *             map.set(this, value);
     *         else
     *             createMap(t, value);
     *     }
     *
     * 在 set() 时，首先获得当前线程对象，然后通过 getMap() 拿到线程 ThreadLocalMap，并将值设入
     * 其中。ThreadLocalMap 可以理解为一个 Map（虽然不是，但是可以把它简单的理解成 HashMap），而
     * 且它是定义在 Thread 内部的成员，如下所示是 Thread 中的定义：
     *
     *     ThreadLocal.ThreadLocalMap threadLocals = null;
     *
     * 而设置到 ThreadLocal 中到数据，也正是写入了 threadLocals 这个 Map。其中，key 为 ThreadLocal
     * 当前对象，value 就是所需要的值。而 threadLocals 本身就保存了当前自己所在线程的所有局部变量，
     * 也就是一个 ThreadLocal 变量的集合。
     *
     * 在进行 get() 操作时，自然就是将这个 Map 中的数据拿出来：
     *
     *     public T get() {
     *         Thread t = Thread.currentThread();
     *         ThreadLocalMap map = getMap(t);
     *         if (map != null) {
     *             ThreadLocalMap.Entry e = map.getEntry(this);
     *             if (e != null) {
     *                 @SuppressWarnings("unchecked")
     *                 T result = (T)e.value;
     *                 return result;
     *             }
     *         }
     *         return setInitialValue();
     *     }
     *
     * 首先，get() 方法也是先取得当前线程的 ThreadLocalMap 对象。然后通过将自己作为 key 取得内部
     * 的实际数据。
     *
     * 在了解了 ThreadLocal 的内部实现后，自然会引出一个问题。那就是这些变量是维护在 Thread 类的
     * 内部（ThreadLocalMap 定义所在类），这也意味着只要线程不退出，对象的引用将一直存在。
     *
     * 当线程退出时，Thread 类会进行一些清理工作，其中就包括清理 ThreadLocalMap。线程退出时，系统
     * 会自动回调 exit() 方法，进行资源清理，如下所示：
     *
     *     private void exit() {
     *         if (group != null) {
     *             group.threadTerminated(this);
     *             group = null;
     *         }
     *         // Aggressively null out all reference fields: see bug 4006245
     *         target = null;
     *         // Speed the release of some of these resources
     *         threadLocals = null;
     *         inheritableThreadLocals = null;
     *         inheritedAccessControlContext = null;
     *         blocker = null;
     *         uncaughtExceptionHandler = null;
     *     }
     *
     * 因此，如果使用线程池，那就意味着当前线程未必会退出（比如固定大小的线程池，线程总是存在）。如果
     * 这样，将一些大对象设置到 ThreadLocal 中（它实际保存在线程持有的 ThreadLocalMap 内），可能
     * 会使系统出现内存泄漏的可能（即 把对象设置到 ThreadLocal 中，但是不清理它，在使用几次后，这个
     * 对象也不再有用，然而却无法被回收了）。
     *
     * 此时，如果希望及时回收对象，最好使用 ThreadLocal.remove() 方法将这个变量移除。就像习惯性地
     * 关闭数据库连接一样，如果确实不需要这个对象了，那么就应该告诉虚拟机，请把它回收掉，防止内存泄漏。
     *
     * 另外一种有趣的情况是 JDK 也可能允许你像释放普通变量一样释放 ThreadLocal。比如，有时候为了加
     * 速垃圾回收，会特意写出类型 obj = null 之类的代码。如果这么做，obj 所指向的对象就会更容易地被
     * 垃圾回收器发现，从而加速回收。
     *
     * 同理，如果对于 ThreadLocal 的变量，也手动将其设置为 null，那么这个 ThreadLocal 对应的所有
     * 线程的局部变量都有可能被回收。
     *
     * 以 ThreadLocalGcDemo 为例，来揭示这里面的奥秘。为了跟踪 ThreadLocal 对象以及内部 SimpleDateFormat
     * 对象的垃圾回收，分别重载了各自的 finalize() 方法。这样，在对象回收时，就可以看到它们的踪迹。
     *
     * 在主方法 main() 中，先后进行了两次任务提交，每次 10_000 个任务。在第一次任务提交后，将 local
     * 设置为 null，接着进行一次 GC。然后进行第二次任务提交，完成后，再进行一次 GC。
     *
     * 运行代码，第一次任务提交，线程池中 10 个线程都各自创建了一个 SimpleDateFormat 对象实例。接着
     * 进行第一次 GC，此时 ThreadLocal 对象被回收了。然后第二次任务提交，线程池中 10 个线程又各自创
     * 建了一个 SimpleDateFormat 对象实例。接着进行第二次 GC，此时，第一次创建的 10 个 SimpleDateFormat
     * 对象实例全部被回收。这里并没有手工 remove() 这些对象，但是系统依然有可能回收它们。
     *
     * （注意：JDK 7 和 JDK 8 所得到的输出可能会不太一样）
     *
     * 要了解这里的回收机制，就需要进一步了解 Thread.ThreadLocalMap 的实现。ThreadLocalMap 是一个
     * 类似于 HashMap 的东西，更精确地说，更加类似于 WeakHashMap。
     *
     * ThreadLocalMap 的实现使用了弱引用。弱应用是比强引用弱得多的引用。JVM 在垃圾回收时，如果发现弱
     * 引用，就会立即回收。ThreadLocalMap 内部是由一系列 Entry 构成，每一个 Entry 都是
     * WeakReference<ThreadLocal<?>>，如下所示：
     *
     *         static class Entry extends WeakReference<ThreadLocal<?>> {
     *             // The value associated with this ThreadLocal.
     *             Object value;
     *
     *             Entry(ThreadLocal<?> k, Object v) {
     *                 super(k);
     *                 value = v;
     *             }
     *         }
     *
     * 这里的参数 k 就是 Map 的 key，参数 v 就是 Map 的 value。其中 k 也就是 ThreadLocal 实例，
     * 作为弱引用使用（super(k) 即调用 WeakReference 的构造方法）。因此，这里使用 ThreadLocal
     * 作为 Map 的 key，但是实际上，它并不真的持有 ThreadLocal 的引用。而当 ThreadLocal 的外部
     * 强引用被回收时，ThreadLocalMap 中的 key 就会变成 null。当系统进行 ThreadLocalMap 清理时
     * （比如将新的 ThreadLocal 变量加入到 ThreadLocalMap 中，就可能会自动进行一次清理【和版本有
     * 关系】），就会自然将这些垃圾数据回收。
     */
    public static void main(String[] args) {

    }

}

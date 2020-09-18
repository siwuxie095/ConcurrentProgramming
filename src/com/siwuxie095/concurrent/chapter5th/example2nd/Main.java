package com.siwuxie095.concurrent.chapter5th.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-09-18 07:55:58
 */
public class Main {

    /**
     * 探讨单例模式
     *
     * 单例模式是设计模式中使用最为普遍的模式之一。它是一种对象创建模式，用于产生一个对象的具体实例，它可以确保系统中
     * 一个类只产生一个实例。在 Java 中，这样的行为能带来两大好处：
     *
     * （1）对于频繁使用的对象，可以省略 new 操作花费的时间，这对于那些重量级对象而言，是非常可观的一笔系统开销；
     * （2）由于 new 操作的次数减少，因而对系统内存的使用频率也会降低，这将减轻 GC 压力，缩短 GC 停顿时间。
     *
     * 严格来说，单例模式与并行没有直接的关系。这里之所以讨论这个模式，是因为它实在是太常见了。并且，不可避免的，会在
     * 多线程环境中使用它们。并且，系统中使用单例的地方可能非常频繁，因此，非常迫切需要一种高效的单例实现。
     *
     * 以 Singleton 为例，这个实现非常简单，而且无疑是一个正确并且良好的实现。使用这种方式创建单例有几点必须特别注意。
     *
     * （1）因为要保证系统中不会有人意外创建多余的实例，所以把 Singleton 的构造函数设置为 private。这就警告所有开发
     * 人员，不能随便创建这个类的实例，从而避免该类被错误的创建。
     *
     * （2）instance 对象必须是 private 并且 static 的。如果不是 private，那么 instance 的安全性无法得到保证。
     * 一个小小的意外就能使得 instance 变成 null。其次，因为工厂方法 getInstance() 必须是 static 的，因此对应的
     * instance 也必须是 static。
     *
     * 这个单例的性能是非常好的，因为 getInstance() 方法只是简单地返回 instance，并没有任何锁操作，因此它在并行程序
     * 中，会有良好的表现。
     *
     * 但是这种方式有一点明显不足，就是 Singleton 构造函数，或者说 Singleton 实例在什么时候创建是不受控制的。对于
     * 静态成员 instance，它会在类第一次初始化的时候被创建。这个时刻并不一定是 getInstance() 方法第一次被调用的时候。
     *
     * 以 SingletonWithStatus 为例，其中还包含了一个表示状态的静态成员 status。此时，在相同任何地方引用这个 status
     * 都会导致 instance 实例被创建（任何对 SingletonWithStatus 的方法或字段的引用，都会导致类初始化，并创建 instance
     * 实例，但是类初始化只有一次，因此 instance 实例永远只会被创建一次）。
     *
     * 比如如下调用也会创建单例：
     *
     * System.out.println(SingletonWithStatus.status);
     *
     * 即使系统没有要求创建单例，new Singleton() 也会被调用。如果觉得这个不足并不重要，那么这种单例模式是一种不错的
     * 选择。它很容易实现，代码易读而且性能优越。
     *
     * 但如果想精确控制 instance 的创建时间，那么这种方式就太不友好了。所以需要寻找一种新的方法，一种支持延迟加载的
     * 策略，它只会在 instance 被第一次使用时创建对象。
     *
     * 以 LazySingleton 为例，它的核心思想如下：最初，并不需要实例化 instance，而当 getInstance() 方法被第一次
     * 调用时，创建单例对象。为了防止对象被多次创建，不得不使用 synchronized 进行方法同步。这种实现的好处是，利用了
     * 延迟加载，只在真正需要时创建对象。但坏处也很明显，并发环境下加锁，竞争激烈的场合对性能可能产生一定的影响。但总
     * 体上，这是一个非常易于实现和理解的方法。
     *
     * 此外，有一种被成为双重检查加锁的方法可以用于创建单例（参见 LazySingletonWithDoubleCheck 示例），不过这种方
     * 法非常丑陋，甚至低版本的 JDK 中都不能保证正确性，所以不推荐使用。
     *
     * 实际上，还有另外一种方式，同时结合了 Singleton 和 LazySingleton 的优点：高性能和延迟初始化，那就是使用静态
     * 内部类。以 StaticSingleton 为例，首先 getInstance() 方法中没有锁，这使得高并发环境下性能优越。其次只有在
     * getInstance() 方法第一次调用时，StaticSingleton 的实例才会被创建。因为这种方法巧妙地使用了内部类和类的初始
     * 化方式。内部类 SingletonHolder 被声明为 private，这使得不可能在外部访问并初始化它，只可能在 getInstance()
     * 内部对 SingletonHolder 类进行初始化，利用虚拟机的类初始化机制创建单例。
     */
    public static void main(String[] args) {

    }

}

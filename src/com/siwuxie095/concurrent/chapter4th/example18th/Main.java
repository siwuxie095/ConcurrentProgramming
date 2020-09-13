package com.siwuxie095.concurrent.chapter4th.example18th;

/**
 * @author Jiajing Li
 * @date 2020-09-13 21:06:10
 */
@SuppressWarnings("all")
public class Main {

    /**
     * Java 中的指针：Unsafe 类
     *
     * 在阅读 JUC 的原子类的源码时，不难发现一个特殊的变量 unsafe，在原子类中做 CAS 操作时，主要就是靠这个变量
     * 来进行的。unsafe 是 sun.misc.Unsafe 类型的。从名字看，这个是应该是封装了一些不安全的操作。那什么操作是
     * 不安全的呢？如果学习过 C 或 C++ 的话，就能知道指针是不安全的。这也是在 Java 中把指针去除的重要原因。如果
     * 指针指错了位置，或者计算指针偏移量时出错，结果可能是灾难性的，很有可能因为覆盖了别人的内存，导致系统崩溃。
     *
     * 而这里的 Unsafe 就是封装了一些类似指针的操作。以其中的 compareAndSwapInt() 方法为例：
     *
     * public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x);
     *
     * 它是一个 native 方法，其中的几个参数含义如下：
     * 第一个参数 o 为给定的对象，offset 为对象内的偏移量（其实就是一个字段到对象头部的偏移量，通过这个偏移量可
     * 以快速定位字段），expected 表示期望值，x 表示要设置的值。如果指定的字段的值等于 expected，那么就会把它
     * 设置为 x。
     *
     * 不难看出 compareAndSwapInt() 方法的内部，必然是使用 CAS 原子指令来完成的。此外，Unsafe 类还提供了一些
     * 方法，主要有以下几个（以 int 操作为例，其他数据类型是类似的）：
     *
     * // 获得给定对象偏移量上的 int 值
     * public native int getInt(Object o, long offset);
     * // 设置给定对象偏移量上的 int 值
     * public native void putInt(Object o, long offset, int x);
     * // 获得字段在对象中的偏移量
     * public native long objectFieldOffset(Field f);
     * // 设置给定对象的 int 值，使用 volatile 语义
     * public native void  putIntVolatile(Object o, long offset, int x);
     * // 获得给定对象的 int 值，使用 volatile 语义
     * public native int getIntVolatile(Object o, long offset);
     * // 和 putIntVolatile() 一样，但是它要求被操作字段就是 volatile 类型的
     * public native void putOrderedInt(Object o, long offset, int x);
     *
     * PS：在 ConcurrentLinkedQueue 中的 Node 里的 CAS 操作，其实也是使用了 Unsafe 类来实现。
     *
     * 不难看出，虽然 Java 抛弃了指针。但是在关键时刻，类似指针的技术还是必不可少的。这里底层的 Unsafe 实现就是
     * 最好的例子。不幸的是，JDK 的开发人员并不希望大家使用这个类。获得 Unsafe 实例的方法是调用其工厂方法 getUnsafe()。
     * 但是，它的实现却是这样：
     *
     *     private Unsafe() {}
     *
     *     private static final Unsafe theUnsafe = new Unsafe();
     *
     *     @CallerSensitive
     *     public static Unsafe getUnsafe() {
     *         Class<?> caller = Reflection.getCallerClass();
     *         if (!VM.isSystemDomainLoader(caller.getClassLoader())) {
     *             throw new SecurityException("Unsafe");
     *         } else {
     *             return theUnsafe;
     *         }
     *     }
     *
     *     public static boolean isSystemDomainLoader(ClassLoader loader) {
     *             return loader == null;
     *     }
     *
     *  在调用 getUnsafe() 时，它会检查调用 getUnsafe() 的类，如果这个类的 ClassLoader 不为 null，就直接抛
     *  出异常，拒绝工作。因此，这也使得自己的应用程序无法直接使用 Unsafe 类。它是 JDK 内部使用的专属类。
     *
     *  注意：根据 Java 类加载器的工作原理，应用程序的类由 App 类加载器加载。而系统核心类，如 rt.jar 中的类由
     *  Bootstrap 类加载器加载。Bootstrap 类加载器没有 Java 类的对象，因此，试图获得这个类加载器会返回 null。
     *  所以，当一个类的加载器为 null 时，说明它是由 Bootstrap 加载的，而这个类也极有可能是 rt.jar 中的类。
     *
     *  PS：OracleJDK 中的 Unsafe 是没有源码的，只能看反编译后的代码。在 OpenJDK 中可以看到 Unsafe 类的源码，
     *  链接如下：
     *  http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/sun/misc/Unsafe.java
     */
    public static void main(String[] args) {

    }

}

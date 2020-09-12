package com.siwuxie095.concurrent.chapter4th.example10th;

import java.util.Vector;

/**
 * @author Jiajing Li
 * @date 2020-09-12 21:46:20
 */
public class Main {

    /**
     * JVM 对锁优化所做的努力：锁消除
     *
     * 锁消除是一种更彻底的锁优化。JVM 在 JIT 编译时，通过对运行上下文的扫描，去除不可能存在共享资源竞争的锁。
     * 通过锁消除，可以节省毫无意义的请求锁时间。
     *
     * 有人对此可能会产生疑问，如果不可能存在竞争，为什么还要加锁呢？这是因为在程序开发中，必然会使用一些 JDK
     * 内置的 API，比如 StringBuffer、Vector 等。开发者在使用这些类时，也许根本不会考虑这些类到底是如何实现
     * 的。比如，很有可能在一个不可能存在并发竞争的场合使用 Vector。而众所周知的是，Vector 内部使用了内部锁，
     * 也就是 synchronized。比如下面的代码：
     *
     *     public String[] createStrings() {
     *         Vector<String> vector = new Vector<>();
     *         for (int i = 0; i < 100; i++) {
     *             vector.add(Integer.toString(i));
     *         }
     *         return vector.toArray(new String[] {});
     *     }
     *
     * 注意上述代码中的 Vector，由于变量 vector 只在 createStrings() 中使用，因此，它只是一个单纯的局部变量。
     * 局部变量是在线程栈上分配的，属于线程私有的数据，因此不可能被其他线程访问。所以，在这种情况下，Vector 内部
     * 所有的加锁同步都是没有必要的。如果 JVM 检测到这种情况，就会将这些无用的锁去除。
     *
     * 锁消除涉及的一项关键技术为逃逸分析。所谓逃逸分析就是观察某一个变量是否会逃出某一个作用域。在本例中，变量
     * vector 显然是没有逃出 createStrings() 之外的。以此为基础，JVM 才可以放心大胆地将 vector 内部的加锁操
     * 作去除。如果 createStrings() 返回的不是 String 数组，而是 vector 本身，那么就认为变量 vector 逃逸出
     * 了当前方法，也就是说 vector 有可能被其他线程访问。如果是这样，虚拟机就不能消除 vector 中的锁操作。
     *
     * 注意：逃逸分析必须在 Server 模式下进行。
     *
     * 使用如下 JVM 参数可以开启逃逸分析（启用）：
     *
     * -XX:+DoEscapeAnalysis
     *
     * 使用如下 JVM 参数可以关闭逃逸分析（禁用）：
     *
     * -XX:-DoEscapeAnalysis
     *
     * 使用如下 JVM 参数可以打印逃逸分析的结果：
     *
     * -XX:+PrintEscapeAnalysis
     *
     * 使用如下 JVM 参数可以开启锁消除（启用）：
     *
     * -XX:+EliminateLocks
     *
     * 使用如下 JVM 参数可以关闭锁消除（禁用）：
     *
     * -XX:-EliminateLocks
     *
     * PS：逃逸分析和锁消除在 JDK 8 中都是默认开启的。
     */
    public static void main(String[] args) {

    }

    public String[] createStrings() {
        Vector<String> vector = new Vector<>();
        for (int i = 0; i < 100; i++) {
            vector.add(Integer.toString(i));
        }
        return vector.toArray(new String[] {});
    }

}

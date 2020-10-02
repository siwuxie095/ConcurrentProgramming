package com.siwuxie095.concurrent.chapter7th.example1st;

/**
 * @author Jiajing Li
 * @date 2020-10-02 09:28:51
 */
public class Main {

    /**
     * 使用 Akka 构建高并发程序
     *
     * 众所周知，写出一个正确、高性能并且可扩展的并发程序是相当困难的，那么是否有一个好的框架可以帮助轻松构建
     * 这么一个应用呢？答案是肯定的，那就是 Akka。Akka 是一款遵循 Apache 2.0 许可的开源框架，这意味着可以
     * 无偿并且几乎没有限制地使用它，包括将它应用于商业环境中。
     *
     * Akka 是用 Scala 编写的，但由于 Scala 和 Java 一样，都是基于 JVM 的语言，本质上，两者并没有什么不同，
     * 所以也可以在 Java 中使用 Akka。
     *
     * 那么使用 Akka 能够带来什么好处呢？
     *
     * 首先，Akka 提供了一种称为 Actor 的并发模型，其粒度比线程更小，这意味着可以在系统中启用极其大量的 Actor。
     *
     * 其次，Akka 中提供了一套容错机制，允许在 Actor 出现异常时，进行一些恢复或者重置操作。
     *
     * 最后，通过 Akka 不仅可以在单机上构建高并发程序，也可以在网络中构建分布式程序，并提供位置透明的 Actor
     * 定位服务。
     */
    public static void main(String[] args) {

    }

}

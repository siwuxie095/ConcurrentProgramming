package com.siwuxie095.concurrent.chapter7th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 17:13:14
 */
public class Main {

    /**
     * 消息路由
     *
     * Akka 提供了非常灵活的消息发送机制。有时候，也许会使用一组 Actor 而不是一个 Actor 来提供一项服务。这一组
     * Actor 中所有的 Actor 都是对等的，也就是说可以找任何一个 Actor 来进行服务。这种情况下，如何才能快速有效
     * 地找到合适的 Actor 呢？或者说如何调度这些消息，可以使负载更为均衡地分配在这一组 Actor 中。
     *
     * 为了解决这个问题，Akka 使用一个路由器组件（Router）来封装消息的调度。系统提供了几种实用的消息路由策略，
     * 比如轮询选择 Actor 进行消息发送，随机消息发送，将消息发送给最空闲的 Actor，甚至是在组内广播消息。
     *
     * 以 Worker、Watcher 等类为例，演示了消息路由的使用方式。
     *
     * Worker 是一个普通的 Actor，用于处理不同的消息。
     *
     * Watcher 则是一个监督的 Actor，里面包含了路由组件 Router，在构造 Router 时，需要指定路由策略和一组被路
     * 由的 Actor（即 Routee）。这里使用的是 RoundRobinRoutingLogic 路由策略，也就是对所有的 Routee 进行
     * 轮询消息发送。这里的 Routee 由 5 个 Worker 构成。
     *
     * 当有消息需要传递给这 5 个 Worker 时，只需要将消息投递给这个 Router 即可。Router 会根据给定的消息路由
     * 策略进行消息投递。当一个 Worker 停止工作时，还可以简单地将其从工作组中移除。这里，如果发现系统中没有可用
     * Worker 时就会直接关闭系统。（实际上是这 5 个 Worker 都被 Watcher 通过 watch() 方法给监督了，所以发送
     * 给 Worker 的消息都会先到 Watcher，Watcher 再使用 Router 进行消息路由，分发给 Worker）。
     *
     * 在 RouteBot 的主方法中，向 Watcher 发送了大量的消息，其中夹杂着几条 CLOSE 消息，这会使得 Worker 逐一
     * 被关闭，最终程序将退出。
     *
     * 运行代码，可以看到 WORKING 消息被轮流发送给这 5 个 Worker。可以尝试修改路由策略，观察不同路由策略下的消
     * 息投递方式，路由策略有这些：
     * （1）RoundRobinRoutingLogic 轮询路由
     * （2）BroadcastRoutingLogic 广播路由
     * （3）RandomRoutingLogic 随机路由
     * （4）SmallestMailboxRoutingLogic 空闲 Actor 优先路由
     * （5）ConsistentHashingRoutingLogic 一致性哈希路由
     * ...
     * ...
     */
    public static void main(String[] args) {

    }

}

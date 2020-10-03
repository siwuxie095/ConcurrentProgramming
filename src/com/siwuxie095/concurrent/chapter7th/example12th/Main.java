package com.siwuxie095.concurrent.chapter7th.example12th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 21:37:52
 */
public class Main {

    /**
     * 多个 Actor 同时修改数据：Agent
     *
     * 在 Actor 的编程模型中，Actor 之间主要通过消息进行信息传递。所以，很少发生多个 Actor 需要访问同一个共享变量
     * 的情况。但在实际开发中，这种情况很难完全避免。那如果多个 Actor 需要对同一个共享变量进行读写时，如何保证线程
     * 安全呢？
     *
     * 在 Akka 中，使用一种叫做 Agent 的组件来实现这个功能。一个 Agent 提供了对一个变量的异步更新。当一个 Actor
     * 希望改变 Agent 的值时，它会向这个 Agent 下发一个动作（action）。当多个 Actor 同时改变 Agent 时，这些
     * action 将会在 ExecutionContext 中被并发调度执行。在任意时刻，一个 Agent 最多只能执行一个 action，对于
     * 某一个线程来说，它执行 action 的顺序与它的发生顺序一致。但对于不同线程来说，这些 action 可能会交织在一起。
     *
     * Agent 的修改可以使用两个方法 send() 或者 alter()。它们都可以向 Agent 发送一个修改动作。但是 send() 方法
     * 没有返回值，而 alter() 方法会返回一个 Future 对象便于跟踪 Agent 的执行。
     *
     * 以 Counter、AgentBot 为例，模拟这么一个场景：有 10 个 Counter，它们一起对一个 Agent 执行累加操作（这里是
     * countAgent）。每一个 Counter 累加 10_000 次，如果没有意外，那么 Agent 的最终值将是 100_000。如果 Counter
     * 间的调度出现问题，那么这个值可能小于 100_000。
     *
     * 具体是这样，先创建了 10 个 Counter 对象，并使用 Inbox 和 Counter 进行通信。Inbox 向每一个 Counter 发送
     * 1，触发 Counter 进行累加操作。然后等待所有 10 个 Counter 运行结束，此时就已经收集了所有的 Future。然后使
     * 用 Futures.sequence() 方法将所有的 Future 进行串行组合，构造了一个整体的 Future，并为它创建了一个回调方
     * 法 onComplete()。在所有的 Agent 操作执行完成后，onComplete() 方法就会被调用。在这个方法中只是简单的输出
     * 了最终的 countAgent 值，并关闭了系统。
     *
     * 运行代码，结果如下：
     *
     * countAgent: 100000
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter7th.example8th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 10:46:34
 */
public class Main {

    /**
     * 消息收件箱(Inbox)
     *
     * 已知，所有 Actor 之间的通信都是通过消息来进行的。这是否意味着必须构建一个 Actor 来控制整个系统呢？答案是
     * 否定的，并不一定要这么做，Akka 框架已经提供了一个叫做 "收件箱" 的组件，使用收件箱，可以很方便地对 Actor
     * 进行消息发送和接收，大大方便了应用程序与 Actor 之间的交互。
     *
     * 以 Worker、ActorBot 为例。
     *
     * 其中 Worker 会根据收到的消息打印自己的工作状态。当接收到 CLOSE 消息时，会关闭自己，结束运行。
     *
     * 而在 ActorBot 中则有一个收件箱 Inbox 和 Worker 进行交互。首先根据 ActorSystem 构造了一个与之绑定的收
     * 件箱 Inbox。接着使用 Inbox 通过 watch() 方法监视 Worker，这样就能在 Worker 停止后得到一个消息通知。
     * 当然，也可以使用 Inbox 向 Worker 发送消息。
     *
     * 运行代码，结果如下：
     *
     * Worker is starting
     * I am working
     * stop working
     * I will shutdown
     * Worker is stopping
     * inbox received: worker is closed
     * inbox received: worker is dead
     */
    public static void main(String[] args) {

    }

}

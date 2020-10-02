package com.siwuxie095.concurrent.chapter7th.example5th;

/**
 * @author Jiajing Li
 * @date 2020-10-02 17:33:59
 */
public class Main {

    /**
     * Actor 的生命周期
     *
     * Actor 在系统中产生后，也存在着 "生老病死" 的生命周期。Akka 框架提供了若干回调方法，利用它们，可以在 Actor
     * 的生命周期内进行一些业务相关的行为。
     *
     * Actor 的生命周期如下：
     * 一个 Actor 在 actorOf() 方法被调用后开始建立，Actor 实例被创建后，会回调 preStart() 方法。在这个方法中，
     * 可以进行一些资源的初始化工作。在 Actor 的工作过程中，可能会出现一些异常，这种情况下，Actor 会需要重启。当
     * Actor 被重启时，会回调 preStart() 方法（在老的实例上）。当新的 Actor 实例创建后，会回调 postRestart()
     * 方法，表示启动完成，同时新的实例将会代替旧的实例。停止一个 Actor 也有很多方式，可以调用 stop() 方法或者给
     * Actor 发送一个 PoisonPill（毒药丸）。Actor 停止时，postStop() 方法会被调用，同时这个 Actor 的监视者会
     * 收到一个 Terminated 消息。
     *
     * 以 Worker、Watcher 等类为例，演示了 Actor 的生命周期。
     *
     * Worker 是一个 Actor。其中重载了 preStart() 和 postStop() 两个方法。一般来说，可以使用 preStart() 方
     * 法来初始化一些资源，使用 postStop() 来进行资源的释放。这个 Actor 很简单，当它收到 WORKING 消息时，就打印
     * "I am Working"，收到 DONE 消息时，打印 "Stop working"。
     *
     * 接着，为 Worker 指定了一个监视者 Watcher，监视者就如同一个劳动监工，一旦 Worker 因为意外停止工作，监视者
     * 就会收到一个通知。Watcher 也是一个 Actor，但不同的是，它会在它的上下文中使用 watch() 方法监视一个 Actor。
     * 如果将来这个被监视的 Actor 退出终止，Watcher 就能收到一条 Terminated 消息。这里只是简单地打印 Terminated
     * 终止消息中相关 Actor 的路径，并且关闭整个 ActorSystem。
     *
     * 在 DeadBot 的主方法中，首先创建 ActorSystem 全局实例，接着创建 Worker 和 Watcher 这两个 Actor。值得注
     * 意的是，创建 Watcher 时使用了 Props.create() 方法，它的第一个参数是要创建的 Actor 的类型，第二个参数是要
     * 传递到这个 Actor 的构造方法中的参数（实际上，这里就是要调用 Watcher 的构造方法）。接着，向 Worker 先后发送
     * WORKING 和 DONE 两条消息。最后，发送了一条特殊的消息 PoisonPill。PoisonPill 就是毒药丸，它会直接毒死接收
     * 方，让其终止。
     *
     * 运行代码，结果如下：
     *
     * Worker is starting
     * I am working
     * stop working
     * Worker is stopping
     * akka://DeadBot/user/worker has terminated, shutting down system
     *
     * 可以看到，Worker 生命周期中的两个回调方法 preStart() 和 postStop() 以及消息处理方法 onReceive() 方法都
     * 被正常调用。最后一行也表明了 Watcher 正常监视到了 Worker 的终止。
     */
    public static void main(String[] args) {

    }

}

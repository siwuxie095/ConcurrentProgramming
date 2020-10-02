package com.siwuxie095.concurrent.chapter7th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-10-02 18:55:40
 */
public class Main {

    /**
     * 监督策略
     *
     * 如果一个 Actor 在执行过程中发生意外，比如没有处理某些异常，导致出错，那么这个时候应该怎么办呢？系统是应该当作什么
     * 都没发生过，继续执行，还是认为遇到了一个系统性的错误而重启 Actor 甚至它所有的兄弟 Actor 呢？
     *
     * 对于这种情况，Akka 框架给予了足够的控制权。在 Akka 框架内，父 Actor 可以对子 Actor 进行监督，监控 Actor 的行为
     * 是否有异常。大体上，监督策略可以分为两种：
     * （1）第一种是 OneForOneStrategy 的监督策略；
     * （2）第二种是 AllForOneStrategy 的监督策略；
     *
     * 对于第一种监督策略，父 Actor 只会对出问题的子 Actor 进行处理，比如重启或者停止，而对于第二种监督策略，父 Actor
     * 会对出问题的子 Actor 以及它所有的兄弟 Actor 都进行处理。显然，第二种监督策略更加适合于各个 Actor 联系非常紧密的
     * 场景。如果多个 Actor 间只要有一个 Actor 出现故障，则宣告整个任务失败，就比较适合第二种监督策略，否则，在更多的场
     * 景中，应该使用第一种监督策略。当然，第一种监督策略也是 Akka 的默认监督策略。
     *
     * 在一个指定的策略中，可以对 Actor 的失败情况进行相应的处理。比如，当失败时，可以无视这个错误，继续执行 Actor，就像
     * 什么事都没发生过一样。或者可以重启这个 Actor，甚至可以让这个 Actor 彻底停止工作。要指定这些监督行为，只要构造一个
     * 自定义的监督策略即可。
     *
     * 以 Supervisor 为例，展示了 SupervisorStrategy 的使用方式。Supervisor 是一个父 Actor，作为所有子 Actor 的监
     * 督者。其中定义了一个 OneForOneStrategy 的监督策略。在这个监督策略中，子 Actor 遇到错误后，会在 1 分钟内进行 3
     * 次重试。如果超过这个频率，那么就会直接杀死子 Actor。具体的策略定义在第三个参数，也就是代表 Function<Throwable,
     * Directive> 的 lambda 表达式。这里的定义是：
     *
     * （1）当遇到算术异常时，继续执行子 Actor，不做任何处理；
     * （2）当遇到空指针异常时，对子 Actor 进行重启；
     * （3）当遇到参数非法异常时，则直接停止子Actor；
     * （4）当遇到其他异常时，则把异常向上抛出，由更顶层的 Actor 处理。
     *
     * 然后重写了 supervisorStrategy() 方法，设置使用自定义的监督策略。
     *
     * 最后在消息处理方法 onReceive() 中新建一个名为 restartActor 的子 Actor。这个子 Actor 就由当前的 Supervisor
     * 进行监督了。当 Supervisor 接收一个 Props 对象时，就会根据这个 Props 配置生成一个 restarter。
     *
     * Restarter 中实现了一些 Actor 生命周期的回调方法。目的是更好地观察 Actor 的活动情况。然后模拟了一些异常，先抛出
     * 空指针异常，再抛出算术异常。
     *
     * 而在 ActorBot 的主方法中，首先创建了全局的 ActorSystem，接着在 customStrategy() 方法中创建了 Supervisor Actor，
     * 并且对 Supervisor 发送一个 Restarter 的 Props（这个消息会使得 Supervisor 创建 Restarter）。
     *
     * 接着，选中 Restarter 实例，向这个 Restarter 发送 100 条 RESTART 消息。这会使得 Restarter 抛出空指针异常。
     *
     * 运行代码，结果如下：
     *
     * preStart hashCode: 602022073
     * meet NullPointerException, restart
     * preRestart hashCode: 602022073
     * 22:43:07.928 [ActorBot-akka.actor.default-dispatcher-5] ERROR akka.actor.OneForOneStrategy - null
     * java.lang.NullPointerException: null
     * 	at com.siwuxie095.concurrent.chapter7th.example6th.Restarter.onReceive(Restarter.java:50)
     * 	at akka.actor.UntypedAbstractActor$$anonfun$receive$1.applyOrElse(AbstractActor.scala:332)
     * 	at akka.actor.Actor.aroundReceive(Actor.scala:537)
     * 	at akka.actor.Actor.aroundReceive$(Actor.scala:535)
     * 	at akka.actor.AbstractActor.aroundReceive(AbstractActor.scala:220)
     * 	at akka.actor.ActorCell.receiveMessage(ActorCell.scala:577)
     * 	at akka.actor.ActorCell.invoke(ActorCell.scala:547)
     * 	at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:270)
     * 	at akka.dispatch.Mailbox.run(Mailbox.scala:231)
     * 	at akka.dispatch.Mailbox.exec(Mailbox.scala:243)
     * 	at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
     * 	at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
     * 	at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
     * 	at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
     * preStart hashCode: 1790973879
     * postRestart hashCode: 1790973879
     * meet NullPointerException, restart
     * preRestart hashCode: 1790973879
     * preStart hashCode: 1561459435
     * postRestart hashCode: 1561459435
     * ... 同上，打印空指针信息
     * meet NullPointerException, restart
     * preRestart hashCode: 1561459435
     * preStart hashCode: 1989253888
     * postRestart hashCode: 1989253888
     * ... 同上，打印空指针信息
     * meet NullPointerException, restart
     * ... 同上，打印空指针信息
     * postStop hashCode: 1989253888
     *
     * 首先是 preStart 表示 Restarter 正在初始化，此时 hashCode 为 602022073。接着，这个 Actor 遇到了空指针异常。
     * 根据自定义的监督策略，会进行重启。然后就有了 preRestart，因为 preRestart 在正式重启之前调用，所以此时 hashCode
     * 还是 602022073，表示当前 Actor 和上一个 Actor 还是同一个实例。接着打印了异常信息。
     *
     * 然后再次进入 preStart，此时 hashCode 是 1790973879，这说明系统已经为 Restarter 生成了一个新的实例。原有的实
     * 例因为重启而被回收。新的实例将代替原有实例继续工作。这说明同一个 Restarter 在系统的工作始终，未必能保持同一个实例。
     * 重启完成后，调用 postRestart。实际上，Actor 重启后的 preStart 就是在 postRestart 中调用的。
     *
     * ...
     *
     * 在经过 3 次重启后，超过了监督策略中的单位时间内的重试上限。因此，系统不会再进行重试，而是直接关闭 Restarter。也就
     * 有了最后的 postStop。
     */
    public static void main(String[] args) {

    }

}

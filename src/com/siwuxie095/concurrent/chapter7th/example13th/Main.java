package com.siwuxie095.concurrent.chapter7th.example13th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 22:25:02
 */
public class Main {

    /**
     * 像数据库一样操作内存数据：软件事务内存
     *
     * 在一些函数式编程语言中，支持一种叫做软件事务内存（STM）的技术。什么是软件事务内存呢？这里的事务和数据库中所说的
     * 事务非常相似，具有原子性、一致性、隔离性。与数据库事务不同的是，内存事务不具备持久性（很显然，内存数据不会保存下
     * 来）。
     *
     * PS：STM 即 Software Transactional Memory。
     *
     * 在很多场合，某一项工作可能要由多个 Actor 协作完成。在这种协作事务中，如果一个 Actor 处理失败，那么根据事务的原
     * 子性，其他 Actor 所进行的操作必须要回滚。
     *
     * 看这样一个简单的案例。假设有一个公司要给他的员工发放福利，公司账户里有 100 元，每次公司账户会给员工账户转一笔钱，
     * 假设转账 10 元，那么公司账户中应该减去 10 元，同时，员工账户中应该增加 10 元。这两个操作必须同时完成，或者同时
     * 不完成。
     *
     * 以 Company、Employee 等类为例，模拟上述场景。
     *
     * PS：跨 Actor 的内存事务在 Akka 的后续版本中被废弃掉了，原因是在分布式环境下，无法保证内存事务。所以这里使用比
     * 较老的版本进行测试。共需要引入如下四个 jar 包：
     * （1）com.typesafe.akka » akka-actor_2.11 » 2.3.16
     * （2）org.scala-lang » scala-library » 2.11.5
     * （3）org.scala-stm » scala-stm_2.11 » 0.7
     * （4）com.typesafe.akka » akka-transactor_2.11 » 2.3.16
     * 被废弃的原因：https://groups.google.com/g/akka-user/c/3JWz-X5dbe8/m/YiV4WFG0qh0J
     *
     * Company 和 Employee 分别用于管理公司账户和员工账户。在 ActorBot 的主方法中尝试进行 19次转账，第一次转账 1
     * 元，第二次转账 2 元，以此类推，最后一次转账 19 元。每一次转账都会启动一个内存事务
     *
     * 启动一个内存事务的方法是新建一个 Coordinated 协调者，并且将这个协调者当作消息发送给 Company，协调者中包含了此
     * 次要转账的金额。然后分别向 Company 和 Employ 查询各自的余额，并进行打印。
     *
     * 当 Company 收到这个协调者消息后，自动成为这个事务的第一个成员。
     *
     * 在 Company 中，设置公司账户余额是 100。在消息处理方法 onReceive() 中判断接收的 message 是否是 Coordinated。
     * 如果是，则表示这是一个新事务的开始。从 Coordinated 中获得需要转账的金额。然后调用 Coordinated.coordinate()
     * 方法，将 Employee 也加入到当前事务中，这样这个事务中就有了两个参与者了。
     *
     * 然后调用 Coordinated.atomic() 方法，定义出原子执行块，作为这个事务的一部分。在这个执行块中，对公司账户余额进
     * 行调整。当时当转账金额大于可用余额时，就会抛出异常，宣告转账失败。
     *
     * Company 如果接收的消息是 GetBalance 字符串，则返回当前账户余额。
     *
     * 在 Employee 中，设置员工账户余额是 50。在消息处理方法 onReceive() 中判断接收的 message 是否是 Coordinated。
     * 如果是，则当前的 Actor（即 Employee）会自动加入 Coordinated 指定的事务。然后调用 Coordinated.atomic() 方
     * 法定义原子执行块，在这个执行块中，将修改员工余额。这里并没有进行异常情况的判断，只要接收到转账金额，一律将其增加到
     * 员工余额。
     *
     * Employee 如果接收的消息是 GetBalance 字符串，则返回当前账户余额。
     *
     * 那么会不会产生这种情况，如果公司账户中由于余额不足而导致转账失败，那这个员工账户中会不会照样正常增加了余额？
     *
     * 答案是不会。因为在这里，这两个 Actor 都已经加入到同一个协调事务 Coordinated 中了，所以当公司账户出现异常后，员
     * 工账户的余额就会回滚。
     *
     * 运行代码，结果如下（有省略，不重要）：
     *
     * ...
     * ...
     * companyBalance: 34
     * employeeBalance: 116
     * ===== ===== ===== ===== =====
     * companyBalance: 22
     * employeeBalance: 128
     * ===== ===== ===== ===== =====
     * companyBalance: 9
     * employeeBalance: 141
     * ===== ===== ===== ===== =====
     * java.lang.RuntimeException: balance 9 is less than amount 14
     * 	at com.siwuxie095.concurrent.chapter7th.example13th.Company.lambda$onReceive$0(Company.java:26)
     * 	at akka.transactor.Coordinated$$anonfun$atomic$1.apply(Coordinated.scala:157)
     * 	at akka.transactor.Coordinated$$anonfun$atomic$1.apply(Coordinated.scala:157)
     * 	at scala.concurrent.stm.ccstm.CommitBarrierImpl$MemberImpl$$anonfun$atomic$1.apply(CommitBarrierImpl.scala:50)
     * 	at scala.concurrent.stm.ccstm.CommitBarrierImpl$MemberImpl$$anonfun$atomic$1.apply(CommitBarrierImpl.scala:43)
     * 	at scala.concurrent.stm.ccstm.InTxnImpl.runBlock(InTxnImpl.scala:560)
     * 	at scala.concurrent.stm.ccstm.InTxnImpl.topLevelAttempt(InTxnImpl.scala:516)
     * 	at scala.concurrent.stm.ccstm.InTxnImpl.topLevelAtomicImpl(InTxnImpl.scala:387)
     * 	at scala.concurrent.stm.ccstm.InTxnImpl.atomic(InTxnImpl.scala:248)
     * 	at scala.concurrent.stm.ccstm.CCSTMExecutor.apply(CCSTMExecutor.scala:24)
     * 	at scala.concurrent.stm.ccstm.CommitBarrierImpl$MemberImpl.atomic(CommitBarrierImpl.scala:43)
     * 	at akka.transactor.Coordinated.atomic(Coordinated.scala:141)
     * 	at akka.transactor.Coordinated.atomic(Coordinated.scala:157)
     * 	at com.siwuxie095.concurrent.chapter7th.example13th.Company.onReceive(Company.java:24)
     * 	at akka.actor.UntypedActor$$anonfun$receive$1.applyOrElse(UntypedActor.scala:167)
     * 	at akka.actor.Actor$class.aroundReceive(Actor.scala:467)
     * 	at akka.actor.UntypedActor.aroundReceive(UntypedActor.scala:97)
     * 	at akka.actor.ActorCell.receiveMessage(ActorCell.scala:516)
     * 	at akka.actor.ActorCell.invoke(ActorCell.scala:487)
     * 	at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:238)
     * 	at akka.dispatch.Mailbox.run(Mailbox.scala:220)
     * 	at akka.dispatch.ForkJoinExecutorConfigurator$AkkaForkJoinTask.exec(AbstractDispatcher.scala:397)
     * 	at scala.concurrent.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
     * 	at scala.concurrent.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
     * 	at scala.concurrent.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
     * 	at scala.concurrent.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)
     * java.lang.RuntimeException: balance 9 is less than amount 15
     * 	at com.siwuxie095.concurrent.chapter7th.example13th.Company.lambda$onReceive$0(Company.java:26)
     * companyBalance: 9
     * employeeBalance: 141
     * ===== ===== ===== ===== =====
     * ...
     * ...
     *
     * 可以看到，无论转账操作是否成功，公司账户和员工账户的余额总是一致的。当转账失败时，员工账户的余额并不会增加，这就是
     * 软件事务内存的作用。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter7th.example3rd;

/**
 * @author Jiajing Li
 * @date 2020-10-02 10:32:37
 */
public class Main {

    /**
     * Akka 之 Hello World
     *
     * 以 Greeter、HelloWorld 等类为例，进一步了解 Akka 的开发。
     *
     * PS：这里使用 Akka 的 2.6.9 版本。共需要引入如下九个 jar 包：
     * （1）com.typesafe.akka » akka-actor_2.13 » 2.5.31
     * （2）com.typesafe » config » 1.3.3
     * （3）org.scala-lang » scala-library » 2.13.0
     * （4）org.scala-lang.modules » scala-java8-compat_2.13 » 0.9.0
     * （5）com.typesafe.akka » akka-actor-typed_2.13 » 2.5.31
     * 链接：https://mvnrepository.com/artifact/com.typesafe.akka
     *
     * 另：
     * （1）Akka 2.4.x 和 2.5.x 版本之间的区别：
     * https://doc.akka.io/docs/akka/2.5/project/migration-guide-2.4.x-2.5.x.html
     * （2）Akka 2.5.x 和 2.6.x 版本之间的区别：
     * https://doc.akka.io/docs/akka/current/project/migration-guide-2.5.x-2.6.x.html
     *
     * 其中定义了一个 Greeter Actor，它继承自 UntypedAbstractActor（Akka 的核心成员之一）。UntypedAbstractActor
     * 就是所说的 Actor，之所以这里强调是无类型的，那是因为在 Akka 中，支持一种有类型的 Actor，即 AbstractBehavior。
     * 有类型的 Actor 可以使用系统中的其他类型构造，可以缓解 Java 单继承的问题。因为在继承 UntypedAbstractActor 后，
     * 就不能再继承系统的其他类了。如果一定要使用系统中的其他类型构造，那就只能选择有类型的 Actor，否则，就应该选择使用
     * 无类型的 Actor，即 UntypedAbstractActor。
     *
     * 在 Greeter 中定义了消息类型的枚举 Message：GREET（欢迎）和 DONE（完成），当 Greeter 收到 GREET 消息时，就会
     * 在控制台打印 "Hello World"，并且向消息发送方发送 DONE 消息。
     *
     * 与 Greeter 交流的另外一个 Actor 是 HelloWorld。其中 preStart() 方法为 Akka 的回调方法，在 Actor 启动前，
     * 会被 Akka 框架调用完成，完成一些初始化的工作。在此方法中，创建了 Greeter 的实例，并且向它发送 GREET 消息。此时，
     * 由于创建 Greeter 时使用的是 HelloWorld 的上下文，所以 Greeter 属于 HelloWorld 的子 Actor。
     *
     * 然后另一个 onReceive() 方法是 HelloWorld 的消息处理方法。这里只处理 DONE 的消息，在收到 DONE 消息后，会再向
     * Greeter 发送 GREET 消息，接着自己停止。
     *
     * 因此，Greeter 前后会收到两条 GREET 消息，会打印两次 "Hello World"。
     *
     * 最后，看一下 GreeterBot 中的主方法。其中创建了 ActorSystem，表示管理和维护 Actor 的系统。一般来说，一个应用
     * 程序只需要一个 ActorSystem 就够用了。ActorSystem.create() 方法的第一个参数 "GreeterBot" 为系统名称，第二
     * 个参数为配置文件。
     *
     * 配置文件 greeterBot.conf 的内容如下：
     *
     * akka {
     *     loglevel = INFO
     * }
     *
     * 这里只是简单地配置了一下日志级别为 INFO。
     *
     * 然后通过 ActorSystem.actorOf() 创建了一个顶级的 Actor -- HelloWorld。
     *
     * 运行代码，输出如下：
     *
     * HelloWold Actor Path: akka://GreeterBot/user/helloWorld
     * Greeter Actor Path: akka://GreeterBot/user/helloWorld/greeter
     * Hello World
     * Hello World
     * 16:05:22.026 [GreeterBot-akka.actor.default-dispatcher-7] INFO akka.actor.RepointableActorRef
     * - Message [com.siwuxie095.concurrent.chapter7th.example3rd.Greeter$Message]
     * from Actor[akka://GreeterBot/user/helloWorld/greeter#-1932747022]
     * to Actor[akka://GreeterBot/user/helloWorld#-1418739471] was not delivered.
     * [1] dead letters encountered. If this is not an expected behavior
     * then Actor[akka://GreeterBot/user/helloWorld#-1418739471] may have terminated unexpectedly.
     * This logging can be turned off or adjusted with configuration settings
     * 'akka.log-dead-letters' and 'akka.log-dead-letters-during-shutdown'.
     *
     * 第一行打印了 HelloWorld Actor 的路径。它是系统内第一个被创建的 Actor。路径中 GreeterBot 表示 ActorSystem
     * 的系统名。接着 user 表示是用户 Actor，所有的用户 Actor 都会挂载在 user 这个路径下。接着 helloWorld 就是这个
     * Actor 的名字。
     *
     * 同理，第二行 Greeter Actor 的路径结构和 HelloWorld Actor 是完全一致的。因为 greeter 是 helloWorld 的子
     * Actor，所以是 /helloWorld/greeter。
     *
     * 第三行、第四行是 Greeter 打印的两条信息。
     *
     * 而最后一大段都是属于第五行。表示系统遇到了一条消息投递失败，失败的原因是 HelloWorld 将自己终止了，导致无法收到
     * Greeter 第二次发送的 DONE 消息无法投递。
     *
     * 可以看到，当使用 Actor 进行并行程序开发时，关注点已经不在线程上了。实际上，线程调度已经被 Akka 框架进行封装，
     * 只需要关注 Actor 对象即可。而 Actor 对象之间的交流和普通对象的方法调用有明显的区别。Actor 对象之间是通过显式
     * 的消息发送来传递信息的。
     *
     * 当系统内有多个 Actor 存在时，Akka 会自动在线程池中选择线程来执行 Actor。因此，多个不同的 Actor 有可能会被同
     * 一个线程执行，同时，一个 Actor 也有可能被不同的线程执行。值得注意的是，不要在一个 Actor 中执行耗时的代码，这样
     * 可能会导致其他 Actor 的调度出现问题。
     *
     * 参考链接：
     * （1）https://juejin.im/post/6844904192797065224
     * （2）https://cloud.tencent.com/developer/article/1435519
     */
    public static void main(String[] args) {

    }

}

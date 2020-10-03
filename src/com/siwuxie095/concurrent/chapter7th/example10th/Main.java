package com.siwuxie095.concurrent.chapter7th.example10th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 18:25:44
 */
public class Main {

    /**
     * Actor 的内置状态转换
     *
     * 在很多场景下，Actor 的业务逻辑可能比较复杂，Actor 可能需要根据不同的状态对同一条消息作出不同的处理。Akka 已经
     * 考虑到了这一点，一个 Actor 内部消息处理方法可以拥有多个不同的状态，在不同状态下，可以对同一消息进行不同的处理，
     * 状态之间也可以任意切换。
     *
     * 现在模拟一个婴儿 Actor，假设婴儿会拥有两种不同的状态，开心或生气。当带他玩时，他总是表现出开心状态，当让他睡觉时，
     * 他就会非常生气。小孩子总是拥有用不完的精力，入睡困难可能是一种通病吧。
     *
     * 在这个简单的场景模拟中，会给婴儿 Actor 发送睡觉和玩两种指令。婴儿正在生气，如果让他睡觉，他就会说 "我已经生气了"，
     * 如果让他去玩，他就会变得开心起来。同样，婴儿正在开心，如果让他继续玩，他就会说 "我已经很愉快了"，如果让他睡觉，他
     * 就会马上变得很生气。
     *
     * 以 Baby、ActorBot 为例，模拟了这个场景。
     *
     * 在 Baby 中，become() 方法用于切换 Actor 的状态。该方法接收一个 PartialFunction 参数，该参数在这里可以表示一
     * 种 Actor 的状态，同时更重要的是它封装了在这种状态下的消息处理逻辑。
     *
     * 一共定义了两个 PartialFunction，一个是 angry 生气，另一个是 happy 开心。
     *
     * 在初始状态下，Baby 既没有生气也没有开心。所以 angry 和 happy 都不会工作。当 Baby 接收到消息时，系统会调用消息
     * 处理方法 onReceive() 来处理这个消息。
     *
     * 令人吃惊的魔法就在这个 onReceive() 方法中，当处理的是 SLEEP 消息时，它就会切换当前 Actor 的状态为 angry，
     * 当处理的是 PLAY 消息时，它就会切换当前 Actor 的状态为 happy。
     *
     * 一旦完成状态切换，当后续有新的消息送达时，就不会再由 onReceive() 方法处理了。由于 angry 和 happy 本身就是消息
     * 处理方法，所以后续的消息就直接交由当前状态（angry 或者 happy）处理，从而很好地封装了 Actor 的多个不同处理逻辑。
     *
     * 在 ActorBot 的主方法中向 Baby 发送了几条 PLAY 和 SLEEP 的消息。运行代码，结果如下（省略最后的终止信息）：
     *
     * onReceive: PLAY
     * happyApply: SLEEP
     * I don't want to sleep
     * angryApply: PLAY
     * I like playing
     * happyApply: PLAY
     * I am already happy :-)
     *
     * 可以看到，当第一个 PLAY 消息到来时，是由 onReceive() 方法进行处理的，在该方法中切换状态为 happy。因此当 SLEEP
     * 消息到达时，由 happy 进行处理，接着 Actor 切换状态为 angry。当 PLAY 消息再次到达时，由 angry 进行处理。下面
     * 同理。
     *
     * 由此可见，Akka 为 Actor 提供了灵活的状态切换机制，由于处于不同状态的 Actor 可以绑定不同的消息处理方法（或消息
     * 处理逻辑）来进行消息处理，这对构造结构化应用有着重要的帮助。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter7th.example11th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 19:43:58
 */
public class Main {

    /**
     * 询问模式：Actor 中的 Future
     *
     * 由于 Actor 之间都是通过异步消息通信的。当发送一条消息给一个 Actor 后，通常只能等待 Actor 的返回。与同步方法不同，
     * 在发送异步消息后，接收消息的 Actor 可能还根本来不及处理你的消息，而调用方就已经返回了。
     *
     * 这种模式和 Future 模式非常相像。不同之处只是在传统的异步调用中，进行的是方法调用，而在这里是发送了一条消息。
     *
     * 因为两者的行为方式如此相像，很自然就会想到，当需要一个有返回值的调用时，Actor 是不是也应该返回一个契约（Future）
     * 呢？这样，就算当下没有办法立即获得 Actor 的处理结果，在将来，通过这个契约还是可以追踪到之前的请求。
     *
     * 以 Worker、Printer 为例，进行说明。
     *
     * Worker 中可以接收一个整数，并计算它的平方，给予返回。其中模拟了一个耗时的操作。
     *
     * Printer 中则只是进行简单的输出。
     *
     * ActorBot 中的主方法则给出了两处在 Actor 交互中使用 Future 的例子。
     *
     * 首先使用 Patterns.ask() 方法给 Worker 发送消息，消息内容是 5，也就是说 Worker 会接收到一个 Integer 消息，值
     * 为 5。当 Worker 接收到消息后，就可以进行计算处理，并且将结果返回给发送者，当然，这个处理过程可能需要花费一点时间。
     *
     * Patterns.ask() 方法不会等待 Worker 处理，会立即返回一个 Future 对象。所以需要使用 Await.result() 方法等待
     * Worker 的返回，并打印返回结果。
     *
     * 这里实际上间接的将一个异步非阻塞调用转为了同步阻塞调用。虽然比较容易理解，但是在有些场合可能会出现性能问题。另外一
     * 种更为有效的方式是使用 Patterns.pipe() 方法。
     *
     * 再次使用 Patterns.ask() 方法给 Worker 发送消息，消息内容是 6。接着并不进行等待，而是使用 Patterns.pipe() 方
     * 法将这个 Future 重定向到另外一个称为 Printer 的 Actor。Patterns.pipe() 不会阻塞程序，会立即返回。
     *
     * 运行代码，结果如下：
     *
     * Worker is starting
     * return:25
     * Worker is stopping
     * Printer: 36
     */
    public static void main(String[] args) {

    }

}

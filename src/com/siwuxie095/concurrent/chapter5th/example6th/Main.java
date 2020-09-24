package com.siwuxie095.concurrent.chapter5th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-23 08:09:05
 */
public class Main {

    /**
     * Future 模式
     *
     * Future 模式是多线程开发中非常常见的一种设计模式，它的核心思想是异步调用。当需要调用一个函数方法时，如果这个函数
     * 执行很慢，那么就要进行等待。但有时候，可能并不急着要结果。因此可以让被调用者立即返回，让它在后台慢慢处理这个请求。
     * 对于调用者来说，则可以先处理一些其他任务，在真正需要数据的场合再去尝试获得需要的数据。
     *
     * Future 模式有点类似于在网上买东西。如果在网上下单买了一个手机，当支付完成后，手机并没有办法立即送到家里，但是电
     * 脑上会立即产生一个订单。这个订单就是将来发货或者领取手机的重要凭证，这个凭证也就是 Future 模式中会给出的一个契约。
     * 在支付活动结束后，自然不会傻傻地等着手机到来，而是可以忙其他事情。而这张订单就成为了商家配货、发货的驱动力。当然，
     * 这一切并不需要买家关心，买家需要做的就是在快递上门时，开门拿货而已。
     *
     * 对于 Future 模式来说，虽然它无法立即给出你需要的数据。但是它会返回一个契约，将来就可以凭借这个契约去重新获取所需
     * 要的信息。
     *
     * 传统的同步方法，调用一段比较耗时的程序的过程是这样：客户端发出 call 请求，这个请求需要相当长一段时间才能返回。客
     * 户端一直等待，直到数据返回，随后，再进行其他任务的处理。
     *
     * 使用 Future 模式则可以改进调用过程：客户端发出 call 请求，虽然 call 本身仍然需要很长一段时间进行处理。但是服务
     * 端可以不等数据处理完成便立即返回客户端一个伪造的数据（也就是一个契约。对应到购物：相当于商品订单，而不是商品本身）。
     * 实现了 Future 模式的客户端在拿到这个返回结果后，并不急着对其进行处理，而去调用其他的业务逻辑，充分利用了等待时间。
     * 这就是 Future 模式的核心所在。在完成了其他业务逻辑的处理后，最后再使用返回比较慢的 Future 数据。这样，在整个调用
     * 过程中，就不存在无谓的等待，充分利用了所有的时间片段，从而提高系统的响应速度。
     *
     *
     *
     * Future 模式的主要角色
     *
     * 以 Main、Client、Data、FutureData、RealData 等类为例，实现一个非常简单的 Future 模式，以认识 Future 模式的
     * 基本结构。其中的主要参与者如下所示：
     *
     * （1）Main：系统启动，调用 Client 发出请求。
     * （2）Client：返回 Data 对象，立即返回 FutureData，并开启 ClientThread 线程装配 RealData。
     * （3）Data：返回数据的接口。
     * （4）FutureData：Future 数据，构造很快，但是是一个虚拟的数据，需要装配 RealData。
     * （5）RealData：真实数据，其构造是很慢的。
     *
     *
     *
     * Future 模式的简单实现
     *
     * 在这个实现中，有一个核心接口 Data，这就是客户端希望获取的数据。在 Future 模式中，这个 Data 接口有两个重要的实现。
     * 一个是 RealData，也就是真实数据，这就是最终需要获得的、有价值的信息。另一个是 FutureData，它就是用来提取 RealData
     * 的一个契约（对应到购物中，就是一个订单）。因此，FutureData 是可以立即返回得到的。
     *
     * FutureData 实现了一个快速返回的 RealData 包装。它只是一个包装，或者说是 RealData 的虚拟实现。因此，它可以很快
     * 被构造并返回。当使用 FutureData 的 getResult() 方法时，如果实际的数据没有准备好，那么程序就会阻塞，等待 RealData
     * 准备好并注入到 FutureData 中，才最终返回数据。
     *
     * 注意：FutureData 是 Future 模式的关键。它实际上是真实数据 RealData 的代理，封装了获取 RealData 的等待过程。
     *
     * RealData 是最终需要使用的数据模型。它的构造很慢。这里使用 sleep() 方法简单地模拟一个耗时的字符串构造。
     *
     * 接下来就是客户端程序，Client 主要实现了获取 FutureData，并开启构造 RealData 的线程。并在接受请求后，很快的返回
     * FutureData。注意，它不会等待真实数据真的构造完毕再返回，而是立即返回 FutureData，即使这个时候 FutureData 内并
     * 没有真实数据。
     *
     * 最后就是主方法 Main，它主要负责调用 Client 发起请求，并消费返回的数据。
     *
     *
     *
     * JDK 中的 Future 模式
     *
     * Future 模式如此常用，因此 JDK 内部已经准备好了一套完整的实现。显然，这个实现要比前面提出的方案复杂得多。下面针对
     * JDK 中 Future 模式进行简单的介绍。
     *
     * JDK 中 Future 模式的基本结构如下：
     * （1）Future 接口：类似于前面描述的契约或订单。通过它，可以得到真实的数据。
     * （2）RunnableFuture 接口：继承了 Runnable 和 Future 两个接口，其中 run() 方法用于构造真实的数据。
     * （3）FutureTask 类：实现自 RunnableFuture 接口。可以传入一个 Callable 对象或一个 Runnable 对象加指定结果。
     *
     * Callable 接口只有一个 call() 方法，它会返回需要构造的实际数据。这个 Callable 接口也是 Future 框架和应用程序
     * 之间的重要接口。如果要实现自己的业务系统，通常需要实现自己的 Callable 对象。此外，FutureTask 类也与应用密切相关。
     * 通常，会使用 Callable 对象构造一个 FutureTask，并将它提交给线程池。
     *
     * PS：FutureTask 如果传入的是一个 Runnable 对象加指定结果，那么会自动将 Runnable 对象加指定结果适配成 Callable
     * 对象（做了一层转换与包装）。当该 Runnable 执行完毕时，会返回指定结果。
     *
     * 以 JdkRealData 和 JdkMain 为例，展示 FutureTask 的使用方式。
     *
     * JdkRealData 实现了 Callable 接口，它的 call() 方法会构造所需要的真实数据并返回。当然，这个过程可能是缓慢的，
     * 这里使用 Thread.sleep() 进行模拟。
     *
     * JdkMain 中则是使用 Future 模式的典型。先是构造了 FutureTask 对象，表示这个任务有返回值。构造 FutureTask 时，
     * 使用 Callable 接口，告诉 FutureTask 需要的数据如何产生。接着将 FutureTask 提交给线程池。显然，作为一个简单的
     * 任务提交，这里必然是立即返回的，因此程序不会阻塞。接下来，不用关心数据是如何产生的。可以去做一些额外的事情，然后在
     * 需要的时候可以通过 Future.get() 方法得到实际的数据。
     *
     * 除了基本的功能外，JDK 还为 Future 接口提供了一些简单的控制功能，如下：
     *
     * boolean cancel(boolean mayInterruptIfRunning);                           // 取消任务
     * boolean isCancelled();                                                   // 是否已经取消
     * boolean isDone();                                                        // 是否已完成
     * V get() throws InterruptedException, ExecutionException;                 // 取得返回对象
     * V get(long timeout, TimeUnit unit)                                       // 取得返回对象，可以设置超时时间
     *     throws InterruptedException, ExecutionException, TimeoutException;
     */
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        // 这里会立即返回，因为得到的是 FutureData 而不是 RealData
        Data data = client.request("DIY ");
        System.out.println("请求完毕");
        /*
         * 这里用一个 sleep 代替了对其他业务逻辑的处理。在处理这些业务逻辑过程中，
         * RealData 被创建，从而充分利用了等待时间
         */
        Thread.sleep(2000);
        // 等待获取真实的数据
        System.out.println("数据 = " + data.getResult());
    }

}

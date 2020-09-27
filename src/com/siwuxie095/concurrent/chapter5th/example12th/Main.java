package com.siwuxie095.concurrent.chapter5th.example12th;

/**
 * @author Jiajing Li
 * @date 2020-09-27 08:31:14
 */
public class Main {

    /**
     * 读写完了再通知我：AIO
     *
     * AIO 是 Asynchronous IO 的简称，即 异步 IO。虽然 NIO 在网络操作中，提供了非阻塞的方法，但是 NIO 的 IO 行为
     * 还是同步的。对于 NIO 来说，业务线程是在 IO 操作准备好时，得到通知，接着就由这个线程自行进行 IO 操作，IO 操作
     * 本身还是同步的。
     *
     * 但对于 AIO 来说，则更加进了一步，它不是在 IO 操作准备好时再通知线程，而是在 IO 操作已经完成后，再给线程发出通
     * 知。因此，AIO 是完全不会阻塞的。此时，业务逻辑将变成一个回调方法，等待 IO 操作完成后，由系统自动触发。
     *
     * 下面将通过 AIO 实现一个简单的 Echo 服务器以及对应的客户端。
     *
     * PS：下面的整体内容都是网络 IO，没有涉及到文件 IO。
     *
     * 另：BIO、NIO、AIO 的异同。
     * （1）BIO，即 Blocking IO，阻塞 IO（同步阻塞）。
     * （2）NIO，即 Non-blocking IO，非阻塞 IO（同步非阻塞）。
     * （3）AIO，即 Asynchronous IO，异步 IO（异步非阻塞）。
     *
     *
     *
     * AIO Echo 服务器的实现
     *
     * 以 AioEchoServer 为例，来进行说明。异步 IO 需要使用异步通道（AsynchronousServerSocketChannel），所以这
     * 里使用 AsynchronousServerSocketChannel 作为服务器，变量名为 server，并绑定 8000 端口为服务器端口。然后
     * 使用这个 server 来进行客户端的接收和处理。
     *
     * 在 start() 方法中开启了服务器。值得注意的是，这个方法中除了一个打印语句外，就只调用了一个方法 server.accept()。
     * 中间的大段代码都是这个 accept() 方法的参数。
     *
     * AsynchronousServerSocketChannel.accept() 方法会立即返回。它并不会真的去等待客户端的到来。accept() 方法
     * 的签名如下：
     *
     * public abstract <A> void accept(A attachment,
     *                                 CompletionHandler<AsynchronousSocketChannel,? super A> handler);
     *
     * 它的第一个参数是一个附件，可以是任意类型，作用是让当前线程和后续的回调方法可以共享信息，它会在后续调用中，传递给
     * handler。它的第二个参数是 CompletionHandler 接口。这个接口有两个方法：
     *
     * void completed(V result, A attachment);
     * void failed(Throwable exc, A attachment);
     *
     * 这两个方法分别在异步操作 accept() 成功或者失败时被回调。
     *
     * 因此 AsynchronousServerSocketChannel.accept() 实际上做了两件事，第一是发起 accept 请求，告诉系统可以开始
     * 监听端口了。第二是注册 CompletionHandler 实例，告诉系统一旦有客户端前来连接，如果连接成功，就去执行 completed()
     * 方法；如果连接失败，就去执行 failed() 方法。
     *
     * 所以 server.accept() 方法不会阻塞，它会立即返回。
     *
     * 下面来看一下 CompletionHandler.completed() 的实现。当 completed() 被执行时，意味着已经有客户端成功连接了。
     * 然后使用 read() 方法读取客户的数据。这里要注意，AsynchronousSocketChannel.read() 方法也是异步的，换句话说，
     * 它不会等待读取完成了再返回，而是立即返回，返回的结果是一个 Future，因此这里就是 Future 模式的典型应用。为了编
     * 程方便，这里直接调用 Future.get() 方法，进行等待，将这个异步方法变成了同步方法。所以这一行执行完成后，数据读取
     * 就已经完成了。
     *
     * 之后，将数据回写给客户端。这里调用的是 AsynchronousSocketChannel.write() 方法。这个方法不会等待数据全部写完，
     * 也是立即返回的。同样，它返回的也是 Future 对象。
     *
     * 再之后，在 finally 块中，服务器进行下一个客户端连接的准备。同时关闭当前正在处理的客户端连接。但在关闭之前，得先
     * 确保之前的 write() 操作已经完成，因此，使用 Future.get() 方法进行等待。
     *
     * 接下来，就只需要在主方法中调用这个 start() 方法即可开启服务器。但由于 start() 方法里使用的都是异步方法，因此，
     * 它会马上返回，并不会像阻塞方法那么会进行等待。因此，如果想让程序驻守执行，那么等待语句就是必需的（如下）。否则，
     * 在 start() 方法结束后，不等客户端到来，程序已经运行完成，主线程就将退出。
     *
     * while (true) {
     *      Thread.sleep(1000);
     * }
     *
     *
     *
     * AIO Echo 客户端的实现
     *
     * 在服务器的实现中，使用 Future.get() 方法将异步转为了一个同步等待。在客户端的实现里，将全部使用异步回调实现。
     *
     * 以 AioClient 为例，整个代码段看起来很长，但实际上只有三个语句。
     *
     * （1）第一个语句是打开 AsynchronousSocketChannel 通道。
     * （2）第二个语句是让客户端去连接指定的服务器，并注册了一系列事件。
     * （3）第三个语句是让线程进行等待。
     *
     * 第二个语句看起来很长，但是它完全是异步的，因此会很快返回，并不会等待在连接操作的过程中。如果不进行等待，客户端
     * 会马上退出，也就无法进行工作了。
     *
     * 在第二个语句中，客户端进行网络连接，并注册了连接成功的回调方法 CompletionHandler<Void, Object>。待连接成功
     * 后，就会进行数据写入，向服务器发送数据。这个过程也是异步的，会很快返回。写入完成后，会通知回调方法
     * CompletionHandler<Integer, Object>。然后开始准备进行数据读取，从服务器读取回写的数据。当然，这个 read()
     * 方法也是立即返回的，成功读取所有数据后，会通知回调方法 CompletionHandler<Integer, ByteBuffer>，打印接收到
     * 的数据。
     */
    public static void main(String[] args) {

    }

}

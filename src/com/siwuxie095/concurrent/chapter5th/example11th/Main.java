package com.siwuxie095.concurrent.chapter5th.example11th;

/**
 * @author Jiajing Li
 * @date 2020-09-26 16:16:40
 */
public class Main {

    /**
     * 准备好了再通知我：NIO
     *
     * Java NIO 是 New IO 的简称（也作 Non-blocking IO，非阻塞 IO），它是一种可以替代 Java IO 的一套新的 IO
     * 机制。它提供了一套不同于 Java 标准 IO 的操作机制。严格来说，NIO 与并发并无直接关系，但是，使用 NIO 技术可以
     * 大大提高线程的使用效率。Java NIO 中涉及的基础内容有通道（Channel）和缓冲区（Buffer）、文件 IO 和网络 IO。
     *
     * 对于标准的网络 IO 来说，会使用 Socket 进行网络的读写。为了让服务器可以支持更多的客户端连接，通常的做法是为每
     * 一个客户端连接开启一个线程。
     *
     * PS：下面的整体内容都是网络 IO，没有涉及到文件 IO。
     *
     * 另：标准 IO 或者普通 IO 或者传统 IO，也叫做 BIO，是 Blocking IO 的简称，即 阻塞 IO。
     *
     *
     *
     * 基于 Socket 的服务端的多线程模式
     *
     * 这里，以一个简单的 Echo 服务器来进行说明。对于 Echo 服务器，它会读取客户端的一个输入，并将这个输入原封不动
     * 的返回给客户端。这看起来很简单，但是麻雀虽小五脏俱全，为了完成这个功能，服务器还是需要有一套完整的 Socket
     * 处理机制。因此，这个 Echo 服务器非常适合用来学习。实际上，任务业务逻辑简单的系统都很适合学习，这样就不用为了
     * 去理解业务上的复杂功能而忽略了系统的重点。
     *
     * 以 MultiThreadEchoServer 为例，服务器会为每一个客户端连接启用一个线程，这个新的线程将全心全意为这个客户端
     * 服务。同时，为了接受客户端连接，服务器还会额外使用一个派发线程（这里即主线程）。
     *
     * 其中使用了一个线程池来处理每一个客户端连接。然后定义了 HandleMsg 线程，它由一个客户端 Socket 构造而成，它
     * 的任务是读取这个 Socket 的内容并将其进行返回，返回成功后，任务完成，客户端 Socket 就被正常关闭。HandleMsg
     * 中还统计并输出了服务端线程处理一次客户端请求所花费的时间（包括读取数据和回写数据的时间）。主线程 main 的主要
     * 作用是在 8000 端口上进行等待。一旦有新的客户端连接，它就根据这个连接创建 HandleMsg 线程进行处理。
     *
     * 这就是一个支持多线程的服务端的核心内容。它的特点是，在相同可支持的线程范围内，可以尽量多地支持客户端的数量，
     * 同时和单线程服务器相比，它也可以更好地使用多核 CPU。
     *
     * 以 SocketClient 为例，是一个简单的客户端的实现。先是连接了服务器的 8000 端口，并发送字符串。接着在第 12 行，
     * 读取服务器的返回信息并进行输出。
     *
     * 这种多线程的服务器开发模式是极其常用的。对于绝大多数应用来说，这种模式可以很好的工作。但是，如果想让程序工作得
     * 更加有效，就必须知道这种模式的一个重大弱点 -- 那就是它倾向于让 CPU 进行 IO 等待。
     *
     * 以 HeavySocketClient 为例，定义了一个新的客户端，可以说明这种模式的弱点。它会进行 10 次请求，每一次请求都会
     * 访问 8000 端口。连接成功后，会向服务器输出 "Hello!" 字符串，但是在这一次交互中，客户端会慢慢地进行输出，每次
     * 只输出一个字符，之后进行 1 秒的等待。所以整个过程会持续 6 秒多。
     *
     * 运行代码，输出如下：
     *
     * spend: 6030 ms
     * spend: 6031 ms
     * spend: 6032 ms
     * spend: 6033 ms
     * spend: 6032 ms
     * spend: 6034 ms
     * spend: 6035 ms
     * spend: 6034 ms
     * spend: 6036 ms
     * spend: 6037 ms
     *
     * 可以看到，对于服务端来说，每一个请求的处理时间都在 6 秒多。这很容易理解，因为服务器要先读入客户端的输入，而客户
     * 端缓慢的处理速度（当然也可能是一个拥塞的网络环境）使得服务器花费了不少等待时间。
     *
     * 试想一下，如果服务器要处理大量的请求连接，每个请求如果都像这样拖慢了服务器的处理速度，那么服务端能够处理的并发数
     * 量就会大幅度减少。反之，如果服务器每次都能很快地处理一次请求，那么相对的，它的并发能力就能上升。
     *
     * 在本例中，服务器处理请求之所以慢，并不是因为服务端有多少繁重的任务，而仅仅是因为服务线程在等待 IO 而已，让高速
     * 运转的 CPU 去等待极其低效的网络 IO 是非常不合算的行为。那么，是不是可以想一个办法，将网络 IO 的等待时间从线程
     * 中分离出来呢？
     *
     *
     *
     * 使用 NIO 进行网络编程（使用 NIO 实现服务器）
     *
     * 使用 Java 的 NIO 就可以将上面的网络 IO 等待时间从业务处理线程中抽取出来。那么 NIO 是什么？它又是如何工作的呢？
     *
     * 要了解 NIO，首先需要知道在 NIO 中的一个关键组件 Channel（通道）。Channel 有点类似于流，一个 Channel 可以和
     * 文件或者网络 Socket 对应。如果 Channel 对应这一个 Socket，那么往这个 Channel 中写数据，就等同于向 Socket
     * 中写入数据。
     *
     * 和 Channel 一起使用的另外一个非常重要的组件就是 Buffer（缓冲区）。Buffer 可以简单理解成一个内存区域或者 byte
     * 数组。数据需要包装成 Buffer 的形式才能和 Channel 交互（写入或者读取）。
     *
     * 另外一个与 Channel 密切相关的是 Selector（选择器）。在 Channel 的众多实现中，有一个 SelectableChannel 实
     * 现，表示可被选择的通道。任何一个 SelectableChannel 都可以将自己注册到一个 Selector 中。这样，这个 Channel
     * 就能被 Selector 所管理。而一个 Selector 可以管理多个 SelectableChannel。当 SelectableChannel 的数据准备
     * 好时，Selector 就会接到通知，得到那些以及准备好的数据。而 SocketChannel 就是 SelectableChannel 的一种。
     *
     * 一个 Selector 可以由一个线程进行管理，而一个 SocketChannel 则可以表示一个客户端连接，因此这就构成由一个或者极
     * 少数线程，来处理大量客户端连接的结构。当与客户端连接的数据没有准备好时，Selector 会处于等待状态（不过幸好，用于
     * 管理 Selector 的线程数是极少量的），而一旦由任何一个 SocketChannel 准备好了数据，Selector 就能立即得到通知，
     *获取数据进行处理。
     *
     * 以 NioEchoServer 为例，就是使用 NIO 来重新构造的多线程的 Echo 服务器。先是定义了一个 Selector 和线程池。这个
     * Selector 用于处理所有的网络连接。而线程池则用于对每一个客户端进行响应的处理，每一个请求都会委托给线程池中的线程进
     * 行实际的处理。
     *
     * 为了能够统计服务器线程在每一个客户端上花费了多少时间，这里还定义了一个与时间统计有关的变量 timeStatistic。它
     * 用于统计在某一个 socket 上花费的时间。key 为 Socket，value 为时间戳（可以记录处理开始时间）。
     *
     * startServer() 方法用于启用 NIO Server。先是通过工厂方法获得一个 Selector 对象的实例。然后获得表示服务端的
     * ServerSocketChannel 实例，并将该实例设置为非阻塞模式。实际上，Channel 也可以像传统的 Socket 那么按照阻塞
     * 的方式工作。但在这里，更倾向于让其工作在非阻塞模式。在这种模式下，才可以向 Channel 注册感兴趣的事件，并且在数
     * 据准备好时，得到必要的通知。接着将这个 Channel 绑定在 8000 端口。
     *
     * 然后将这个 ServerSocketChannel 注册到 Selector 上，并设置它感兴趣的事件为接收操作(OP_ACCEPT)。这样，Selector
     * 就能为这个 Channel 服务。当 Selector 发现 ServerSocketChannel 有新的客户端连接时，就会通知 ServerSocketChannel
     * 进行处理。方法 register() 的返回值是一个 SelectionKey，SelectionKey 表示一对 Selector 和 Channel 的关
     * 系。当 Channel 注册到 Selector 上时，就相当于确立了两者的服务关系，SelectKey 就是这个契约。当 Selector
     * 或者 Channel 被关闭时，它们对应的 SelectionKey 就会失效。
     *
     * 接着，就是一个无穷循环，它的主要任务就是等待和分发网络消息。其中 select() 方法是一个阻塞方法，如果当前没有任何
     * 数据准备好，它就会等待。一旦有数据可读，它就会返回。它的返回值是已经准备就绪的 SelectionKey 的数量。这里简单
     * 地将其忽略。
     *
     * 然后获取那些准备好的 SelectionKey。因为 Selector 同时为多个 Channel 服务，因此已经准备就绪的 Channel 就有
     * 可能是多个。所以，这里得到的自然是一个集合。得到这个就绪集合后，剩下的就是遍历这个集合，挨个处理所有的 Channel
     * 数据。
     *
     * 然后得到这个集合的迭代器。使用迭代器遍历整个集合。根据迭代器获得一个集合内的 SelectionKey 实例。获得该实例后，
     * 需要将该实例从这个集合中移除。注意，这个很重要，否则就会重复处理相同的 SelectionKey。
     *
     * 然后判断当前 SelectionKey 所代表的 Channel 如果是在 Acceptable 状态，就进行客户端的接收 doAccept()。如果
     * 所代表的 Channel 是在 Readable 状态，就进行读取 doRead()。这里为了统计系统处理每一个 Socket 连接的时间，就
     * 记录了在读取数据之前的一个时间戳。如果所代表的 Channel 是在 Writable 状态，就进行写入 doWrite()，同时在写入
     * 完成后，根据读取前的时间戳，输出处理这个 Socket 连接的耗时。
     *
     * 以上就是服务端的整体框架。下面是几个主要方法的内部实现。
     *
     * 首先是 doAccept() 方法，它与客户端建立连接。和 Socket 编程很类似，当有一个新的客户端连接接入时，就会有一个新的
     * Channel 产生代表这个连接。这里 SocketChannel clientSocketChannel 表示和客户端通信的通道。然后将这个 Channel
     * 配置为非阻塞模式，也就是要求系统在准备好 IO 后，再通知线程来读取或者写入。
     *
     * 然后将新生成的 Channel 注册到 Selector 上，并设置感兴趣的事件为读取操作(OP_READ)。这样，当 Selector 发现这个
     * Channel 已经准备好读时，就能给线程一个通知。
     *
     * 然后新建一个 EchoClient 的对象实例，一个 EchoClient 实例代表一个客户端。这里将这个客户端实例作为一个附件，附加
     * 到表示这个连接的 SelectionKey 上。这样在整个连接的处理过程中，都可以共享这个 EchoClient 实例。
     *
     * EchoClient 的定义很简单，它封装了一个队列，保存需要回复给这个客户端的所有信息。这样，再进行回复时，只要从队列中
     * 弹出元素即可。
     *
     * 再来看 doRead() 方法，当 Channel 可以读取时，它就会被调用。doRead() 方法接收一个 SelectionKey 参数，通过这
     * 个 SelectionKey 可以得到当前的客户端 Channel。这里准备了 8K 的缓冲区读取数据，所有读取的数据存放在缓冲区中。
     * 读取完成后，重置缓冲区（调用 flip() 方法），为数据处理做准备。
     *
     * 本例中，虽然对数据的处理很简单，但为了模拟复杂的场景，还是使用了线程池进行处理数据。这样，如果数据处理很复杂，就能
     * 在单独的线程 HandleMsg 中进行，而不用阻塞任务派发线程。
     *
     * HandleMsg 的实现很简单，将接收到的数据压入 EchoClient 的队列，如果需要处理业务逻辑，就可以在这里进行处理。在
     * 数据处理完成后，就可以准备将结果回写到客户端。因此重新设置感兴趣的事件为读取操作和写入操作(OP_READ | OP_WRITE)，
     * 这样在 Channel 准备好写入时，就能通知线程。
     *
     * 再来看 doWrite() 方法，它也接收一个 SelectionKey 作为参数。当然，针对一个客户端来说，这个 SelectionKey 实例
     * 和 doRead() 拿到的 SelectionKey 实例是同一个。所以通过 SelectionKey 就可以在这两个操作中共享 EchoClient
     * 实例。然后取得 EchoClient 实例以及它的发送内容列表。获得列表的顶部元素，准备写回客户端。然后进行写回操作，如果
     * 全部发送完成，则移除这个缓存队列。
     *
     * 在 doWrite() 中最重要的，也是最容易被忽略的是在全部数据发送完成后（队列长度为 0），需要将写入操作从感兴趣的事件中
     * 移除。如果不这么做，每次 Channel 准备好写时，都会来执行 doWrite() 方法。而实际上，又无数据可写，这显然是不合理
     * 的。
     *
     * 现在使用这个 NIO 服务器来处理 HeavySocketClient 的访问。同样的，客户端也是要花费 6 秒多才能完成一次消息的发送，
     * 但是使用 NIO 技术后的服务端线程则无须花费什么时间。运行代码，服务端耗时如下：
     *
     * spend: 1 ms
     * spend: 1 ms
     * spend: 1 ms
     * spend: 1 ms
     * spend: 0 ms
     * spend: 0 ms
     * spend: 0 ms
     * spend: 0 ms
     * spend: 1 ms
     * spend: 0 ms
     *
     * 可以看到，在使用 NIO 技术后，即使客户端出现迟钝或者出现了网络延迟等现象，并不会给服务器带来太大的问题。
     *
     *
     *
     * 使用 NIO 来实现客户端
     *
     * 在上文中，使用 Socket 编程来构建客户端，使用 NIO 来实现服务器。实际上，使用 NIO 也可以用来创建客户端。
     *
     * 以 NioClient 为例，就是使用 NIO 来重新构造的客户端。和构造服务器类似，核心元素也是 Selector、Channel
     * 和 SelectionKey。
     *
     * 首先是 init() 方法初始化 Selector 和 Channel。先是创建一个 SocketChannel 实例，并设置为非阻塞模式，
     * 然后创建一个 Selector，将 Channel 绑定到对应的端口上，由于当前 Channel 是非阻塞的，所以 Channel 的
     * connect() 方法返回时，连接并不一定建立成功，在后续使用这个连接时，还需要是 finishConnect() 再次确认。
     * 最后将这个 Channel 注册到 Selector 上，并设置感兴趣的事件为连接操作(OP_CONNECT)。
     *
     * 初始化完成后，就是程序的主要执行逻辑 working() 方法。通过 select() 得到已经准备好的事件。如果当前没有
     * 任何事件准备就绪，这里就会阻塞。这里的处理机制和服务器非常类似，主要处理两个事件，首先是表示连接就绪的
     * Connect 事件（由 connect() 方法处理）以及表示通道可读的 Read 事件（由 read() 方法处理）。
     *
     * connect() 方法接收 SelectionKey 作为参数，首先判断是否连接已经建立，如果没有，则调用 finishConnect()
     * 完成连接。建立连接后，向 Channel 写入数据，并同时设置读取操作为感兴趣的事件。
     *
     * 当 Channel 可读时，会执行 read() 方法，进行数据读取。先是创建了 100 字节的缓冲区，接着从 Channel 中
     * 读取数据，并将其打印在控制台。最后，关闭 Channel 和 Selector。
     */
    public static void main(String[] args) {

    }

}

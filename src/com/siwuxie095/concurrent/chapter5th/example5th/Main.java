package com.siwuxie095.concurrent.chapter5th.example5th;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-21 07:55:57
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 高性能的生产者-消费者：无锁的实现
     *
     * BlockingQueue 是用于实现生产者和消费者的一个不错的选择。它可以很自然地实现作为生产者和消费者的内存缓冲区。
     * 但是 BlockingQueue 并不是一个高性能的实现，它完全使用锁和阻塞等待来实现线程间的同步。在高并发场合，它的
     * 性能并不是特别的优越。ConcurrentLinkedQueue 是一个高性能的队列，而 BlockingQueue 只是为了方便数据共享。
     *
     * 而 ConcurrentLinkedQueue 的秘诀就在于大量使用了无锁的 CAS 操作。同理，如果使用 CAS 来实现生产者-消费者
     * 模式，也同样可以获得可观的性能提升。然而众所周知的是，使用 CAS 进行编程非常困难。但是不用担心，实际上目前有
     * 一个现成的 Disruptor 框架（也是一个高性能的队列），已经实现了这一功能。
     *
     *
     *
     * 无锁的缓存框架：Disruptor
     *
     * Disruptor 框架是由 LMAX 公司开发的一款高效的无锁内存队列。它使用无锁的方式实现了一个环形队列，非常适合于实
     * 现生产者和消费者模式，比如事件和消息的发布。在 Disruptor 中，别出心裁地使用了环形队列（RingBuffer）来代替
     * 普通线性队列，这个环形队列内部实现为一个普通的数组。对于一般的队列，势必要提供队列同步头部 head 和尾部 tail
     * 的两个指针，用于出队和入队，这样无疑就增加了线程协作的复杂度。但如果队列是环形的，则只需要对外提供一个当前位置
     * cursor，利用这个指针既可以进行入队操作，也可以进行出队操作。由于环形队列的缘故，队列的总大小必须事先指定，不
     * 能动态扩展。为了能够快速从一个序列（sequence）对应到数据的实际位置（每次有元素入队，序列就加 1）。Disruptor
     * 要求必须将数组的大小设置为 2 的整数次方。这样 sequence & (queueSize - 1) 就能立即定位到实际的元素位置 index。
     * 这个要比取余操作快得多。
     *
     * 针对 sequence & (queueSize - 1) 的说明：
     * 如果 queueSize 是 2 的整数次幂，则这个数字的二进制必然是 10、100、1000、10000 等形式。所以 (queueSize
     * - 1) 就是一个全 1 的数字，这样就可以将 sequence 限定在 (queueSize - 1) 范围内，并且不会任何一位是浪费的。
     *
     * 在 RingBuffer 的结构中，生产者向 RingBuffer 缓冲区中写入数据，而消费者从中读取数据。生产者写入数据时，使用
     * CAS 操作，消费者读取数据时，为了防止多个消费者处理同一个数据，也使用 CAS 操作进行数据保护。
     *
     * 这种固定大小的环形队列的另外一个好处就是可以做到完全的内存复用。在系统运行过程中，不会有新的空间需要分配或者老
     * 的空间需要回收。因此，可以大大减少系统分配空间以及回收空间的额外开销。
     *
     *
     *
     * 用 Disruptor 实现生产者-消费者案例
     *
     * 下面将使用截止到目前（2020/09/21）的最新版本 disruptor-3.4.2 来展示 Disruptor 的基本使用方法。注意，不同
     * 版本的 disruptor 可能会有细微的差别。
     *
     * Disruptor 官网链接：http://lmax-exchange.github.io/disruptor/
     *
     * 以 Producer、Consumer、ProducedConsumedData、ProducedConsumedDataFactory 等类为例，使用生产者不断产生
     * 整数，消费者读取生产者的数据，并计算平方。
     *
     * Consumer 实现了 WorkHandler 接口。消费者的作用是读取数据进行处理。这里数据的读取已经由 Disruptor 进行了封装，
     * onEvent() 方法为框架的回调方法。所以，该类只需要进行简单的数据处理即可。
     *
     * ProducedConsumedDataFactory 是一个产生 ProducedConsumedData 的工厂类。它会在 Disruptor 系统初始化时，构
     * 造所有的缓冲区中的对象实例（Disruptor 会预先分配空间）。
     *
     * Producer 稍微复杂些。生产者需要一个 RingBuffer 的引用，也就是环形缓冲区。它有一个重要的方法 pushData() 将产
     * 生的数据推入缓冲区。方法 pushData 接收一个 ByteBuffer 对象。在 ByteBuffer 中可以用来包装任何数据类型。这里用
     * 来存储 long 整数，pushData() 的功能就是将传入的 ByteBuffer 中的数据提取出来，并装载到环形缓冲区中。其中，通过
     * next() 方法得到一个可用的序列号，然后通过序列号取得下一个空闲可用的 ProducedConsumedData 对象，并将这个对象里
     * 面的数据设为期望值，这个值最终会传递给消费者。最后，进行数据发布，只有发布后的数据才会真正被消费者看见。
     *
     * 至此，生产者、消费者和数据都已经准备完毕，只差一个统筹规划的主方法将所有内容进行整合。主方法中先是将缓冲区的大小设
     * 为 1024，它显然是 2 的整数次幂 -- 一个合理的大小。然后创建了 disruptor 对象，它封装了整个 disruptor 库的使用，
     * 提供了一些便捷的 API。再然后设置了 4 个消费者实例，系统会将每一个消费者实例映射到一个线程里，即 将会提供 4 个消费
     * 者线程。再然后启动并初始化 disruptor 系统。最后由一个生产者不断的向缓冲区中存入数据。
     *
     * 这样，生产者和消费者就能正常工作了。根据 Disruptor 的官方报告，Disruptor 的性能要比 BlockingQueue 至少高一个
     * 数量级以上。如此诱人的性能，自然是值得一试。
     *
     *
     *
     * 提高消费者的响应时间：选择合适的策略
     *
     * 当有新数据在 Disruptor 的环形缓冲区中产生时，消费者如何知道这些新产生的数据？或者说，消费者如何监控缓冲区中的信息？
     * 为此，Disruptor 提供了几种策略，这些策略由 WaitingStrategy 接口进行封装，主要有以下几种实现。
     *
     * （1）BlockingWaitStrategy: 这是默认的策略。使用 BlockingWaitStrategy 和使用 BlockingQueue 是非常类似的，
     * 它们都使用锁和条件进行数据的监控和线程的唤醒。因为涉及到线程的切换，BlockingWaitStrategy 策略是最节省 CPU，但是
     * 在高并发下性能表现最糟糕的一种等待策略。
     *
     * （2）SleepingWaitStrategy: 这个策略也是对 CPU 使用率非常保守的。它会在循环中不断等待数据。它会先进行自旋等待，
     * 如果不成功，则使用 Thread.yield() 让出 CPU，并最终使用 LockSupport.parkNanos() 进行线程休眠，以确保不占用
     * 太多的 CPU 时间。因此，这个策略对于数据数据可能产生比较高的平均延时。它比较适合于对延时要求不是特别高的场合，好处
     * 是它对生产者线程的影响最小。典型的应用场景就是异步日志。
     *
     * （3）YieldingWaitStrategy: 这个策略用于低延时的场合。消费者线程会不断循环监控缓冲区变化，在循环内部，它会使用
     * Thread.yield() 让出 CPU 给别的线程执行。如果需要一个高性能的系统，并且对延时有较为严格的要求，则可以考虑这种策
     * 略。使用策略时，相当于消费者线程变身成为了一个内部执行了 Thread.yield() 的死循环。因此，最好有多于消费者线程数量
     * 的逻辑 CPU 数量（逻辑 CPU 指的是 "双核四线程" 中的那个四线程），否则整个应用程序恐怕都会受到影响。
     *
     * （4）BusySpinWaitStategy: 这个是最疯狂的等待策略了。它就是一个死循环。消费者线程会尽最大努力疯狂监控缓冲区的变
     * 化。因此，它会吃掉所有的 CPU 资源。只有在对延迟非常苛刻的场合可以考虑使用它（或者说，系统真的非常繁忙）。因为在这
     * 里等同于开启了一个死循环监控，所以，物理 CPU 的数量必须要大于消费者线程数。注意，这里说的是物理 CPU，如果在一个物
     * 理核上使用超线程技术模拟两个逻辑核，另外一个逻辑核显然就会受到这种超密集计算的影响而不能正常工作。
     *
     * 在本例中，使用的是 BlockingWaitStrategy。不妨替换一下不同策略，来体验一下不同等待策略的效果。
     *
     *
     * CPU Cache 的优化：解决伪共享问题
     *
     * 除了使用 CAS 和提供了各种不同的等待策略来提高系统的吞吐量外。Disruptor 大有将优化进行到底的气势，它甚至尝试解决
     * CPU 缓存的伪共享问题。
     *
     * 什么是伪共享问题？众所周知，为了提高 CPU 的速度，CPU 有一个高速缓存 Cache。在高速缓存中，读写数据的最小单位是缓
     * 存行（Cache Line），它是从主存（memory）复制到缓存（Cache）的最小单位，一般为 32 字节到 128 字节。
     *
     * PS：每一个 CPU 都有一个高速缓存。
     *
     * 如果两个变量存放在一个缓存行时，在多线程访问中，可能会相互影响彼此的性能。假设 X 和 Y 在同一个缓存行，运行在 CPU1
     * 上的线程更新了 X，那么 CPU2 上的缓存行就会失效。CPU2 上同一行的 Y 即使没有修改也会变成无效，导致 Cache 无法命中。
     * 接着，如果CPU2 上的线程更新了 Y（此时，CPU2 重新从主存载入同一行的 X 和 Y），则导致 CPU1 上的缓存行又失效（同一
     * 行的 X 又变得无法访问）。这种情况反反复复发生，无疑是一个潜在的性能杀手，如果 CPU 经常不能命中缓存，那么系统的吞吐
     * 量就会急剧下降。
     *
     * 为了使这种情况不发生，一种可行的做法是在 X 变量的前后空间都先占据一定的位置（把它叫做 padding 吧，用来填充用的）。
     * 这样，当内存被读入缓存中时，这个缓存行中，只有 X 一个变量是实际有效的，因此就不会发生多个线程同时修改缓存行中不同
     * 变量导致全体变量失效的情况。
     *
     * 以 FalseSharing 为例，来实现上述目的。这里使用四个线程，即 将 NUM_THREADS 设为 4（因为所使用的电脑是四个物理
     * 核的，可以根据自己电脑的硬件配置修改该参数）。然后准备一个 VolatileLong 数组，数组元素个数和线程数量保持一致。
     * 每个线程都会访问自己对应的 VolatileLong 数组中的元素（线程和数组下标对应）。
     *
     * 最关键的一点就是 VolatileLong。其中准备了 7 个 long 型变量用来填充缓存。实际上，只有 VolatileLong.value 会被
     * 使用。而 p1、p2、p3 等仅仅用于将 VolatileLong 数组中第一个 VolatileLong.value 和第二个 VolatileLong.value
     * 分开，防止它们进入同一个缓存行。
     *
     * 这里使用 JDK 8 的 64 位版本。运行代码，输出如下：
     *
     * duration: 17267
     *
     * 这说明系统花费了 17 秒完成了所有的操作。如果注释掉 private long p1, p2, p3, p4, p5, p6, p7; 填充变量这一行。
     * 也就是运行系统中 VolatileLong.value 放置在同一个缓存行中。运行代码，输出如下：
     *
     * duration: 30328
     *
     * 花费了 30 秒。显然，这一行填充对系统的性能是非常有帮助的。
     *
     * 注意：由于各个 JDK 版本内部实现不一致，在某些 JDK 版本中，会自动优化不使用的字段。这将直接导致这种 padding 的伪
     * 共享解决方案失效（如：JDK 8 的某些小版本，目前使用的版本没有出现这个问题）。
     *
     * Disruptor 框架充分考虑了这个问题，它的核心组件 Sequence 会被非常频繁的访问（每次入队，它都会被加 1）。其基本结构
     * 如下：
     *
     * class LhsPadding
     * {
     *     protected long p1, p2, p3, p4, p5, p6, p7;
     * }
     *
     * class Value extends LhsPadding
     * {
     *     protected volatile long value;
     * }
     *
     * class RhsPadding extends Value
     * {
     *     protected long p9, p10, p11, p12, p13, p14, p15;
     * }
     *
     *public class Sequence extends RhsPadding
     * {
     *     // 省略具体实现
     * }
     *
     * 虽然在 Sequence 中，主要使用的只有 value。但是通过 LhsPadding 和 RhsPadding，在这个 value 的前后安置了一些
     * 占位空间，使得 value 可以无冲突的存在于缓存中。
     *
     * 此外，对于 Disruptor 的环形缓冲区 RingBuffer，它内部的数组是通过以下语句构造的：
     *
     * this.entries = new Object[sequencer.getBufferSize() + 2 * BUFFER_PAD];
     *
     * 注意，这里实际产生的数组大小是缓冲区实际大小再加上两倍的 BUFFER_PAD。这就相当于在这个数组的头部和尾部两段各增加了
     * BUFFER_PAD 个填充，使得整个数组被载入 Cache 时不会受到其他变量的影响而失效。
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ProducedConsumedDataFactory factory = new ProducedConsumedDataFactory();
        // specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;
        Disruptor<ProducedConsumedData> disruptor = new Disruptor<ProducedConsumedData>(factory,
                bufferSize,
                executor,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(new Consumer(), new Consumer(), new Consumer(), new Consumer());
        disruptor.start();
        RingBuffer<ProducedConsumedData> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            byteBuffer.putLong(0, l);
            System.out.println("add data: " + l);
            producer.pushData(byteBuffer);
            Thread.sleep(1000);
        }
    }

}

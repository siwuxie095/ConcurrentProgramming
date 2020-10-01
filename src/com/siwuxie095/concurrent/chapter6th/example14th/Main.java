package com.siwuxie095.concurrent.chapter6th.example14th;

/**
 * @author Jiajing Li
 * @date 2020-10-01 15:20:18
 */
public class Main {

    /**
     * 增强的 Future: CompletableFuture
     *
     * CompletableFuture 是 Java 8 新增的一个超大型工具类。为什么说它大呢？因为它实现了 Future 接口，而且更重要的是，
     * 它也实现了 CompletionStage 接口。CompletionStage 接口也是在 Java 8 中新增的。而 CompletionStage接口拥有将
     * 近 40 个方法。这看起来完全不符合设计原则中所谓的 "单方法接口"，但是在这里，它就这么存在了。这个接口之所以拥有如此
     * 众多的方法，是为了函数式编程中的流式调用准备的。通过 CompletionStage 提供的接口，可以在一个执行结果上进行多次流式
     * 调用，以此得到最终结果。
     *
     *
     *
     * 完成了就通知我
     *
     * CompletableFuture 和 Future 一样，可以作为函数调用的契约。如果向 CompletableFuture 请求一个数据，如果数据
     * 还没有准备好，请求线程就会等待。而让人惊喜的是，通过 CompletableFuture，可以手动设置 CompletableFuture 的完
     * 成状态。
     *
     * 以 Complete 为例，它接收一个 CompletableFuture 作为其构造方法的参数，它的任务是计算 CompletableFuture 表
     * 示的数字的平方，并将其打印。
     *
     * 在主方法中，先是创建了一个 CompletableFuture 对象实例，让将这个对象实例传递给 Complete 线程，并启动这个线程。
     * 此时，Complete 执行到 res = future.get() * future.get(); 会阻塞，因为 CompletableFuture 中根本没有它
     * 所需要的数据，整个 CompletableFuture 处于未完成状态。然后模拟了一个长时间的计算过程，当计算完成后，可以将最终数
     * 据载入 CompletableFuture，并标记为完成状态。这时，Complete 就可以继续执行了。
     *
     *
     *
     * 异步执行任务
     *
     * 通过 CompletableFuture 提供的进一步封装，很容易实现 Future 模式那样的异步调用。
     *
     * 以 AsyncTask 为例，在主方法中使用了 CompletableFuture.supplyAsync() 方法构造了一个 CompletableFuture
     * 实例，在 supplyAsync() 方法中，会到一个新的线程执行传入的参数。在这里，是执行 calc() 方法。而 calc() 方法
     * 的执行可能是比较慢的，但是这不影响 CompletableFuture 实例的构造速度，因此，supplyAsync() 方法会立即返回，
     * 它返回的 CompletableFuture 对象实例就可以作为这次调用的契约，在将来任何场合，用于获得最终的计算结果。当调用
     * get() 方法时，当前计算还没有完成，则调用 get() 方法的线程就会等待。
     *
     * 在 CompletableFuture 中，类似的工厂方法有以下四个：
     *
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
     * public static CompletableFuture<Void> runAsync(Runnable runnable)
     * public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
     *
     * 其中 supplyAsync() 方法用于那些需要有返回值的场景，比如计算某个数据等。而 runAsync() 方法用于没有返回值的场景，
     * 比如，仅仅是简单的执行某一个异步动作。
     *
     * 在这两对方法中，都有一个方法可以接收一个 Executor 参数。这就可以让 Supplier<U> 或 Runnable 在指定的线程池中
     * 工作。如果不指定，则在默认的系统公共的 ForkJoinPool.common 线程池中执行。
     *
     * 注意：在 Java 8 中，新增了 ForkJoinPool.commonPool() 方法，它可以获得一个公共的 ForkJoin 线程池。这个公共
     * 线程池中的所有线程都是 Daemon 线程。这意味着如果主线程退出，这些线程无论是否执行完毕，都会退出系统。
     *
     *
     *
     * 流式调用
     *
     * CompletionStage 中有将近 40 个接口，均是为函数式编程做准备的。
     *
     * 以 StreamCall 为例，展示了如何使用这些接口进行函数式的流式 API 调用。其中先是使用 supplyAsync() 方法执行一个
     * 异步任务。接着连续使用流式调用对任务的处理结果进行再加工，直到最后的结果输出。
     *
     * 这里使用 get() 方法，目的是等待 calc() 方法执行完成。如果不进行这个等待调用，由于 CompletableFuture 异步执行
     * 的缘故，主方法不等 calc() 方法执行完毕就会退出，随着主线程的结束，所有的 Daemon 线程都会立即退出，从而导致 calc()
     *  方法无法正常完成。
     *
     *
     *
     * CompletableFuture 中的异常处理
     *
     * 如果 CompletableFuture 在执行过程中遇到异常，可以用函数式编程的风格来优雅地处理这些异常。CompletableFuture
     * 提供了一个异常处理方法 exceptionally()。
     *
     * 以 ExceptionHandle 为例，对当前的 CompletableFuture 进行了异常处理。如果没有异常发生，则 CompletableFuture
     * 就会返回原有的结果。如果遇到了异常，就可以在 exceptionally() 中处理异常，并返回一个默认的值。在本例中，忽略了异常
     * 堆栈，只是简单地打印异常信息。
     *
     * 运行代码，结果如下：
     *
     * java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
     * "0"
     *
     *
     *
     * 组合多个 CompletableFuture
     *
     * CompletableFuture 还允许将多个 CompletableFuture 进行组合，一种方法是使用 thenCompose() 方法，它的签名如下：
     *
     * public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn)
     *
     * 一个 CompletableFuture 可以在执行完成后，将执行结果通过 Function 传递给下一个 CompletionStage 进行处理，其中
     * Function 接口会返回新的 CompletionStage 实例。
     *
     * 以 Compose 为例，会将处理后的结果传递给 thenCompose() 方法，也就是传递给后续新生成的 CompletableFuture 实例。
     *
     * 运行代码，结果如下：
     *
     * "12"
     *
     * 另外一种组合多个 CompletableFuture 的方法是 thenCombine()，它的签名如下：
     *
     *     public <U,V> CompletableFuture<V> thenCombine(
     *         CompletionStage<? extends U> other,
     *         BiFunction<? super T,? super U,? extends V> fn)
     *
     * 方法 thenCombine() 首先完成当前 CompletableFuture 和 other 的执行。接着，将这两者的执行结果传递给 BiFunction，
     * 并返回代表 BiFunction 实例的 CompletableFuture 对象。
     *
     * 以 Combine 为例，首先生成两个 CompletableFuture 实例，接着使用 thenCombine() 组合这两个 CompletableFuture，
     * 将两者的执行结果进行累加，然后将累加结果转为字符串并输出。
     *
     * 运行代码，结果如下：
     *
     * "37"
     */
    public static void main(String[] args) {

    }

}

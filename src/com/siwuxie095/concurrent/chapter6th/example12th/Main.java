package com.siwuxie095.concurrent.chapter6th.example12th;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * @author Jiajing Li
 * @date 2020-09-30 07:50:14
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 一步一步走入函数式编程
     *
     * 为了能更快地理解函数式编程，先从一个简单的例子开始：
     *
     *     private static void first() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         for (int val : arr) {
     *             System.out.println(val);
     *         }
     *     }
     *
     * 上述代码循环遍历了数组内的元素，并且进行了数值的打印，这也是传统的做法。如果使用 Java 8 中的流，
     * 那么可以这么写：
     *
     *     private static void second() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach(new IntConsumer() {
     *             @Override
     *             public void accept(int value) {
     *                 System.out.println(value);
     *             }
     *         });
     *     }
     *
     * 注意：Arrays.stream() 方法返回了一个流对象。类似于集合或者数组，流对象也是一个对象的集合，它将
     * 提供遍历处理流内元素的功能。
     *
     * 这里值得注意的是，这个流对象的 forEach() 方法，它接收一个 IntConsumer 接口的实现，用于对每个
     * 流内的对象进行处理。之所以是 IntegerConsumer 接口，因为当前流是 IntStream，也就是装有 Integer
     * 元素的流，因此，它自然需要一个处理 Integer 元素的接口。函数 forEach() 会挨个将流内的元素送入
     * IntConsumer 进行处理，循环过程被封装在 forEach 内部，也就是 JDK 框架内。
     *
     * 除了 IntStream 外，Arrays.stream() 还支持 DoubleStream、LongStream 和普通的对象流 Stream，
     * 这完全取决于它所接收的参数，如下：
     *
     * public static <T> Stream<T> stream(T[] array)
     * public static <T> Stream<T> stream(T[] array, int startInclusive, int endExclusive)
     * public static IntStream stream(int[] array)
     * public static IntStream stream(int[] array, int startInclusive, int endExclusive)
     * public static LongStream stream(long[] array)
     * public static LongStream stream(long[] array, int startInclusive, int endExclusive)
     * public static DoubleStream stream(double[] array)
     * public static DoubleStream stream(double[] array, int startInclusive, int endExclusive)
     *
     * 但这样的写法可能还不能让人满意，代码量似乎比原先还多，而且除了引入不必要的接口和匿名类等复杂性外，
     * 也看不出来有什么太大的好处。但是还没到最后，不妨继续往下看。试想，既然 forEach() 函数的参数是可
     * 以从上下文推导出来的，那为什么还要不厌其烦地写出来呢？这些机械的推导工作，直接交给编译器就好了。
     * 于是就有了如下代码：
     *
     *     private static void third() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach((final int x) -> {
     *             System.out.println(x);
     *         });
     *     }
     *
     * 从上述代码可以看到，IntStream 接口名称被省略了，这里指只使用了参数名和一个实现体，看起来就简洁了
     * 不少。但是还不够，因为参数的类型也是可以推导的。既然是 IntStream 接口，参数自然是 int 了。于是：
     *
     *     private static void forth() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach((x) -> {
     *             System.out.println(x);
     *         });
     *     }
     *
     * 好了，现在连参数类型也省略了，但是这两个花括号特别碍眼。虽然它们对程序没有什么影响，但是为了简单的
     * 一句执行语句要加上一对花括号也实属没有必要，那干脆也去掉吧。去掉花括号后，为了清晰起见，把参数声明
     * 和接口实现就放在一行吧，如下：
     *
     *     private static void fifth() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach((x) -> System.out.println(x));
     *     }
     *
     * PS：如果有多条执行语句，则必须使用花括号。
     *
     * 甚至于还可以做如下简化：
     *
     *     private static void sixth() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach(x -> System.out.println(x));
     *     }
     *
     * 可以看到，x 左右的小括号被去掉了。当只有一个参数时，可以不使用小括号。但是当没有参数或者有多个参数
     * 时，则必须使用小括号。没有参数是这样：() -> {...}，有多个参数是这样：(x, y) -> {...}。
     *
     * 这样看起来就好多了。此时 forEach() 函数的参数依然是 IntConsumer，但是它却以一种新的形式被定义，
     * 这就是 lambda 表达式。表达式使用 -> 分割，左半部分表示参数，右半部分表示实现体（函数体）。所以
     * 也可以简单地理解 lambda 表达式只是匿名对象实现的一种新的方式。实际上，也是这样的。
     *
     * 以 Test 为例，来说明 lambda 表达式的原理。可以通过命令行的方式对其进行编译，如下：
     *
     * javac -g /Users/siwuxie095/IdeaProjects/ConcurrentProgramming/src/com/siwuxie095/
     * concurrent/chapter6th/example12th/Test.java
     *
     * 此时会在当前包下生成 Test.class 文件，再通过命令行对 class 文件进行反编译，如下：
     *
     * javap -p -v /Users/siwuxie095/IdeaProjects/ConcurrentProgramming/src/com/siwuxie095/
     * concurrent/chapter6th/example12th/Test.class
     *
     * 查看反编译文件，可以看到多出来一个私有静态方法 lambda$main$0(int)，如下：
     *
     *   private static void lambda$main$0(int);
     *     descriptor: (I)V
     *     flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
     *     Code:
     *       stack=2, locals=1, args_size=1
     *          0: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *          3: iload_0
     *          4: invokevirtual #6                  // Method java/io/PrintStream.println:(I)V
     *          7: return
     *       LineNumberTable:
     *         line 14: 0
     *
     * 关于这个方法命令是这样，以 lambda 开头，因为是在 main 方法里使用 lambda 表达式，所以带有 $main，
     * 又因为是第一个 lambda 表达式，所以是 $0。
     *
     * 然后再使用 JVM 参数 -Djdk.internal.lambda.dumpProxyClasses 可以将 lambda 表达式相关的中间
     * 类型进行输出，方便调试和学习。命令行如下：
     *
     * cd src
     * java -Djdk.internal.lambda.dumpProxyClasses com.siwuxie095.concurrent.chapter6th.
     * example12th.Test
     * cd ..
     *
     * 此时，会在当前包下生成一个 Test$$Lambda$1.class 文件，其内容如下：
     *
     * // $FF: synthetic class
     * final class Test$$Lambda$1 implements IntConsumer {
     *     private Test$$Lambda$1() {
     *     }
     *
     *     @Hidden
     *     public void accept(int var1) {
     *         Test.lambda$main$0(var1);
     *     }
     * }
     *
     * 可以看到，这个类 Test$$Lambda$1 被声明为 final，且实现了 IntConsumer 接口的 accept() 方法，
     * 实现的内容很简单，就是调用了前面编译所产生的 lambda$main$0()。也就是说，中间还是生成了一个实现
     * lambda 表达式的内部类，使用 lambda 表达式，也就像使用匿名对象一样。
     *
     * 由此，可以看到，Java 8 中对 lambda 表达式的处理几乎等同于匿名类的实现，但是在写法上和编程范式上
     * 有了明显的区别。
     *
     * 不过，简化代码的流程并没有结束，因为 Java 8 还支持了方法引用，通过方法引用的推导，甚至连参数声明
     * 和传递都可以省略，如下：
     *
     *     private static void seventh() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach(System.out::println);
     *     }
     *
     * 至此，结束。使用 lambda 表达式不仅可以简化匿名类的编写，与接口的默认方法结合，还可以使用更顺畅的
     * 流式 API 对各种组件进行更自由的装配。
     *
     * 下面这个例子对集合中原有的元素进行两次输出，一次输出到标准错误，一次输出到标准输出中。
     *
     *     private static void doublePrint() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         IntConsumer outPrintln = System.out::println;
     *         IntConsumer errPrintln = System.err::println;
     *         Arrays.stream(arr).forEach(outPrintln.andThen(errPrintln));
     *     }
     *
     * 这里首先使用方法引用，直接定义了两个 IntConsumer 接口实例，一个指向标准输出，另一个指向标准错误。
     * 使用接口的默认方法 IntConsumer.andThen()，将两个 IntConsumer 进行组合，得到一个新的 IntConsumer，
     * 这个新的 IntConsumer 会依次调用 outPrintln 和 errPrintln，完成对数组中元素的处理。
     *
     * 其中 IntConsumer.andThen() 方法的实现如下：
     *
     *     default IntConsumer andThen(IntConsumer after) {
     *         Objects.requireNonNull(after);
     *         return (int t) -> { accept(t); after.accept(t); };
     *     }
     *
     * 可以看到，andThen() 方法返回一个新的 IntConsumer，这个新的 IntConsumer 会先调用第一个 IntConsumer
     * 进行处理，接着调用第二个 IntConsumer 进行处理，从而实现多个处理器的整合。这种操作手法在 Java 8 的函数
     * 式编程中极其常见。
     */
    public static void main(String[] args) {
        first();
        second();
        third();
        forth();
        fifth();
        sixth();
        seventh();
        doublePrint();
    }

    private static void first() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        for (int val : arr) {
            System.out.println(val);
        }
    }

    private static void second() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.println(value);
            }
        });
    }

    private static void third() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach((final int x) -> {
            System.out.println(x);
        });
    }

    private static void forth() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach((x) -> {
            System.out.println(x);
        });
    }

    private static void fifth() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach((x) -> System.out.println(x));
    }

    private static void sixth() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach(x -> System.out.println(x));
    }

    private static void seventh() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach(System.out::println);
    }

    private static void doublePrint() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        IntConsumer outPrintln = System.out::println;
        IntConsumer errPrintln = System.err::println;
        Arrays.stream(arr).forEach(outPrintln.andThen(errPrintln));
    }
}

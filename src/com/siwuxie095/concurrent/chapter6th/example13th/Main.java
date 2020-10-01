package com.siwuxie095.concurrent.chapter6th.example13th;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Jiajing Li
 * @date 2020-10-01 14:25:20
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 并行流与并行排序
     *
     * Java 8 中，可以在接口不变的情况下，将流改为并行流。这样，就可以很自然地使用多线程进行集合中
     * 的数据处理。
     *
     *
     *
     * 使用并行流过滤数据
     *
     * 以 PrimeUtils 为例，统计 1~1_000_000 内所有的质数的数量。其中有一个判断质数的方法，给定
     * 一个数字，如果这个数字是质数就返回 true，否则返回 false。
     *
     * 接着，使用函数式编程统计给定范围内所有的质数：
     *
     * IntStream.range(1, 1_000_000).filter(PrimeUtils::isPrime).count();
     *
     * 上述代码首先生成一个 1 到 1_000_000 的数字流。接着使用过滤函数，只选择所有的质数，最后进行
     * 数量统计。
     *
     * 但上述代码是串行的，将它改造成并行计算非常简单，只需要将流并行化即可：
     *
     * IntStream.range(1, 1_000_000).parallel().filter(PrimeUtils::isPrime).count();
     *
     * 上述代码中，首先使用 parallel() 方法得到一个并行流，接着，在并行流上进行过滤，此时 isPrime()
     * 方法会被多线程并发调用，应用于流中的所有元素。
     *
     *
     *
     * 从集合得到并行流
     *
     * 在函数式编程中，可以从集合得到一个流或者并行流。以 Student 为例，统计集合内所有学生的平均分：
     *
     * students.stream().mapToInt(s -> s.score).average().getAsDouble();
     *
     * 从集合对象 List 中，使用 stream() 方法可以得到一个流。如果希望这段代码并行化，则可以使用
     * parallelStream() 方法：
     *
     * students.parallelStream().mapToInt(s -> s.score).average().getAsDouble();
     *
     * 可以看到，将原有的串行方式改造成并行执行是非常容易的。
     *
     *
     *
     * 并行排序
     *
     * 除了并行流外，对于普通数组，Java 8 中也提供了简单的并行功能。比如，对于数组排序，有 Arrays.sort()
     * 方法。如下：
     *
     *     private static void serialSort() {
     *         int[] arr = new int[1_000_000];
     *         // ...
     *         Arrays.sort(arr);
     *     }
     *
     * 当然，这是串行排序，但在 Java 8 中，可以使用新增的 Arrays.parallelSort() 方法直接使用并行排序。
     * 比如，可以这样使用：
     *
     *     private static void parallelSort() {
     *         int[] arr = new int[1_000_000];
     *         // ...
     *         Arrays.parallelSort(arr);
     *     }
     *
     * 除了并行排序外，Arrays 中还增加了一些 API 用于数组中数据的赋值，比如：
     *
     * public static void setAll(int[] array, IntUnaryOperator generator)
     *
     * 这是一个函数式味道很浓的接口，它的第二个参数是一个函数式接口，如果想给数组中每一个元素都附上一个随机
     * 值，则可以这么做：
     *
     *     private static void serialSetAll() {
     *         int[] arr = new int[1_000_000];
     *         Random random = new Random();
     *         Arrays.setAll(arr, i -> random.nextInt());
     *     }
     *
     * 当然，以上过程是串行的。但是只要使用 setAll() 对应的并行版本，就可以很快将它执行在多个 CPU 上：
     *
     *     private static void parallelSetAll() {
     *         int[] arr = new int[1_000_000];
     *         Random random = new Random();
     *         Arrays.parallelSetAll(arr, i -> random.nextInt());
     *     }
     */
    public static void main(String[] args) {
        serialSort();
        parallelSort();
        serialSetAll();
        parallelSetAll();
    }

    private static void serialSort() {
        int[] arr = new int[1_000_000];
        // ...
        Arrays.sort(arr);
    }

    private static void parallelSort() {
        int[] arr = new int[1_000_000];
        // ...
        Arrays.parallelSort(arr);
    }

    private static void serialSetAll() {
        int[] arr = new int[1_000_000];
        Random random = new Random();
        Arrays.setAll(arr, i -> random.nextInt());
    }

    private static void parallelSetAll() {
        int[] arr = new int[1_000_000];
        Random random = new Random();
        Arrays.parallelSetAll(arr, i -> random.nextInt());
    }

}

package com.siwuxie095.concurrent.chapter6th.example7th;

import java.util.Arrays;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:04:39
 */
public class Main {

    /**
     * 函数式编程简介：更少的代码
     *
     * 通常情况下，函数式编程更加简明扼要，Clojure 语言（一种运行于 JVM 的函数式语言）的爱好者就宣称，使用 Clojure 可以
     * 将 Java 代码行数减少到原有的十分之一。一般来说，精简的代码更易于维护。引入函数式编程范式后，可以使用 Java 用更少的
     * 代码完成更多的工作。
     *
     * 请看如下示例，对于数组中每一个成员，首先判断是否是奇数，如果是奇数，则执行加 1，并最终打印数组内所有成员。
     *
     * （1）传统编程的方式：
     *
     *     private static void tradition() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         for (int i = 0; i < arr.length; i++) {
     *             if (arr[i] % 2 != 0) {
     *                 arr[i]++;
     *             }
     *             System.out.println(arr[i]);
     *         }
     *     }
     *
     * （2）函数式编程的方式：
     *
     *     private static void lambda() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).map(x -> x % 2 == 0 ? x : x + 1).forEach(System.out::println);
     *     }
     *
     * 可以看到，函数式编程的范式更加紧凑而且简洁。
     */
    public static void main(String[] args) {
        tradition();
        System.out.println();
        lambda();
    }

    private static void tradition() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 != 0) {
                arr[i]++;
            }
            System.out.println(arr[i]);
        }
    }

    private static void lambda() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).map(x -> x % 2 == 0 ? x : x + 1).forEach(System.out::println);
    }


}

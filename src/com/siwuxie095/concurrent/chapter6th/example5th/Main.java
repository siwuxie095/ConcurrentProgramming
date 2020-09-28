package com.siwuxie095.concurrent.chapter6th.example5th;

import java.util.Arrays;

/**
 * @author Jiajing Li
 * @date 2020-09-28 21:33:22
 */
public class Main {

    /**
     * 函数式编程简介：不变的对象
     *
     * 在函数式编程中，几乎所有传递的对象都不会被轻易修改。
     *
     * 以如下代码为例：
     *
     *     private static void lambda() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).map(x -> x = x + 1).forEach(System.out::println);
     *         System.out.println();
     *         Arrays.stream(arr).forEach(System.out::println);
     *     }
     *
     * 在第一次打印数组时，看似对每一个数组成员执行了加 1 操作。但是在操作完成后，再一次打印数组时，会发现，
     * 数组的成员并没有变化。在使用函数式编程时，这种状态是一种常态，几乎所有的对象都拒绝被修改，这非常类似
     * 于不变模式。
     */
    public static void main(String[] args) {
        lambda();
    }

    private static void lambda() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).map(x -> x = x + 1).forEach(System.out::println);
        System.out.println();
        Arrays.stream(arr).forEach(System.out::println);
    }

}

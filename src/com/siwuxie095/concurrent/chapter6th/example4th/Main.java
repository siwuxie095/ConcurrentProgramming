package com.siwuxie095.concurrent.chapter6th.example4th;

import java.util.Arrays;

/**
 * @author Jiajing Li
 * @date 2020-09-28 08:28:28
 */
public class Main {

    /**
     * 函数式编程简介：声明式的
     *
     * 函数式编程式声明式(Declarative)的编程方式。相对于命令式(Imperative)而言，命令式的程序设计喜欢大量使用
     * 可变对象和指令。开发者总是习惯于创建对象或者变量，并且修改它们的状态或者值，或者喜欢提供一系列指令，要求程
     * 序执行。这种编程习惯在声明式的函数式编程中有所变化。对于声明式的编程范式，不再需要提供明确的指令操作，所有
     * 的细节指令将会更好地被程序库所封装，而要做的只是提出要求，声明用意即可。
     *
     * 下面一段程序是传统的命令式编程，为了打印数组中的值，需要进行一个循环，并且每次需要判断循环是否结束。在循环
     * 体内，要明确地给出需要执行的语句和参数。
     *
     *     private static void imperative() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         for (int i = 0; i < arr.length; i++) {
     *             System.out.println(arr[i]);
     *         }
     *     }
     *
     * 与之对应的声明式代码如下：
     *
     *     private static void declarative() {
     *         int[] arr = {1, 2, 3, 4, 5, 6, 7};
     *         Arrays.stream(arr).forEach(System.out::println);
     *     }
     *
     * 可以看到，变量数组的循环体就这样消失了。println() 方法似乎也没有指定任何参数。这里只是简单的声明了用意。
     * 有关循环以及判断循环是否结束等操作都被简单地封装在程序库中。
     */
    public static void main(String[] args) {
        imperative();
        declarative();
    }

    private static void imperative() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    private static void declarative() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach(System.out::println);
    }

}

package com.siwuxie095.concurrent.chapter6th.example8th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:16:19
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 函数式编程基础：FunctionalInterface 注解
     *
     * Java 8 提出了函数式接口的概念。所谓函数式接口，简单来说，就是只定义了单一抽象方法的接口。比如下面的定义：
     *
     * @FunctionalInterface
     * public interface IntHandler1st {
     *
     *     void handle(int i);
     *
     * }
     *
     * 注解 FunctionalInterface 用于表明 IntHandler1st 接口是一个函数式接口，该接口被定义为只包含一个抽象
     * 方法 handle()，因此它符合函数式接口的定义。
     *
     * 如果一个函数满足函数式接口的定义，那么即使不标注为 FunctionalInterface，编译器依然会把它看作函数式接口。
     * 这有点像 Override 注解，如果你的函数符合重载的要求，无论你是否标注了 Override，编译器都会识别这个重载函
     * 数，但一旦进行了标注，而实际的代码不符合规范，那么就会得到一个编译错误。比如在一个函数式接口中定义多于一个
     * 抽象方法且使用了 FunctionalInterface 注解，就会报编译错误。如下：
     *
     * @FunctionalInterface
     * public interface IntHandler2nd {
     *
     *     void handle(int i);
     *
     *     void handle2(int i);
     *
     * }
     *
     * 需要强调的是，函数式接口只能有一个抽象方法，而不是只能有一个方法。这分两点来进行说明：
     * （1）在 Java 8 中，接口中可以存在默认方法。
     * （2）任何被 java.lang.Object 实现的方法，都不能视为抽象方法。
     *
     * 所以下面的 NonFunc 接口不是函数式接口，因为 equals() 方法在 Object 中已经实现。
     *
     * public interface NonFunc {
     *
     *     @Override
     *     boolean equals(Object obj);
     *
     * }
     *
     * 同理，下面实现的 IntHandler3rd 接口符合函数式接口要求。虽然看起来不太像，但实际上它是一个完全符合规范的
     * 函数式接口。
     *
     * @FunctionalInterface
     * public interface IntHandler3rd {
     *
     *     void handle(int i);
     *
     *     @Override
     *     boolean equals(Object obj);
     *
     * }
     *
     * 函数式接口的实例可以由方法引用或者 lambda 表达式进行构造。
     */
    public static void main(String[] args) {

    }

}

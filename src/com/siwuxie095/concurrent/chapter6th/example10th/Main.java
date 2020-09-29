package com.siwuxie095.concurrent.chapter6th.example10th;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Jiajing Li
 * @date 2020-09-29 22:02:04
 */
public class Main {

    /**
     * 函数式编程基础：lambda 表达式
     *
     * lambda 表达式可以说是函数式编程的核心。lambda 表达式即匿名函数，它是一段没有函数名的函数体，可以作为参数
     * 直接传递给相关的调用者。lambda 表达式极大地增强了 Java 语言的表达能力。
     *
     * 下面的示例展示了 lambda 表达式的使用，在 forEach() 函数中，传入的就是一个 lambda 表达式，它完成了
     * 对元素的标准输出操作。可以看到这段表达式并不像函数一样有名字，非常类似于匿名内部类，它只是简单地描述应
     * 该执行的代码段。
     *
     *         List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
     *         numbers.forEach((Integer value) -> System.out.println(value));
     *
     * 和匿名对象一样，lambda 表达式也可以访问外部的局部变量，如下所示：
     *
     *         final int num = 2;
     *         Function<Integer, Integer> stringConverter = (from) -> from * num;
     *         System.out.println(stringConverter.apply(3));
     *
     * 上述代码可以编译通过，正常执行，并输出 6。与匿名内部对象一样，在这种情况下，外部的 num 变量必须声明为
     * final，这样才能保证在 lambda 表达式中合法的访问它。
     *
     * 但奇妙的是，对于 lambda 表达式而言，即使去掉上述的 final 定义，程序依然可以编译通过！但千万不要以为
     * 这样就可以修改 num 的值了。实际上，这只是 Java 8 做了一个掩人耳目的小处理，它会自动将在 lambda 表
     * 达式中使用的变量视为 final。因此，如下代码是可以编译通过的：
     *
     *         int num = 2;
     *         Function<Integer, Integer> stringConverter = (from) -> from * num;
     *         System.out.println(stringConverter.apply(3));
     *
     * 但是如果像下面这样写，就不行：
     *
     *         int num = 2;
     *         Function<Integer, Integer> stringConverter = (from) -> from * num;
     *         num++;
     *         System.out.println(stringConverter.apply(3));
     *
     * 其中 num++ 会引起一个编译错误：
     *
     * Variable used in lambda expression should be final or effectively final
     */
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        numbers.forEach((Integer value) -> System.out.println(value));

        final int num = 2;
        Function<Integer, Integer> stringConverter = (from) -> from * num;
        System.out.println(stringConverter.apply(3));
    }

}

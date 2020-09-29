package com.siwuxie095.concurrent.chapter6th.example9th;

import java.util.Comparator;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:44:54
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 函数式编程基础：接口默认方法
     *
     * 在 Java 8 之前的版本，接口只能包含抽象方法。但从 Java 8 之后，接口也可以包含若干个实例方法。这一改进
     * 使得 Java 8 拥有了类似于多继承的能力。一个对象实例，将拥有来自于多个不同接口的实例方法。
     *
     * 比如，对于 Horse 接口（马），如下：
     *
     * public interface Horse {
     *
     *     void eat();
     *
     *     default void run() {
     *         System.out.println("horse run");
     *     }
     * }
     *
     * 在 Java 8 中，使用 default 关键字，可以在接口内定义默认方法（实例方法）。注意，这个方法并非抽象方法，
     * 而是拥有特定逻辑的具体实例方法。
     *
     * 所有的动物都能自由呼吸，所以这里再定义一个 Animal 接口（动物），它也包含一个默认方法 breath()，如下：
     *
     * public interface Animal {
     *
     *     default void breath() {
     *         System.out.println("breath");
     *     }
     *
     * }
     *
     * 骡是马和驴的杂交物种，所以 Mule（骡）可以实现 Horse 接口，同时骡也是动物，也可以实现 Animal 接口，
     * 如下：
     *
     * public class Mule1st implements Horse, Animal {
     *
     *     @Override
     *     public void eat() {
     *         System.out.println("mule eat");
     *     }
     *
     *     public static void main(String[] args) {
     *         Mule1st mule = new Mule1st();
     *         mule.run();
     *         mule.breath();
     *     }
     * }
     *
     * 值得注意的是，Mule1st 实例同时拥有来自不同接口的实现方法。这在 Java 8 之前是做不到的。从某种程度上说，
     * 这种模式可以弥补 Java 单一继承的一些不变。但同时也要知道，它也将遇到和多继承相同的问题。如果另外再定义
     * 一个 Donkey 接口（驴），其中也存在一个默认方法 run()，那么同时实现 Horse、Donkey 接口的 Mule 就会
     * 不知所措，因为它不知道应该以哪个方法为准。
     *
     * 增加一个 Donkey 接口，如下：
     *
     * public interface Donkey {
     *
     *     void eat();
     *
     *     default void run() {
     *         System.out.println("donkey run");
     *     }
     *
     * }
     *
     * 此时的 Mule 同时实现了 Horse、Donkey，但由于 Horse 和 Donkey 拥有相同的默认实例方法 run()，所以
     * 编译器会抛一个错误，如下：
     *
     * com.siwuxie095.concurrent.chapter6th.example9th.Mule2nd inherits unrelated defaults for run() from types
     * com.siwuxie095.concurrent.chapter6th.example9th.Horse and com.siwuxie095.concurrent.chapter6th.example9th.Donkey
     *
     * 为了让 Mule 同时实现 Horse 和 Donkey，就不得不重新实现一下 run() 方法，让编译器可以进行方法绑定。
     * 其实现如下：
     *
     * public class Mule2nd implements Horse, Donkey, Animal {
     *
     *     @Override
     *     public void eat() {
     *         System.out.println("mule eat");
     *     }
     *
     *     @Override
     *     public void run() {
     *         Donkey.super.run();
     *     }
     *
     *     public static void main(String[] args) {
     *         Mule2nd mule = new Mule2nd();
     *         mule.run();
     *         mule.breath();
     *     }
     * }
     *
     * 这里将 Mule 的 run() 方法委托给了 Donkey 的实现。当然，也可以不委托，直接做自己的实现，如下：
     *
     * public class Mule3rd implements Horse, Donkey, Animal {
     *
     *     @Override
     *     public void eat() {
     *         System.out.println("mule eat");
     *     }
     *
     *     @Override
     *     public void run() {
     *         System.out.println("mule run");
     *     }
     *
     *     public static void main(String[] args) {
     *         Mule3rd mule = new Mule3rd();
     *         mule.run();
     *         mule.breath();
     *     }
     * }
     *
     * 接口默认实现对于整个函数式编程的流式表达非常重要。比如，对于 Comparator 接口，它在 JDK 1.2 时就已经
     * 被引入，用于在排序时给出两个对象实例的具体比较逻辑。在 Java 8 中，Comparator 接口新增了若干个默认方
     * 法，用于多个比较器的整合。其中一个常用的默认方法如下：
     *
     *     default Comparator<T> thenComparing(Comparator<? super T> other) {
     *         Objects.requireNonNull(other);
     *         return (Comparator<T> & Serializable) (c1, c2) -> {
     *             int res = compare(c1, c2);
     *             return (res != 0) ? res : other.compare(c1, c2);
     *         };
     *     }
     *
     * 有了这个默认方法，在进行排序时，就可以非常方便地进行元素的多条件排序，比如，如下代码构造了一个比较器，
     * 它先按照字符串长度排序，继而按照大小写不敏感的字母顺序排序。
     *
     *     Comparator<String> cmp = Comparator.comparingInt(String::length)
     *             .thenComparing(String.CASE_INSENSITIVE_ORDER);
     */
    public static void main(String[] args) {
        Comparator<String> cmp = Comparator.comparingInt(String::length)
                .thenComparing(String.CASE_INSENSITIVE_ORDER);
    }

}

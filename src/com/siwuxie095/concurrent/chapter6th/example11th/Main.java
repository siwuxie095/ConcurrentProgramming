package com.siwuxie095.concurrent.chapter6th.example11th;

/**
 * @author Jiajing Li
 * @date 2020-09-29 22:19:06
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 函数式编程基础：方法引用
     *
     * 方法引用是 Java 8 中提出的用来简化 lambda 表达式的一种手段。它通过类名和方法名来定位到一个静态方法
     * 或者实例方法。
     *
     * 方法引用在 Java 8 中的使用非常灵活。总的来说，可以分为以下几种。
     *
     * （1）静态方法引用：ClassName::methodName
     * （2）实例上的实例方法引用：instanceReference::methodName
     * （3）类型上的实例方法引用：ClassName::methodName
     * （4）超类上的实例方法引用：super::methodName
     * （5）构造方法引用：Class::new
     * （6）数组构造方法引用：TypeName[]::new
     *
     * 首先，方法引用使用 :: 定义，:: 的前半部分表示类名或是实例名，后半部分表示方法名称，如果是构造函数，
     * 则使用 new 表示。
     *
     * 下例展示了方法引用的基本使用：
     *
     * public class InstanceMethodRef {
     *
     *     public static void main(String[] args) {
     *         List<User> users = new ArrayList<>();
     *         for (int i = 1; i < 10; i++) {
     *             users.add(new User(i, "Jack" + i));
     *         }
     *         users.stream().map(User::getName).forEach(System.out::println);
     *     }
     *
     * }
     *
     * 对于第一个方法引用 User::getName，表示 User 类的实例方法。在执行时，Java 会自动识别流中的元素（这
     * 里指 User 实例）是作为调用目标还是调用方法的参数。在 User::getName 中，显然流内的元素都应该作为调用
     * 目标，因此实际上，在这里调用了每一个 User 实例的 getName() 方法，并将这些 User 的 name 作为一个新
     * 的流。同时，对于这里得到的所有 name，使用方法引用 System.out::println 进行处理。这里的 System.out
     * 为 PrintStream 对象实例，因此这里表示 System.out 实例的 println 方法，系统也会自动判断，流内的元
     * 素此时应该作为方法的参数传入，而不是调用目标。
     *
     * 一般来说，如果使用的是静态方法，或者调用目标明确，那么流内的元素会自动作为参数使用。如果函数引用表示实
     * 例方法，并且不存在调用目标，那么流内元素就会自动作为调用目标。
     *
     * 如果一个类中存在同名的实例方法和静态方法，那么编译器就会感到困惑。因为此时，它不知道应该使用哪个方法进
     * 行调用。它既可以选择同名的实例方法，将流内元素作为调用目标，也可以使用静态方法，将流作为参数。
     *
     * 请看下面的例子：
     *
     * public class BadMethodRef {
     *
     *     public static void main(String[] args) {
     *         List<Double> numbers = new ArrayList<>();
     *         for (int i = 1; i < 10; i++) {
     *             numbers.add(Double.valueOf(i));
     *         }
     *         numbers.stream().map(Double::toString).forEach(System.out::println);
     *     }
     *
     * }
     *
     * 上述代码试图将所有的 Double 元素转为 String 并将其输出，但是很不幸，在 Double 中同时存在以下两个函数：
     *
     *     public static String toString(double d)
     *     public String toString()
     *
     * 此时对函数引用的处理就出现了歧义，所以这段代码在编译时就会抛如下错误：
     *
     * No compile-time declaration for the method reference is found
     *
     * 方法引用也可以直接使用构造函数。下面的方法引用调用了 User 的构造函数：
     *
     * public class ConstructMethodRef {
     *
     *     @FunctionalInterface
     *     interface UserFactory<T extends User> {
     *
     *         T create(int id, String name);
     *
     *     }
     *
     *     private static UserFactory<User> factory = User::new;
     *
     *     public static void main(String[] args) {
     *         List<User> users = new ArrayList<>();
     *         for (int i = 1; i < 10; i++) {
     *             users.add(factory.create(i, "jack" + i));
     *         }
     *         users.stream().map(User::getName).forEach(System.out::println);
     *     }
     *
     * }
     *
     * 在此，UserFactory 作为 User 的工厂类，是一个函数式接口。当使用 User::new 创建接口实例时，系统会根据
     * UserFactory.create() 的函数签名来选择合适的 User 构造函数。在这里，很显然就是 public User(int id,
     * String name)。在创建 UserFactory 实例后，对 UserFactory.create() 的调用，都会委托给 User 的实际
     * 构造函数进行，从而创建 User 对象实例。
     */
    public static void main(String[] args) {

    }

}

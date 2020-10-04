package com.siwuxie095.concurrent.chapter8th.example1st;

/**
 * @author Jiajing Li
 * @date 2020-10-05 00:05:05
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 并行程序调试
     *
     * 并行程序调试要比串行程序复杂得多。但幸运的是，现在 IDE 可以在一定程度上缓解并发程序调试的难度。下面
     * 将简单介绍一下有关并行程序调试的一些技巧和经验。
     *
     *
     *
     * 准备实验样本
     *
     * 以 UnsafeArrayList 为例，作为实验样本。显然，在多线程下访问 ArrayList 是错误的写法。运行代码，会
     * 抛出类似如下的数组越界异常：
     *
     * Exception in thread "t1" java.lang.ArrayIndexOutOfBoundsException: 33
     * 	at java.util.ArrayList.add(ArrayList.java:463)
     * 	at com.siwuxie095.concurrent.chapter8th.example1st.UnsafeArrayList$AddTask.run(UnsafeArrayList.java:24)
     * 	at java.lang.Thread.run(Thread.java:748)
     *
     * 	下面将使用调试重现这个错误。
     *
     * PS：这里使用的 JDK 版本是 JDK8u171，使用的 IDE 是 IntelliJ IDEA。
     *
     *
     *
     * 正式起航
     *
     * 在正式开始之前，需要先熟悉一下 IDE 的调试环境。当使用 IDE 调试 Java 程序时，程序执行到断点处，默认
     * 情况下，当前的线程就会被挂起。
     *
     * 这里简单在 ArrayList.add() 方法的第一行打一个断点。以调试的方式运行代码，可以看到程序会停留在系统
     * 第一次调用 ArrayList.add() 的地方。此时是主线程 main 停留在 ArrayList.add() 中，并且显示了完整
     * 的调用堆栈。但其实这里对主方法并没有太大兴趣，因为这些都是 JDK 内部的代码实现。目前更关心的是在程序
     * 中 t1 和 t2 线程对 ArrayList 的调用。因此，会更希望忽略这些无关的调用。对于 ArrayList 这种非常常
     * 用的类来说，如果不加识别地进行断点设置，对系统的整个调试会变得异常痛苦。那么应该怎么处理呢？
     *
     * PS：主方法一启动，在 t1 和 t2 启动之前，就会先有内部逻辑去调用 ArrayList.add()。
     *
     * 依托于 IDE 的强大功能，很容易实现这点，即 为这个断点设置一些额外的属性。在断点处右键，会出来一个弹框，
     * 可以对这个断点设置属性。
     *
     * 由于不希望主方法启动时被中断，因此在条件断点中指定断点条件（Condition）是当前线程而不是主线程 main。
     * 指定的断点条件如下：
     *
     * !Thread.currentThread().getName().equals("main")
     *
     * 即 取得当前线程名称，并判断是否为主线程。只有不是主线程时，该断点才会生效。
     *
     * PS：在设置断点条件时，同样可以利用 IDE 的提示来进行设置。
     *
     * 基于以上设置，再次运行代码，就可以调试 t1 和 t2 线程了。
     *
     * 从调试窗口可以看到，当前正在执行的线程中，t1 和 t2 两个线程都在 ArrayList.add() 方法中被挂起。
     *
     * 分别选中对应的线程，就可以进行单步调试等操作。如果要对另一个线程进行调试，只需要手动选中另一个线程即可。
     *
     *
     *
     * 挂起整个虚拟机
     *
     * 这里有一个比较重要的功能需要提一下。当断点条件成立时，系统可以有两个选择：
     * （1）挂起整个 JVM，即 Suspend All。
     * （2）挂起相关线程，即 Suspend Thread。
     *
     * PS：不同 IDE 的默认选择不同。
     *
     * 如果系统只是挂起相关线程，那么没有断点的线程会继续执行。在实际环境中，那些还在继续执行的线程可能会对
     * 整个调试产生不利的影响。所以建议选择挂起整个 JVM，而不仅仅是挂起相关线程。
     *
     * PS：在实际使用中，做什么选择，还是要看情况。
     *
     * 当选择 Suspend All 时，运行代码，只会看到 main、t1、t2 三个线程，此时 t3 并没有出现，因为还没有
     * 运行到 t3 启动，整个虚拟机都被挂起了。
     *
     * 当选择 Suspend Thread 时，运行代码，可以看到 main、t1、t2、t3 四个线程，此时仅仅只是挂起了相关的
     * 线程 t1 和 t2，由于 t3 中没有调用 ArrayList.add()，所以可以正常运行。
     *
     *
     *
     * 调试进入 ArrayList 内部
     *
     * 首先，需要理解 ArrayList 的工作方式。在 ArrayList 初始化时，默认会分配 10 个数组空间。当数组空间
     * 消耗完毕后，ArrayList 就会进行自动扩容。在每次 add() 操作时，系统总要事先检查一下内部空间是否满足
     * 所需的大小，如果不满足，就会扩容，否则就可以正常添加元素。
     *
     * 多线程共同访问 ArrayList 的问题在于：在 ArrayList 容量快用完时（只有一个可用空间），如果两个线程
     * 同时进入 add() 方法，并同时判断认为系统满足继续添加元素，而不需要扩容，进而两者都不会进行扩容操作。
     * 之后两个线程先后向系统写入自己的数据，那么必然有一个线程会将数据写到边界外，从而产生了数组越界异常。
     *
     * 下面是 add() 方法的实现，第一行完成容量检查，如果容量不够，则进行扩容。第二行即正式追加元素，第三行
     * 返回成功。
     *
     *     public boolean add(E e) {
     *         ensureCapacityInternal(size + 1);
     *         elementData[size++] = e;
     *         return true;
     *     }
     *
     * 基于上述原理，将断点条件设置为（同上，设置在第一行）：
     *
     * !Thread.currentThread().getName().equals("main") && size == 9
     *
     * 这意味着，在非主线程（这里就是 t1 和 t2）进入 add() 方法后，如果当前 ArrayList 的容量为 9（当前
     * 的最大容量为 10），则触发断点。之所以这么设置，是因为当容量没有饱和时，显然不会有数组越界异常这个问
     * 题，因此可以忽略容量没有饱和的情况。
     *
     * 接着选中 t1 线程，让它进行容量检查，并让它停止在追加元素的语句前。
     *
     * 接着在 t1 增加元素之前，选中 t2 线程，并让 t2 进入 add() 方法，完成容量检查。
     *
     * 此时，t1 和 t2 都认为 ArrayList 中的容量是满足它们的需求的，因此，它们都准备开始追加元素。这里先
     * 选择 t1，完成追加。
     *
     * 在 t1 追加完成后，t2 并不知道数据空间实际上已经用完了。而之前的容量检查告诉 t2，可以继续追加元素，
     * 因此 t2 还会义无反顾地继续执行后续追加操作。选择 t2，让 t2 进行追加。
     *
     * 此时，当 t2 试图向 ArrayList 追加元素时，追加操作并没有如预期一样完成，因为，此时 size 的值已经
     * 超出了 elementData 数组的边界。然后就会抛出数组越界异常，而且该异常位于 t2 线程中。再之后，t2
     * 线程就从线程列表中消失了（执行结束）。
     *
     * Exception in thread "t2" java.lang.ArrayIndexOutOfBoundsException: 10
     * 	at java.util.ArrayList.add(ArrayList.java:463)
     * 	at com.siwuxie095.concurrent.chapter8th.example1st.UnsafeArrayList$AddTask.run(UnsafeArrayList.java:24)
     * 	at java.lang.Thread.run(Thread.java:748)
     *
     * 根据实际测试，在调试过程中，IDE 老是提示跳过了断点：
     *
     * Skipped breakpoint at java.util.ArrayList:462 because it happened inside debugger evaluation
     *
     * 按照网上搜索结果，去到如下路径：
     * Preferences -> Build, Execution, Deployment -> Debugger -> Data Views -> Java
     *
     * 去掉两个勾选，但是这么做并没有生效，如下：
     * （1）Enable 'toString()' object view
     * （2）Enable alternative view for Collections classes
     *
     * 后来在 Stack Overflow 看到一个解决办法：打断点时使用 Suspend Thread，不要用 Suspend All 即可。
     *
     * 链接如下：
     * https://stackoverflow.com/questions/47866398/skipped-breakpoint-because-it-happened-inside-debugger-evaluation-intellij-ide
     *
     * 猜测可能的原因是：
     * 如果挂起整个 JVM，IDE 难以掌控全局，所以老是判断失误，从而跳过断点。而如果仅仅只是挂起相关的线程，
     * IDE 就能够智珠在握，判断断点就能够快准狠，不会出现失误。
     */
    public static void main(String[] args) {

    }

}

package com.siwuxie095.concurrent.chapter4th.example12th;

/**
 * @author Jiajing Li
 * @date 2020-09-13 07:55:02
 */
public class Main {

    /**
     * ThreadLocal 的简单使用
     *
     * 除了控制资源的访问外，还可以通过增加资源来保证所有对象的线程安全。比如，让 100 个人填写个人信息表，如果
     * 只有一支笔，那么大家就得挨个填写，对于管理人员来说，必须保证大家不会去哄抢这仅存的一支笔，否则谁也填不完。
     * 从另一个角度出发，可以干脆就准备 100 支笔，人手一支，那么所有人都可以各自为营，很快就能完成表格的填写工
     * 作。如果说锁是第一种思路，那么 ThreadLocal 就是第二种思路了。
     *
     * 从 ThreadLocal 的名字上可以看到，这是一个线程的局部变量。也就是说，只有当前线程可以访问。既然是只有当
     * 前线程可以访问的数据，自然是线程安全的。
     *
     * 以 BadParseDate 为例，其中使用 SimpleDateFormat 来解析字符串类型的日期。运行代码，会得到类似如下的
     * 异常：
     *
     * java.lang.NumberFormatException: For input string: ""
     * java.lang.NumberFormatException: For input string: ".222222222000"
     * java.lang.NumberFormatException: multiple points
     * java.lang.NumberFormatException: For input string: ".20202E.420202E4"
     *
     * 出现这些问题的原因，是 SimpleDateFormat.parse() 方法并不是线程安全的。因此，在线程池中共享这个对象必
     * 然导致错误。
     *
     * 一种可行的方案是在 sdf.parse() 前后加锁，这是一般的处理思路（使用 synchronized(sdf) 即可）。但在这里
     * 却并不这么做，这里使用 ThreadLocal 为每一个线程都产生一个 SimpleDateFormat 对象实例。
     *
     * 以 GoodParseDate 为例，其中会判断如果当前线程不持有 SimpleDateFormat 对象实例，那么就新建一个并把它
     * 设置到当前线程中，如果已经持有，则直接使用。
     *
     * 从这里也可以看出，为每一个线程人手分配一个对象的工作并不是由 ThreadLocal 来完成的，而是需要在应用层面保
     * 证的。如果在应用层面上为每一个线程分配了相同的对象实例，那么 Thread Local 也不能保证线程安全。
     *
     * 注意：为每一个线程分配不同的对象，需要在应用层面保证。ThreadLocal 只是起到了简单的容器作用。
     */
    public static void main(String[] args) {

    }

}

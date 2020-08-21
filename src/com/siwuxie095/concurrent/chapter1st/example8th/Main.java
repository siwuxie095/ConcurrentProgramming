package com.siwuxie095.concurrent.chapter1st.example8th;

/**
 * @author Jiajing Li
 * @date 2020-08-21 07:38:39
 */
public class Main {

    /**
     * 哪些指令不能重排：Happen-Before 规则
     *
     * 虽然 Java 虚拟机和执行系统会对指令进行一定的重排，但是指令重排是有原则的，并非所有的指令都可以
     * 随意改变执行位置，以下罗列了一些基本原则，这些原则是指令重排不可违背的。
     *
     * （1）程序顺序规则：一个线程内保证语义的串行性
     * （2）volatile 规则：volatile 变量的写，先发生于读，这保证了 volatile 变量的可见性
     * （3）锁规则：解锁（unlock）必然发生在随后的加锁（lock）前
     * （4）传递性规则：A 先于 B，B 先于 C，那么 A 必然先于 C
     * （5）线程启动规则：线程的 start() 方法先于它的每一个动作
     * （6）线程终止规则：线程的所有操作先于线程的终结（Thread.join()）
     * （7）线程中断规则：线程的中断（Thread.interrupt()）先于被中断线程的代码
     * （8）对象终结规则：对象的构造函数执行、结束先于 finalize() 方法
     *
     * 对于（1），重排后的指令绝对不能改变原有的串行语义，比如：
     * a = 1;
     * b = a + 1;
     * 由于第 2 条语句依赖第一条的执行结果。如果贸然交换两条语句的执行顺序，那么程序的语义就会改变。
     * 因此这种情况绝对不允许发生。这也是指令重排的一条基本原则。
     *
     * 对于（2），volatile 变量在每次被线程访问时，都强迫从主内存中读该变量的值，而当该变量发生变
     * 化时，又回强迫将最新的值刷新到主内存，任何时刻，不同的线程总是能够看到该变量的最新值。volatile
     * 的原理是通过内存屏障（一是保证特定程序的执行顺序，而是强制刷出CPU缓存数据，实现数据的可见性）
     * 实现禁止指令重排。
     *
     * 对于（3），锁规则强调，unlock 操作必然发生在后续的对同一个锁的 lock 之前。也就是说，如果对
     * 一个锁解锁后，再加锁，那么加锁的动作绝对不能重排到解锁动作之前。很显然，如果这么做，加锁行为
     * 是无法获得这把锁的。
     *
     * 对于（4），没有什么好说的，就是传递性规则。
     *
     * 对于（5），如果线程 A 在执行线程 B 的 start() 方法之前，修改了共享变量的值，那么当线程 B
     * 执行 start() 时，线程 A 对共享变量的修改对线程 B 可见。
     *
     * 对于（6），Thread.join() 方法的作用是等待当前执行的线程终止。假设在线程 B 终止之前，修改
     * 了共享变量，线程 A 从线程 B 的 join() 方法成功返回之后，线程 B 对共享变量的修改对线程 A
     * 可见。
     *
     * 对于（7），对线程 interrupt() 方法的调用先行发生于被中断线程的代码，检测到中断事件的发生，
     * 可以通过 Thread.interrupted() 方法检测线程是否中断。
     *
     * 对于（8），就是对象的构造，先于对象的回收。
     */
    public static void main(String[] args) {

    }

}

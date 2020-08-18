package com.siwuxie095.concurrent.chapter1st.example5th;

/**
 * @author Jiajing Li
 * @date 2020-01-07 23:18:54
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 原子性（Atomicity）
     *
     * 原子性是指一个操作是不可中断的。即使是在多个线程一起执行的时候，一个操作一旦开始，
     * 就不会被其他线程干扰。
     *
     * 比如，对于一个静态全局变量 int i，两个线程同时对它赋值，线程 A 给他赋值 1，线程
     * B 给它赋值 -1。那么不管这两个线程以何种方式、何种步调工作，i 的值要么是 1，要么
     * 是 -1。线程 A 和线程 B 之间是没有干扰的。这就是原子性的一个特点，不可被中断。
     *
     * 但如果不使用 int 型而使用 long 型的话，可能就没有那么幸运了。对于 32 位系统来说，
     * long 型数据的读写不是原子性的（因为 long 有 64 位）。也就是说，如果两个线程同时
     * 对 long 进行写入的话（或者读取），对线程之间的结果是有干扰的。
     *
     * 如本例代码所示，有 4 个线程对 long 型数据 t 进行赋值，分别对 t 赋值 111、-999、
     * 333、444。然后有一个读取线程，读取这个 t 的值。一般来说，t 的值总是这 4 个数值中
     * 的一个。这当然也是期望值。但在 32 位的虚拟机中，未必总是这样。
     *
     * 如果读取线程 ReadT 总是读到合理的数据，那么这个程序应该没有任何输出。但是，实际上，
     * 这个程序一旦运行，就会输出大量的其他数字。（强调：使用 32 位虚拟机的时候）
     *
     * 看起来，读取线程居然读到了两个根本不可能存在的数值。这就是因为 32 位系统中 long 型
     * 数据的读和写都不是原子性的，多线程之间相互干扰了。
     *
     * 换句话说，由于并行的关系，数字被写乱了，或者读的时候，读串位了。即 每个数字的前 32
     * 位和后 32 位重新自由组合成了新的数字。
     */
    public static void main(String[] args) {
        new Thread(new MultiThreadLong.ChangeT(111L)).start();
        new Thread(new MultiThreadLong.ChangeT(-999L)).start();
        new Thread(new MultiThreadLong.ChangeT(333L)).start();
        new Thread(new MultiThreadLong.ChangeT(-444L)).start();
        new Thread(new MultiThreadLong.ReadT()).start();
    }

}

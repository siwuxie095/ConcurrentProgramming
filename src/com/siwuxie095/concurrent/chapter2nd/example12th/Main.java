package com.siwuxie095.concurrent.chapter2nd.example12th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 07:53:32
 */
public class Main {

    /**
     * 先干重要的事：线程优先级
     *
     * Java 中的线程可以有自己的优先级。优先级高的线程在竞争资源时会更有优势，更可能抢占资源，当然，这只是一个
     * 概率问题。如果运气不好，高优先级线程可能也会抢占失败。由于线程优先级调度和底层操作系统有密切的关系，在各
     * 个平台表现不一，并且这种优先级产生的后果也可能不容易预测，无法精准控制，比如一个低优先级的线程可能一直抢
     * 占不到资源，从而始终无法运行，而产生饥饿（虽然优先级低，但也不能饿死它呀）。因此，在要求严格的场合，还是
     * 需要自己在应用层解决线程调度问题。
     *
     * 在 Java 中，使用 1 到 10 表示线程优先级。一般可以使用内置的三个静态变量表示：
     *
     * public final static int MIN_PRIORITY = 1;
     * public final static int NORM_PRIORITY = 5;
     * public final static int MAX_PRIORITY = 10;
     *
     * 数字越大则优先级越高，但有效范围在 1 到 10 之间，高优先级的线程倾向于更快地完成。
     *
     * 以 PriorityDemo 为例，分别将 HighPriorityThread 设置为高优先级，LowPriorityThread 设置为低优先
     * 级。让它们完成相同的工作，即 把 count 从 0 加到 10_000_000 完成后，打印信息给一个提示，这样就知道谁
     * 先完成工作。值得注意的是，这里在对 count 累加前，使用 synchronized 产生了一次资源竞争，目的是使得优
     * 先级的差异表现的更为明显。
     *
     * 多次运行，不难发现，高优先级的线程在大部分情况下都会首先完成任务。
     */
    public static void main(String[] args) {

    }

}

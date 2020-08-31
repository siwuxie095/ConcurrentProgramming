package com.siwuxie095.concurrent.chapter2nd.example11th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 07:25:51
 */
public class Main {

    /**
     * 驻守后台：守护线程(Daemon)
     *
     * 守护线程是一种特殊的线程，就和它的名字一样，它是系统的守护者，在后台默默地完成一些系统性的服务，比如
     * 垃圾回收线程、JIT 线程就可以理解为守护线程。与之相对应的是用户线程，用户线程可以认为是系统的工作线程，
     * 它会完成这个程序应该要完成的业务操作。如果用户线程全部结束，这也意味着这个程序实际上无事可做了。守护
     * 线程要守护的对象已经不存在了，那么整个应用程序就自然应该结束。因此，当一个 Java 应用内，只有守护线程
     * 时，Java 虚拟机就会自然退出。
     *
     * 以 DaemonDemo 为例，将线程 t 设置为一个守护线程。注意：设置守护线程必须在线程 start() 之前设置，
     * 否则就会得到一个异常 java.lang.IllegalThreadStateException，表示守护线程设置失败。但是程序和
     * 线程依然可以正常执行。只是被当作用户线程而已。因此，如果不小心忽略了异常信息，很可能就会察觉不到这个
     * 错误，从而诧异为什么程序永远停不下来。
     *
     * Exception in thread "main" java.lang.IllegalThreadStateException
     * 	at java.lang.Thread.setDaemon(Thread.java:1359)
     * 	at com.siwuxie095.concurrent.chapter2nd.example11th.DaemonDemo.main(DaemonDemo.java:13)
     *
     * 在本例中，由于 t 被设置为守护线程，系统中只有主线程 main 为用户线程，因此在 main 线程休眠2 秒后退出
     * 时，整个程序也随之结束。但如果不把线程 t 设置为守护线程，main 线程结束后，t 线程还会不停地打印，永远
     * 不会结束。
     */
    public static void main(String[] args) {

    }

}

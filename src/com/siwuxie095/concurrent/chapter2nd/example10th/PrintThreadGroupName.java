package com.siwuxie095.concurrent.chapter2nd.example10th;

/**
 * @author Jiajing Li
 * @date 2020-08-31 21:07:51
 */
public class PrintThreadGroupName implements Runnable {

    public static void main(String[] args) {
        ThreadGroup tg = new ThreadGroup("PrintGroup");
        Thread t1 = new Thread(tg, new PrintThreadGroupName(), "T1");
        Thread t2 = new Thread(tg, new PrintThreadGroupName(), "T2");
        t1.start();
        t2.start();
        System.out.println(tg.activeCount());
        tg.list();
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getThreadGroup().getName() + "-" + Thread.currentThread().getName();
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am " + name);
        }
    }
}

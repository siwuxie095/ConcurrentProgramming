package com.siwuxie095.concurrent.chapter3rd.example8th;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Jiajing Li
 * @date 2020-09-06 15:10:10
 */
@SuppressWarnings("all")
class InterruptDemo {


    private static Object u = new Object();
    private static Thread t1 = new ChangeObjectThread("T1");
    private static Thread t2 = new ChangeObjectThread("T2");

    static class ChangeObjectThread extends Thread {

        public ChangeObjectThread(String name) {
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
                if (Thread.interrupted()) {
                    System.out.println(getName() + ":interrupted");
                }
                System.out.println(getName() + ":over");
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(100);
        t2.start();
        t1.interrupt();
        LockSupport.unpark(t2);
    }

}

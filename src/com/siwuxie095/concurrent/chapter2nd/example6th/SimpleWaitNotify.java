package com.siwuxie095.concurrent.chapter2nd.example6th;

/**
 * @author Jiajing Li
 * @date 2020-08-27 08:32:20
 */
class SimpleWaitNotify {

    private final static Object OBJECT = new Object();

    public static void main(String[] args) {
        Thread t1 = new T1();
        Thread t2 = new T2();
        t1.start();
        t2.start();
    }

    static class T1 extends Thread {

        @Override
        public void run() {
            synchronized (OBJECT) {
                System.out.println(System.currentTimeMillis() + " T1 start");
                try {
                    System.out.println(System.currentTimeMillis() + " T1 wait for OBJECT");
                    OBJECT.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + " T1 end");
            }
        }

    }

    static class T2 extends Thread {

        @Override
        public void run() {
            synchronized (OBJECT) {
                System.out.println(System.currentTimeMillis() + " T2 start, notify T1");
                OBJECT.notify();
                System.out.println(System.currentTimeMillis() + " T2 end");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

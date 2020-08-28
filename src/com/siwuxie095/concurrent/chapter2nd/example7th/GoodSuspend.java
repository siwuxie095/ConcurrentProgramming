package com.siwuxie095.concurrent.chapter2nd.example7th;

/**
 * @author Jiajing Li
 * @date 2020-08-28 07:53:18
 */
class GoodSuspend {

    private static Object u = new Object();

    public static void main(String[] args) throws InterruptedException {
        WriteObjectThread t1 = new WriteObjectThread();
        ReadObjectThread t2 = new ReadObjectThread();
        t1.start();
        t2.start();
        Thread.sleep(1000);
        t1.suspendMe();
        System.out.println("suspend t1 5s");
        Thread.sleep(5000);
        System.out.println("resume t1");
        t1.resumeMe();
    }

    static class WriteObjectThread extends Thread {

        private volatile boolean suspendMe = false;

        public void suspendMe() {
            suspendMe = true;
        }

        public void resumeMe() {
            suspendMe = false;
            synchronized (this) {
                System.out.println("notify");
                notify();
            }
        }

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    while (suspendMe) {
                        try {
                            System.out.println("wait");
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (u) {
                    System.out.println("in WriteObjectThread");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.yield();
            }
        }

    }

    static class ReadObjectThread extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (u) {
                    System.out.println("in ReadObjectThread");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.yield();
            }
        }

    }

}

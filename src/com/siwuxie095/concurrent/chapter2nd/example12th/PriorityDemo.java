package com.siwuxie095.concurrent.chapter2nd.example12th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 08:06:38
 */
class PriorityDemo {

    public static void main(String[] args) {
        Thread high = new HighPriorityThread();
        Thread low = new LowPriorityThread();
        high.setPriority(Thread.MAX_PRIORITY);
        low.setPriority(Thread.MIN_PRIORITY);
        low.start();
        high.start();
    }

    static class HighPriorityThread extends Thread {

        static int count = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (PriorityDemo.class) {
                    count++;
                    if (count > 10_000_000) {
                        System.out.println("HighPriorityThread is completed");
                        break;
                    }
                }
            }
        }

    }

    static class LowPriorityThread extends Thread {

        static int count = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (PriorityDemo.class) {
                    count++;
                    if (count > 10_000_000) {
                        System.out.println("LowPriorityThread is completed");
                        break;
                    }
                }
            }
        }

    }

}

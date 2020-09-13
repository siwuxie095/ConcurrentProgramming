package com.siwuxie095.concurrent.chapter4th.example17th;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jiajing Li
 * @date 2020-09-13 18:59:26
 */
@SuppressWarnings("all")
class AtomicIntegerDemo {

    private static AtomicInteger atomicInteger = new AtomicInteger();

    static class AddThread implements Runnable {

        @Override
        public void run() {
            for (int k = 0; k < 10_000; k++) {
                atomicInteger.incrementAndGet();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int k = 0; k < 10; k++) {
            threads[k] = new Thread(new AddThread());
        }
        for (int k = 0; k < 10; k++) {
            threads[k].start();
        }
        for (int k = 0; k < 10; k++) {
            threads[k].join();
        }
        System.out.println(atomicInteger);
    }

}

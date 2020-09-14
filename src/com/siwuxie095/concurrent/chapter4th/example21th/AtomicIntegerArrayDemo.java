package com.siwuxie095.concurrent.chapter4th.example21th;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author Jiajing Li
 * @date 2020-09-14 22:10:44
 */
@SuppressWarnings("all")
class AtomicIntegerArrayDemo {

    private static AtomicIntegerArray array = new AtomicIntegerArray(10);


    static class AddThread implements Runnable {

        @Override
        public void run() {
            for (int k = 0; k < 10_000; k++) {
                array.getAndIncrement(k % array.length());
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
        System.out.println(array);
    }

}

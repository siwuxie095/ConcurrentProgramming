package com.siwuxie095.concurrent.chapter3rd.example2nd;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jiajing Li
 * @date 2020-09-03 07:55:59
 */
@SuppressWarnings("all")
class ReenterLock implements Runnable {

    private static ReentrantLock lock = new ReentrantLock();

    private static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLock r = new ReenterLock();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}

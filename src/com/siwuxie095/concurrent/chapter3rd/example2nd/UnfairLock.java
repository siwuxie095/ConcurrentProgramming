package com.siwuxie095.concurrent.chapter3rd.example2nd;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jiajing Li
 * @date 2020-09-03 22:49:35
 */
@SuppressWarnings("all")
public class UnfairLock implements Runnable {

    private static ReentrantLock lock = new ReentrantLock(false);

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getId() + " get lock");
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        UnfairLock r = new UnfairLock();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
    }
}

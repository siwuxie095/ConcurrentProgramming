package com.siwuxie095.concurrent.chapter3rd.example2nd;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jiajing Li
 * @date 2020-09-03 22:28:16
 */
@SuppressWarnings("all")
public class TryLock implements Runnable {

    private static ReentrantLock lock1 = new ReentrantLock();
    private static ReentrantLock lock2 = new ReentrantLock();
    int lock;

    public TryLock(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        if (lock == 1) {
            while (true) {
                if (lock1.tryLock()) {
                    try {
                        sleep(500);
                        if (lock2.tryLock()) {
                            try {
                                System.out.println(Thread.currentThread().getId() + ":My Job Done");
                                return;
                            } finally {
                                lock2.unlock();
                            }
                        }
                    } finally {
                        lock1.unlock();
                    }
                }
            }
        } else {
            while (true) {
                if (lock2.tryLock()) {
                    try {
                        sleep(500);
                        if (lock1.tryLock()) {
                            try {
                                System.out.println(Thread.currentThread().getId() + ":My Job Done");
                                return;
                            } finally {
                                lock1.unlock();
                            }
                        }
                    } finally {
                        lock2.unlock();
                    }
                }
            }
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TryLock r1 = new TryLock(1);
        TryLock r2 = new TryLock(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }
}

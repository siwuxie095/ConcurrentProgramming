package com.siwuxie095.concurrent.chapter3rd.example2nd;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jiajing Li
 * @date 2020-09-03 22:15:34
 */
@SuppressWarnings("all")
public class TimeLock implements Runnable {

    private static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                System.out.println("get lock succeeded");
                Thread.sleep(6000);
            } else {
                System.out.println("get lock failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TimeLock r = new TimeLock();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
    }

}

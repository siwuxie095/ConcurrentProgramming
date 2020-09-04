package com.siwuxie095.concurrent.chapter3rd.example3rd;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jiajing Li
 * @date 2020-09-04 07:45:19
 */
@SuppressWarnings("all")
public class ReenterLockCondition implements Runnable {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " get lock and await");
            condition.await();
            System.out.println(Thread.currentThread().getName() + " is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenterLockCondition r = new ReenterLockCondition();
        Thread t = new Thread(r);
        t.start();
        Thread.sleep(2000);
        System.out.println("2 seconds later");
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " get lock");
        condition.signal();
        System.out.println("signal and sleep 5 seconds");
        Thread.sleep(5000);
        lock.unlock();
    }
}

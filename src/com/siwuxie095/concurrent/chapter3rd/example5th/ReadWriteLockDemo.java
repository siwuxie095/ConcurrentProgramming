package com.siwuxie095.concurrent.chapter3rd.example5th;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Jiajing Li
 * @date 2020-09-06 08:35:10
 */
@SuppressWarnings("all")
class ReadWriteLockDemo {

    private static Lock lock = new ReentrantLock();

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static Lock readLock = readWriteLock.readLock();

    private static Lock writeLock = readWriteLock.writeLock();

    private int value;

    /**
     * 模拟读操作
     *
     * 读操作的耗时越多，读写锁的优势就越明显
     */
    public Object handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            return value;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 模拟写操作
     */
    public void handleWrite(Lock lock, int index) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            value = index;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        for (int i = 0; i < 18; i++) {
            new Thread(() -> {
                try {
                    demo.handleRead(readLock);
                    //demo.handleRead(lock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    demo.handleWrite(writeLock, new Random().nextInt());
                    //demo.handleWrite(lock, new Random().nextInt());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}

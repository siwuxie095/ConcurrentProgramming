package com.siwuxie095.concurrent.chapter6th.example15th;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * @author Jiajing Li
 * @date 2020-10-01 18:21:44
 */
@SuppressWarnings("all")
public class StampedLockCpuDemo {

    private static Thread[] holdCpuThreads = new Thread[3];

    private static final StampedLock STAMPED_LOCK = new StampedLock();

    private static class HoldCpuReadThread implements Runnable {

        @Override
        public void run() {
            long stamp = STAMPED_LOCK.readLock();
            System.out.println(Thread.currentThread().getName() + " 获得读锁");
            STAMPED_LOCK.unlockRead(stamp);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            long stamp = STAMPED_LOCK.writeLock();
            LockSupport.parkNanos(600_000_000_000L);
            STAMPED_LOCK.unlockWrite(stamp);
        }).start();
        Thread.sleep(100);
        for (int i = 0; i < 3; i++) {
            holdCpuThreads[i] = new Thread(new HoldCpuReadThread());
            holdCpuThreads[i].start();
        }
        Thread.sleep(10_000);
        // 线程中断后，会占用 CPU
        for (int i = 0; i < 3; i++) {
            holdCpuThreads[i].interrupt();
        }
    }

}

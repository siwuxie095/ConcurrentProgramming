package com.siwuxie095.concurrent.chapter6th.example16th;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Jiajing Li
 * @date 2020-10-01 21:50:48
 */
@SuppressWarnings("all")
public class LongAdderDemo {
    /**
     * 线程数
     */
    private static final int MAX_THREADS = 3;
    /**
     * 任务数
     */
    private static final int TASK_COUNT = 3;
    /**
     * 目标总数
     */
    private static final int TARGET_COUNT = 10_000_000;

    private LongAdder longAdder = new LongAdder();

    private AtomicLong atomicLong = new AtomicLong(0L);

    private long syncCount = 0;

    private static CountDownLatch longAdderLatch = new CountDownLatch(TASK_COUNT);

    private static CountDownLatch atomicLongLatch = new CountDownLatch(TASK_COUNT);

    private static CountDownLatch syncLatch = new CountDownLatch(TASK_COUNT);

    class LongAdderThread implements Runnable {

        private long start;

        public LongAdderThread(long start) {
            this.start = start;
        }

        @Override
        public void run() {
            long value = longAdder.sum();
            while (value < TARGET_COUNT) {
                longAdder.increment();
                value = longAdder.sum();
            }
            long end = System.currentTimeMillis();
            System.out.println("LongAdderThread spend: " + (end - start) + " ms, value: " + value);
            longAdderLatch.countDown();
        }
    }

    class AtomicLongThread implements Runnable {

        private long start;

        public AtomicLongThread(long start) {
            this.start = start;
        }

        @Override
        public void run() {
            long value = atomicLong.get();
            while (value < TARGET_COUNT) {
                value = atomicLong.incrementAndGet();
            }
            long end = System.currentTimeMillis();
            System.out.println("AtomicLongThread spend: " + (end - start) + " ms, value: " + value);
            atomicLongLatch.countDown();
        }
    }

    private synchronized long increment() {
        return ++syncCount;
    }

    private synchronized long getSyncCount() {
        return syncCount;
    }

    class SyncThread implements Runnable {

        private long start;

        public SyncThread(long start) {
            this.start = start;
        }

        @Override
        public void run() {
            long value = getSyncCount();
            while (value < TARGET_COUNT) {
                value = increment();
            }
            long end = System.currentTimeMillis();
            System.out.println("SyncThread spend: " + (end - start) + " ms, value: " + value);
            syncLatch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo demo = new LongAdderDemo();
        demo.testLongAdder();
        demo.testAtomicLong();
        demo.testSync();
    }

    private void testLongAdder() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        long start = System.currentTimeMillis();
        LongAdderThread longAdderThread = new LongAdderThread(start);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(longAdderThread);
        }
        longAdderLatch.await();
        executor.shutdown();
    }

    private void testAtomicLong() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        long start = System.currentTimeMillis();
        AtomicLongThread atomicLongThread = new AtomicLongThread(start);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(atomicLongThread);
        }
        atomicLongLatch.await();
        executor.shutdown();
    }

    private void testSync() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        long start = System.currentTimeMillis();
        SyncThread syncThread = new SyncThread(start);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.submit(syncThread);
        }
        syncLatch.await();
        executor.shutdown();
    }

}

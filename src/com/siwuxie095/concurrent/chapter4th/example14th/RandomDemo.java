package com.siwuxie095.concurrent.chapter4th.example14th;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author Jiajing Li
 * @date 2020-09-13 13:27:54
 */
@SuppressWarnings("all")
class RandomDemo {

    private static final int GEN_COUNT = 10_000_000;

    private static final int THREAD_COUNT = 4;

    private static ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Random random = new Random(123);

    private static ThreadLocal<Random> local = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random(123);
        }
    };

    static class RandomTask implements Callable<Long> {

        private int mode = 0;

        public RandomTask(int mode) {
            this.mode = mode;
        }

        public Random getRandom() {
            if (mode == 0) {
                return random;
            } else if (mode == 1) {
                return local.get();
            } else {
                return null;
            }
        }

        @Override
        public Long call() throws Exception {
            long b = System.currentTimeMillis();
            for (long i = 0; i < GEN_COUNT; i++) {
                getRandom().nextInt();
            }
            long e = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " spend " + (e - b) + " ms");
            return e - b;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futures = new Future[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = executor.submit(new RandomTask(0));
        }
        long totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("多线程访问同一个 Random 实例：" + totalTime + " ms");
        // ThreadLocal 的情况
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = executor.submit(new RandomTask(1));
        }
        totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("使用 ThreadLocal 包装 Random 实例：" + totalTime + " ms");
        executor.shutdown();
    }

}

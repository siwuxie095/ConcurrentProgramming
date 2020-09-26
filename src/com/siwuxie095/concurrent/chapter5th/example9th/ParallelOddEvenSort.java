package com.siwuxie095.concurrent.chapter5th.example9th;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-26 10:26:23
 */
@SuppressWarnings("all")
class ParallelOddEvenSort {

    private static int exchangeFlag = 1;

    public static int getExchangeFlag() {
        return exchangeFlag;
    }

    public static synchronized void setExchangeFlag(int exchangeFlag) {
        ParallelOddEvenSort.exchangeFlag = exchangeFlag;
    }

    static class OddEvenSortTask implements Runnable {
        private int[] arr;
        private int i;
        private CountDownLatch latch;

        public OddEvenSortTask(int[] arr, int i, CountDownLatch latch) {
            this.arr = arr;
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] > arr[i + 1]) {
                int tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
                setExchangeFlag(1);
            }
            latch.countDown();
        }
    }

    public static void parallelOddEvenSort(int[] arr) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        int start = 0;
        while (getExchangeFlag() == 1 || start == 1) {
            setExchangeFlag(0);
            // 数组长度为偶数，当 start 为 1 时，只有 len/2-1 个线程
            CountDownLatch latch = new CountDownLatch(arr.length / 2 - (arr.length % 2 == 0 ? start : 0));
            for (int i = start; i < arr.length - 1; i += 2) {
                executor.submit(new OddEvenSortTask(arr, i, latch));
            }
            // 等待所有线程结束
            latch.await();
            if (start == 0) {
                start = 1;
            } else {
                start = 0;
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        int[] arr = new int[] {7, 4, 3, 8, 9, 10, 1};
        parallelOddEvenSort(arr);
        for (int val : arr) {
            System.out.print(val + " ");
        }
    }
}

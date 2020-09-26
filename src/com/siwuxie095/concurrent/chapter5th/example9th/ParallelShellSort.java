package com.siwuxie095.concurrent.chapter5th.example9th;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-26 11:24:48
 */
@SuppressWarnings("all")
class ParallelShellSort {

    static class ShellSortTask implements Runnable {

        private int[] arr;
        private int i;
        private int h;
        private CountDownLatch latch;

        public ShellSortTask(int[] arr, int i, int h, CountDownLatch latch) {
            this.arr = arr;
            this.i = i;
            this.h = h;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] < arr[i - h]) {
                int tmp = arr[i];
                int j = i - h;
                while (j >= 0 && arr[j] > tmp) {
                    arr[j + h] = arr[j];
                    j -= h;
                }
                arr[j + h] = tmp;
            }
            latch.countDown();
        }
    }

    public static void parallelShellSort(int[] arr) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        int h = 1;
        CountDownLatch latch = null;
        // 计算出 h 的最大值
        while (h <= arr.length / 3) {
            h = h * 3 + 1;
        }
        while (h > 0) {
            System.out.println("h = " + h);
            if (h >= 4 && arr.length - h >= 0) {
                latch = new CountDownLatch(arr.length - h);
            }
            for (int i = h; i < arr.length; i++) {
                // 控制线程数量
                if (h >= 4) {
                    executor.execute(new ShellSortTask(arr, i, h, latch));
                } else {
                    if (arr[i] < arr[i - h]) {
                        int tmp = arr[i];
                        int j = i - h;
                        while (j >= 0 && arr[j] > tmp) {
                            arr[j + h] = arr[j];
                            j -= h;
                        }
                        arr[j + h] = tmp;
                    }
                    //System.out.println(Arrays.toString(arr));
                }
            }
            // 等待线程排序完成，进入下一次排序
            if (Objects.nonNull(latch)) {
                latch.await();
            }
            // 计算出下一个 h 值
            h = (h - 1) / 3;
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        int[] arr = new int[] {7, 4, 3, 8, 9, 10, 1};
        parallelShellSort(arr);
        for (int val : arr) {
            System.out.print(val + " ");
        }
    }

}

package com.siwuxie095.concurrent.chapter5th.example8th;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jiajing Li
 * @date 2020-09-25 22:21:37
 */
@SuppressWarnings("all")
class ParallelSearch {

    private static int[] arr;

    private static ExecutorService executor = Executors.newCachedThreadPool();

    private static final int THREAD_NUM = 2;

    /**
     * 用于存放符合条件的元素在 arr 数组中的下标。默认为 -1，表示没有找到给定元素
     */
    private static AtomicInteger result = new AtomicInteger(-1);

    static class SearchTask implements Callable<Integer> {

        private int searchValue;

        private int begin;

        private int end;

        public SearchTask(int searchValue, int begin, int end) {
            this.searchValue = searchValue;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public Integer call() throws Exception {
            return search(searchValue, begin, end);
        }

        private static int search(int searchValue, int begin, int end) {
            int i = 0;
            for (i = begin; i < end; i++) {
                if (result.get() >= 0) {
                    return result.get();
                }
                if (arr[i] == searchValue) {
                    // 设置失败，说明其他线程已经先找到了
                    if (!result.compareAndSet(-1, i)) {
                        return result.get();
                    }
                    return i;
                }
            }
            return -1;
        }

    }

    public static int parallelSearch(int searchValue) throws ExecutionException, InterruptedException {
        int subLen = arr.length / THREAD_NUM + 1;
        List<Future<Integer>> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i += subLen) {
            int begin = i;
            int end = i + subLen;
            if (end >= arr.length) {
                end = arr.length;
            }
            list.add(executor.submit(new SearchTask(searchValue, begin, end)));
        }
        for (Future<Integer> future : list) {
            if (future.get() >= 0) {
                return future.get();
            }
        }
        return -1;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        arr = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int index = parallelSearch(5);
        System.out.println(index);
        executor.shutdown();
    }

}

package com.siwuxie095.concurrent.chapter3rd.example17th;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author Jiajing Li
 * @date 2020-09-08 07:46:40
 */
@SuppressWarnings("all")
class CountTask extends RecursiveTask<Long> {

    private static final int THRESHOLD = 10_000;

    private long start;

    private long end;

    public CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;
        boolean canCompute = end - start < THRESHOLD;
        if (canCompute) {
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 分成 100 个小任务
            long step = (end - start) / 100;
            List<CountTask> subTaskList = new ArrayList<>();
            long from = start;
            for (int i = 0; i < 100; i++) {
                long to = from + step;
                if (to > end) {
                    to = end;
                }
                CountTask subTask = new CountTask(from, to);
                from += step + 1;
                subTaskList.add(subTask);
                subTask.fork();
            }
            for (CountTask subTask : subTaskList) {
                sum += subTask.join();
            }
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        CountTask task = new CountTask(0L, 200_000L);
        ForkJoinTask<Long> res = pool.submit(task);
        long sum = res.get();
        System.out.println("sum = " + sum);
    }

}

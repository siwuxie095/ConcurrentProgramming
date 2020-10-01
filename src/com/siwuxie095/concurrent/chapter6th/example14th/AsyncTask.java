package com.siwuxie095.concurrent.chapter6th.example14th;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Jiajing Li
 * @date 2020-10-01 16:02:20
 */
@SuppressWarnings("all")
public class AsyncTask {

    public static Integer calc(Integer value) {
        try {
            // 模拟一个长时间的执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value * value;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> calc(50));
        System.out.println(future.get());
    }

}

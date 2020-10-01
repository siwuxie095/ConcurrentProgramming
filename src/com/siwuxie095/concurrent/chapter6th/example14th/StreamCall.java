package com.siwuxie095.concurrent.chapter6th.example14th;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Jiajing Li
 * @date 2020-10-01 16:27:48
 */
@SuppressWarnings("all")
public class StreamCall {

    public static Integer calc(Integer value) {
        try {
            // 模拟一个长时间的操作
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value * value;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calc(50))
                .thenApply(i -> Integer.toString(i))
                .thenApply(str -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        future.get();
    }

}

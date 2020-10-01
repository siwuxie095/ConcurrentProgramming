package com.siwuxie095.concurrent.chapter6th.example14th;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Jiajing Li
 * @date 2020-10-01 17:00:19
 */
@SuppressWarnings("all")
public class Combine {

    public static Integer calc(Integer value) {
        return value / 2;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> intFuture1 = CompletableFuture.supplyAsync(() -> calc(50));
        CompletableFuture<Integer> intFuture2 = CompletableFuture.supplyAsync(() -> calc(25));

        CompletableFuture<Void> future = intFuture1.thenCombine(intFuture2, (i, j) -> (i + j))
                .thenApply(str -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        future.get();
    }

}

package com.siwuxie095.concurrent.chapter6th.example14th;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Jiajing Li
 * @date 2020-10-01 16:46:40
 */
@SuppressWarnings("all")
public class Compose {

    public static Integer calc(Integer value) {
        return value / 2;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calc(50))
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> calc(i)))
                .thenApply(str -> "\"" + str + "\"")
                .thenAccept(System.out::println);
        future.get();
    }

}

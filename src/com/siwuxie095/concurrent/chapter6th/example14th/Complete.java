package com.siwuxie095.concurrent.chapter6th.example14th;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Jiajing Li
 * @date 2020-10-01 15:41:11
 */
@SuppressWarnings("all")
public class Complete implements Runnable {

    private CompletableFuture<Integer> future = null;

    public Complete(CompletableFuture<Integer> future) {
        this.future = future;
    }

    @Override
    public void run() {
        int res = 0;
        try {
            res = future.get() * future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(res);
    }

    public static void main(String[] args) throws InterruptedException {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        new Thread(new Complete(future)).start();
        // 模拟长时间的计算过程
        Thread.sleep(1000);
        // 告知完成结果
        future.complete(60);
    }
}

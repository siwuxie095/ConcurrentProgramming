package com.siwuxie095.concurrent.chapter3rd.example6th;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-06 09:32:36
 */
@SuppressWarnings("all")
class CountDownLatchDemo implements Runnable {

    private static final CountDownLatch end = new CountDownLatch(10);

    private static final CountDownLatchDemo demo = new CountDownLatchDemo();

    @Override
    public void run() {
        try {
            // 模拟检查任务
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println("check complete");
            end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(demo);
        }
        // 等待检查
        end.await();
        // 发射火箭
        System.out.println("fire");
        executor.shutdown();
    }
}

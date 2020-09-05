package com.siwuxie095.concurrent.chapter3rd.example4th;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Jiajing Li
 * @date 2020-09-04 22:11:44
 */
@SuppressWarnings("all")
class SemapDemo implements Runnable {

    private final Semaphore semaphore = new Semaphore(5);

    @Override
    public void run() {
        try {
            semaphore.acquire();
            // 模拟耗时的操作
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getId() + ":done!");
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final SemapDemo r = new SemapDemo();
        for (int i = 0; i < 20; i++) {
            executorService.submit(r);
        }
    }
}

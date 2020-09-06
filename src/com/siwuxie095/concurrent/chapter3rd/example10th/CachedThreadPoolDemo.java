package com.siwuxie095.concurrent.chapter3rd.example10th;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-06 18:24:54
 */
@SuppressWarnings("all")
class CachedThreadPoolDemo {

    static class MyTask implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":Thread Id:" + Thread.currentThread().getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        MyTask task = new MyTask();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.submit(task);
        }
    }

}

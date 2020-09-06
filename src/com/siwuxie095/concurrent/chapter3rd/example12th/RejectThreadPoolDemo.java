package com.siwuxie095.concurrent.chapter3rd.example12th;

import java.util.concurrent.*;

/**
 * @author Jiajing Li
 * @date 2020-09-06 22:49:12
 */
@SuppressWarnings("all")
class RejectThreadPoolDemo {

    static class MyTask implements Runnable {

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":Thread Id:" + Thread.currentThread().getId());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        ExecutorService executor = new ThreadPoolExecutor(5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(10),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(r.toString() + " is discard");
                    }
        });

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executor.submit(task);
            Thread.sleep(10);
        }
    }



}

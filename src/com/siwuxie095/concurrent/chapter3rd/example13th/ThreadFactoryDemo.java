package com.siwuxie095.concurrent.chapter3rd.example13th;

import java.util.concurrent.*;

/**
 * @author Jiajing Li
 * @date 2020-09-06 23:13:39
 */
@SuppressWarnings("all")
class ThreadFactoryDemo {

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
                new SynchronousQueue<>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        System.out.println("create " + t);
                        return t;
                    }
                });

        for (int i = 0; i < 5; i++) {
            executor.submit(task);
        }
        Thread.sleep(2000);
    }

}

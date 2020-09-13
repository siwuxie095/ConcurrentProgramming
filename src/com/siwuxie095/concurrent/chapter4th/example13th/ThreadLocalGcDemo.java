package com.siwuxie095.concurrent.chapter4th.example13th;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-13 10:38:46
 */
@SuppressWarnings("all")
class ThreadLocalGcDemo {

    private static volatile ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected void finalize() {
            System.out.println(this.toString() + " is gc");
        }
    };

    private static volatile CountDownLatch cd = new CountDownLatch(10_000);



    static class ParseDate implements Runnable {

        private int i = 0;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                if (Objects.isNull(local.get())) {
                    local.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {
                        @Override
                        protected void finalize() {
                            System.out.println(this.toString() + " is gc");
                        }
                    });
                    System.out.println(Thread.currentThread().getId() + ":create SimpleDateFormat");
                }
                Date date = local.get().parse("2020-09-13 08:00:" + (i % 60));
                //System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                cd.countDown();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10_000; i++) {
            executor.execute(new ParseDate(i));
        }
        cd.await();
        System.out.println("mission complete");
        local = null;
        System.gc();
        Thread.sleep(3000);
        System.out.println("first GC complete");
        Thread.sleep(3000);
        local = new ThreadLocal<>();
        cd = new CountDownLatch(10_000);
        for (int i = 0; i < 10_000; i++) {
            executor.execute(new ParseDate(i));
        }
        cd.await();
        System.out.println("mission complete");
        System.gc();
        Thread.sleep(3000);
        System.out.println("second GC complete");
        executor.shutdown();
        Thread.sleep(3000);
    }


}

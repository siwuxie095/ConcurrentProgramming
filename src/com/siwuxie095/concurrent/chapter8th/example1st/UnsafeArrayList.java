package com.siwuxie095.concurrent.chapter8th.example1st;

import java.util.ArrayList;

/**
 * @author Jiajing Li
 * @date 2020-10-05 00:08:08
 */
@SuppressWarnings("all")
public class UnsafeArrayList {

    private static volatile ArrayList list = new ArrayList();

    public static class AddTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 1_000_000; i++) {
                list.add(new Object());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddTask(), "t1");
        Thread t2 = new Thread(new AddTask(), "t2");
        t1.start();
        t2.start();
        Thread t3 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t3");
        t3.start();
    }

}

package com.siwuxie095.concurrent.chapter2nd.example15th;

import java.util.ArrayList;

/**
 * @author Jiajing Li
 * @date 2020-09-02 21:54:19
 */
@SuppressWarnings("all")
class ArrayListMultiThread {

    private static ArrayList<Integer> list = new ArrayList<>(10);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread());
        Thread t2 = new Thread(new AddThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(list.size());
    }

    static class AddThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                list.add(i);
            }
        }

    }

}

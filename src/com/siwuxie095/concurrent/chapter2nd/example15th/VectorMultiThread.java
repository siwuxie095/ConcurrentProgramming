package com.siwuxie095.concurrent.chapter2nd.example15th;

import java.util.Vector;

/**
 * @author Jiajing Li
 * @date 2020-09-02 21:58:13
 */
@SuppressWarnings("all")
class VectorMultiThread {

    private static Vector<Integer> vector = new Vector<>(10);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread());
        Thread t2 = new Thread(new AddThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(vector.size());
    }

    static class AddThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 1_000_000; i++) {
                vector.add(i);
            }
        }

    }

}

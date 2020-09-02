package com.siwuxie095.concurrent.chapter2nd.example16th;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2020-09-02 22:17:50
 */
@SuppressWarnings("all")
class HashMapMultiThread {

    private static Map<String, String> map = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new AddThread(0));
        Thread t2 = new Thread(new AddThread(1));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(map.size());
    }

    static class AddThread implements Runnable {

        int start = 0;

        AddThread(int start) {
            this.start = start;
        }

        @Override
        public void run() {
            for (int i = start; i < 100_000; i+= 2) {
                map.put(Integer.toString(i), Integer.toBinaryString(i));
            }
        }
    }

}

package com.siwuxie095.concurrent.chapter2nd.example8th;

/**
 * @author Jiajing Li
 * @date 2020-08-29 08:01:29
 */
class SimpleJoin {

    private volatile static int i = 0;

    public static void main(String[] args) throws InterruptedException {
        AddThread at = new AddThread();
        at.start();
        at.join();
        System.out.println(i);
    }

    static class  AddThread extends Thread {

        @Override
        public void run() {
            for (i = 0; i < 10_000_000; i++) {}
        }

    }



}

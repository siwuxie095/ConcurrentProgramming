package com.siwuxie095.concurrent.chapter2nd.example9th;

/**
 * @author Jiajing Li
 * @date 2020-08-30 22:38:30
 */
class ComplexOperation {

    static volatile int i = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new PlusTask());
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }
        System.out.println(i);
    }

    static class PlusTask implements Runnable {

        @Override
        public void run() {
            for (int k = 0; k < 10_000; k++) {
                i++;
            }
        }
    }

}

package com.siwuxie095.concurrent.chapter2nd.example17th;

/**
 * @author Jiajing Li
 * @date 2020-09-02 23:04:13
 */
class BadLockOnInteger implements Runnable {

    private static Integer i = 0;

    private static BadLockOnInteger instance = new BadLockOnInteger();

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            synchronized (i) {
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}

package com.siwuxie095.concurrent.chapter2nd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 20:30:18
 */
@SuppressWarnings("all")
class AccountingWithVolatile implements Runnable {

    private static AccountingWithVolatile instance = new AccountingWithVolatile();

    private static volatile int i = 0;

    public static void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            increase();
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

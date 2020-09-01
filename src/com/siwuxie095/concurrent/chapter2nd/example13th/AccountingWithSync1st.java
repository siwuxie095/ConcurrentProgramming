package com.siwuxie095.concurrent.chapter2nd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 21:58:17
 */
@SuppressWarnings("all")
class AccountingWithSync1st implements Runnable {

    private static AccountingWithSync1st instance = new AccountingWithSync1st();

    private static int i = 0;

    public void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            synchronized (instance) {
                increase();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 此时 synchronized 作用于给定对象，所以必须是对同一个对象加锁，才能有效
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}

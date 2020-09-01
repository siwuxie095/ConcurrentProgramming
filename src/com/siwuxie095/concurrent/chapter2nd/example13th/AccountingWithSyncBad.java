package com.siwuxie095.concurrent.chapter2nd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 21:58:17
 */
@SuppressWarnings("all")
class AccountingWithSyncBad implements Runnable {

    private static int i = 0;

    public synchronized void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 此时 synchronized 作用于实例方法，但是却有两个实例，所以 synchronized 无效
        Thread t1 = new Thread(new AccountingWithSyncBad());
        Thread t2 = new Thread(new AccountingWithSyncBad());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}

package com.siwuxie095.concurrent.chapter2nd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 21:58:17
 */
@SuppressWarnings("all")
class AccountingWithSync2nd implements Runnable {

    private static int i = 0;

    public static void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            synchronized (AccountingWithSync2nd.class) {
                increase();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 此时 synchronized 作用于给定类，也可以和 AccountingWithSync1st 中一样，放相同的 instance 实例进来
        Thread t1 = new Thread(new AccountingWithSync2nd());
        Thread t2 = new Thread(new AccountingWithSync2nd());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}

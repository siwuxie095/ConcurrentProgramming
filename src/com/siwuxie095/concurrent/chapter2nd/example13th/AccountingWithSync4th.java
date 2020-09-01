package com.siwuxie095.concurrent.chapter2nd.example13th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 21:58:17
 */
class AccountingWithSync4th implements Runnable {

    private static int i = 0;

    public synchronized static void increase() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10_000_000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 此时 synchronized 作用于静态方法，这里就不用传入同一个实例
        Thread t1 = new Thread(new AccountingWithSync4th());
        Thread t2 = new Thread(new AccountingWithSync4th());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}

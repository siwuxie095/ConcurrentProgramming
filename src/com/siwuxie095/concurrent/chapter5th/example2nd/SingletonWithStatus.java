package com.siwuxie095.concurrent.chapter5th.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-09-18 08:08:22
 */
@SuppressWarnings("all")
class SingletonWithStatus {

    private SingletonWithStatus() {
        System.out.println("SingletonWithStatus is created");
    }

    private static int status = 1;

    private static SingletonWithStatus instance = new SingletonWithStatus();

    public static SingletonWithStatus getInstance() {
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        System.out.println(SingletonWithStatus.status);
    }

}

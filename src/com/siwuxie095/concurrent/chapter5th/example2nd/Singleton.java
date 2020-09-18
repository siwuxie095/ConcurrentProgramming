package com.siwuxie095.concurrent.chapter5th.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-09-18 08:08:22
 */
@SuppressWarnings("all")
class Singleton {

    private Singleton() {
        System.out.println("Singleton is created");
    }

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        Singleton.getInstance();
    }

}

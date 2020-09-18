package com.siwuxie095.concurrent.chapter5th.example2nd;

/**
 * @author Jiajing Li
 * @date 2020-09-18 08:55:15
 */
@SuppressWarnings("all")
class StaticSingleton {

    private StaticSingleton() {
        System.out.println("StaticSingleton is created");
    }

    public static StaticSingleton getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        private static StaticSingleton instance = new StaticSingleton();

    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        StaticSingleton.getInstance();
    }
}

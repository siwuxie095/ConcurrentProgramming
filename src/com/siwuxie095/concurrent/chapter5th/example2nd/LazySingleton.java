package com.siwuxie095.concurrent.chapter5th.example2nd;

import java.util.Objects;

/**
 * @author Jiajing Li
 * @date 2020-09-18 08:49:26
 */
@SuppressWarnings("all")
class LazySingleton {

    private LazySingleton() {
        System.out.println("LazySingleton is created");
    }

    private static LazySingleton instance = null;

    public static synchronized LazySingleton getInstance() {
        if (Objects.isNull(instance)) {
            instance = new LazySingleton();
        }
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        LazySingleton.getInstance();
    }


}

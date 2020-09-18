package com.siwuxie095.concurrent.chapter5th.example2nd;

import java.util.Objects;

/**
 * @author Jiajing Li
 * @date 2020-09-18 08:49:26
 */
@SuppressWarnings("all")
class LazySingletonWithDoubleCheck {

    private LazySingletonWithDoubleCheck() {
        System.out.println("LazySingletonWithDoubleCheck is created");
    }

    private static volatile LazySingletonWithDoubleCheck instance = null;

    public static LazySingletonWithDoubleCheck getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (LazySingletonWithDoubleCheck.class) {
                if (Objects.isNull(instance)) {
                    instance = new LazySingletonWithDoubleCheck();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(3000);
        LazySingletonWithDoubleCheck.getInstance();
    }
}

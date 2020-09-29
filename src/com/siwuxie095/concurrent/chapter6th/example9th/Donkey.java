package com.siwuxie095.concurrent.chapter6th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:54:47
 */
@SuppressWarnings("all")
public interface Donkey {

    void eat();

    default void run() {
        System.out.println("donkey run");
    }

}

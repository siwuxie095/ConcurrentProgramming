package com.siwuxie095.concurrent.chapter6th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:51:03
 */
@SuppressWarnings("all")
public interface Horse {

    void eat();

    default void run() {
        System.out.println("horse run");
    }
}

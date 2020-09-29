package com.siwuxie095.concurrent.chapter6th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:51:58
 */
@SuppressWarnings("all")
public interface Animal {

    default void breath() {
        System.out.println("breath");
    }

}

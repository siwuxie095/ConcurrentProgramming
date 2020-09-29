package com.siwuxie095.concurrent.chapter6th.example8th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:21:06
 */
@SuppressWarnings("all")
@FunctionalInterface
public interface IntHandler3rd {

    void handle(int i);

    @Override
    boolean equals(Object obj);

}

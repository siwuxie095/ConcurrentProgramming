package com.siwuxie095.concurrent.chapter6th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 21:51:33
 */
public class Main {

    /**
     * 函数式编程简介：易于并行
     *
     * 由于对象都处于不变的状态，因此函数式编程更加易于并行。实际上，甚至完全不用担心线程安全的问题。之所以要关注
     * 线程安全，一个很重要的原因是当多个线程对同一个对象进行写操作时，容易将这个对象 "写坏"。但是，由于对象是不
     * 变的，所以在多线程环境下，也就没有必要进行任何同步操作。这样不仅有利于并行化，同时，在并行化后，由于没有同
     * 步和锁机制，其性能也会比较好。
     */
    public static void main(String[] args) {

    }

}

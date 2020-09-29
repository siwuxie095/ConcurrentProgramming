package com.siwuxie095.concurrent.chapter6th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:56:10
 */
@SuppressWarnings("all")
public class Mule2nd implements Horse, Donkey, Animal {

    @Override
    public void eat() {
        System.out.println("mule eat");
    }

    @Override
    public void run() {
        Donkey.super.run();
    }

    public static void main(String[] args) {
        Mule2nd mule = new Mule2nd();
        mule.run();
        mule.breath();
    }
}

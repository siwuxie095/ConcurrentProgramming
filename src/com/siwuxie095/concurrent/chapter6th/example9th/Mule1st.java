package com.siwuxie095.concurrent.chapter6th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:52:42
 */
@SuppressWarnings("all")
public class Mule1st implements Horse, Animal {

    @Override
    public void eat() {
        System.out.println("mule eat");
    }

    public static void main(String[] args) {
        Mule1st mule = new Mule1st();
        mule.run();
        mule.breath();
    }
}

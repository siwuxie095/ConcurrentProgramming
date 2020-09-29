package com.siwuxie095.concurrent.chapter6th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-28 22:59:46
 */
@SuppressWarnings("all")
public class Mule3rd implements Horse, Donkey, Animal {

    @Override
    public void eat() {
        System.out.println("mule eat");
    }

    @Override
    public void run() {
        System.out.println("mule run");
    }

    public static void main(String[] args) {
        Mule3rd mule = new Mule3rd();
        mule.run();
        mule.breath();
    }
}

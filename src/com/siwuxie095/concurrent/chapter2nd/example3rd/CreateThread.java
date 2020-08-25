package com.siwuxie095.concurrent.chapter2nd.example3rd;

/**
 * @author Jiajing Li
 * @date 2020-08-25 07:53:02
 */
public class CreateThread implements Runnable {

    @Override
    public void run() {
        System.out.println("Oh, I am Runnable");
    }

    /**
     * 使用 Runnable 接口，避免重写 Thread.run() 方法。
     *
     * 单纯使用接口来定义 Thread，也是最常用的做法。
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(new CreateThread());
        t1.start();
    }

}

package com.siwuxie095.concurrent.chapter2nd.example11th;

/**
 * @author Jiajing Li
 * @date 2020-09-01 07:37:24
 */
public class DaemonDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new DaemonThread();
        //t.setDaemon(true);
        t.start();
        t.setDaemon(true);
        Thread.sleep(2000);
    }

    static class DaemonThread extends Thread {

        @Override
        public void run() {
            while (true) {
                System.out.println("I am alive");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

package com.siwuxie095.concurrent.chapter2nd.example9th;

/**
 * @author Jiajing Li
 * @date 2020-08-30 22:47:37
 */
class WithNoVisibility {

    private static boolean exit;

    private static int number;

    public static void main(String[] args) throws InterruptedException {
        new ReaderTread().start();
        Thread.sleep(1_000);
        number = 42;
        exit = true;
        Thread.sleep(10_000);
    }

    static class ReaderTread extends Thread {

        @Override
        public void run() {
            while (!exit) {
                System.out.println(number);
            }
        }

    }

}

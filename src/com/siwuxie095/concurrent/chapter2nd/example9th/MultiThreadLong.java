package com.siwuxie095.concurrent.chapter2nd.example9th;

/**
 * @author Jiajing Li
 * @date 2020-08-30 22:30:22
 */
@SuppressWarnings("all")
class MultiThreadLong {

    public volatile static long t = 0;

    public static void main(String[] args) {
        new Thread(new MultiThreadLong.ChangeT(111L)).start();
        new Thread(new MultiThreadLong.ChangeT(-999L)).start();
        new Thread(new MultiThreadLong.ChangeT(333L)).start();
        new Thread(new MultiThreadLong.ChangeT(-444L)).start();
        new Thread(new MultiThreadLong.ReadT()).start();
    }

    public static class ChangeT implements Runnable {
        private long to;

        public ChangeT(long to) {
            this.to = to;
        }

        @Override
        public void run() {
            while (true) {
                MultiThreadLong.t = to;
                Thread.yield();
            }
        }
    }

    public static class ReadT implements Runnable {

        @Override
        public void run() {
            while (true) {
                long tmp = MultiThreadLong.t;
                if (tmp != 111L && tmp != -999L && tmp != 333L && tmp != -444L) {
                    System.out.println(tmp);
                }
                Thread.yield();
            }
        }
    }

}

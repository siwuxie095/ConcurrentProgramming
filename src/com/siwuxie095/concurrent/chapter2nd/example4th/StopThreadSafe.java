package com.siwuxie095.concurrent.chapter2nd.example4th;

/**
 * @author Jiajing Li
 * @date 2020-08-25 22:35:52
 */
@SuppressWarnings("all")
class StopThreadSafe {

    public static User user = new User();

    public static void main(String[] args) throws InterruptedException {
        ReadObjectThread t1 = new ReadObjectThread();
        t1.start();
        while (true) {
            WriteObjectThread t2 = new WriteObjectThread();
            t2.start();
            Thread.sleep(150);
            t2.stopMe();
        }
    }


    static class WriteObjectThread extends Thread {

        volatile boolean stopMe = false;

        public void stopMe() {
            stopMe = true;
        }

        @Override
        public void run() {
            while (true) {
                if (stopMe) {
                    System.out.println("exit by stop me");
                    break;
                }
                synchronized (user) {
                    int val = (int) (System.currentTimeMillis() / 1000);
                    user.id = val;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.name = String.valueOf(val);
                }
                Thread.yield();
            }
        }

    }

    static class ReadObjectThread extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (user) {
                    if (user.id != Integer.parseInt(user.name)) {
                        System.out.println(user);
                    }
                }
                Thread.yield();
            }
        }

    }

}

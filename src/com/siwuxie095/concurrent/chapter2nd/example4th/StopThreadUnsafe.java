package com.siwuxie095.concurrent.chapter2nd.example4th;

/**
 * @author Jiajing Li
 * @date 2020-08-25 22:17:33
 */
@SuppressWarnings("all")
class StopThreadUnsafe {

    public static User user = new User();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new StopThreadUnsafe.ReadObjectThread());
        t1.start();
        while (true) {
            Thread t2 = new Thread(new StopThreadUnsafe.WriteObjectThread());
            t2.start();
            Thread.sleep(150);
            t2.stop();
        }
    }


    static class WriteObjectThread implements Runnable {

        @Override
        public void run() {
            while (true) {
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

    static class ReadObjectThread implements Runnable {

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

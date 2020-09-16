package com.siwuxie095.concurrent.chapter4th.example25th;

/**
 * @author Jiajing Li
 * @date 2020-09-16 07:40:47
 */
@SuppressWarnings("all")
class DeadLockDemo extends Thread {

    protected Object tool;

    private static Object fork1 = new Object();
    private static Object fork2 = new Object();

    public DeadLockDemo(Object tool) {
        this.tool = tool;
        if (tool == fork1) {
            this.setName("哲学家A");
        }
        if (tool == fork2) {
            this.setName("哲学家B");
        }
    }

    @Override
    public void run() {
        if (tool == fork1) {
            synchronized (fork1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (fork2) {
                    System.out.println("哲学家A开始吃饭了");
                }
            }
        }
        if (tool == fork2) {
            synchronized (fork2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (fork1) {
                    System.out.println("哲学家B开始吃饭了");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadLockDemo philosopher1 = new DeadLockDemo(fork1);
        DeadLockDemo philosopher2 = new DeadLockDemo(fork2);
        philosopher1.start();
        philosopher2.start();
        Thread.sleep(1000);
    }
}

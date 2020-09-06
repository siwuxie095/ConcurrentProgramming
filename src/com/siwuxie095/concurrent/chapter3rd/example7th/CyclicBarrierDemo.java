package com.siwuxie095.concurrent.chapter3rd.example7th;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Jiajing Li
 * @date 2020-09-06 10:10:25
 */
@SuppressWarnings("all")
class CyclicBarrierDemo {

    static class Soldier implements Runnable {

        private final CyclicBarrier cyclicBarrier;

        private String soldier;

        public Soldier(CyclicBarrier cyclicBarrier, String soldier) {
            this.cyclicBarrier = cyclicBarrier;
            this.soldier = soldier;
        }

        @Override
        public void run() {
            try {
                // 等待所有士兵到齐
                cyclicBarrier.await();
                doWork();
                // 等待所有士兵完成工作
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                System.err.println(soldier + ":InterruptedException");
            } catch (BrokenBarrierException e) {
                System.err.println(soldier + ":BrokenBarrierException");
            }
        }

        private void doWork() {
            try {
                Thread.sleep(Math.abs(new Random().nextInt() % 10_000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(soldier + ":任务完成");
        }
    }

    static class BarrierAction implements Runnable {

        boolean flag;

        int N;

        public BarrierAction(boolean flag, int n) {
            this.flag = flag;
            N = n;
        }

        @Override
        public void run() {
            if (!flag) {
                System.out.println("===== 结束集合队伍 =====");
                System.out.println("司令：[ "+ N +" 个士兵，已经集合完毕！]");
                System.out.println();
                System.out.println("===== 开始执行任务 =====");
                flag = true;
            } else {
                System.out.println("===== 结束执行任务 =====");
                System.out.println("司令：[ "+ N +" 个士兵，已经完成任务！]");

            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] soldiers = new Thread[N];
        boolean flag = false;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new BarrierAction(flag, N));
        System.out.println("===== 开始集合队伍 =====");
        for (int i = 0; i < N; i++) {
            System.out.println("士兵 " + i + " 报道");
            soldiers[i] = new Thread(new Soldier(cyclicBarrier, "士兵 " + i));
            soldiers[i].start();
            //if (i == 4) {
            //    soldiers[i].interrupt();
            //}
        }
    }

}

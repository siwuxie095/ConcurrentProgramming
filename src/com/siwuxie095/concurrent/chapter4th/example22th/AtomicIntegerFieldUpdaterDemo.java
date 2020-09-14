package com.siwuxie095.concurrent.chapter4th.example22th;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author Jiajing Li
 * @date 2020-09-14 22:33:59
 */
@SuppressWarnings("all")
class AtomicIntegerFieldUpdaterDemo {

    static class Candidate {
        int id;
        volatile  int score;
    }

    private final static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
    // 检查 Updater 是否工作正确
    private static AtomicInteger allScore = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        final Candidate candidate = new Candidate();
        Thread[] threads = new Thread[10_000];
        // 模拟计票过程
        for (int i = 0; i < 10_000; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    if (Math.random() > 0.4) {
                        // 投赞成票，修改 Candidate.score
                        scoreUpdater.incrementAndGet(candidate);
                        // 同时修改 allScore，作为参考基准
                        allScore.incrementAndGet();
                    }
                }
            };
            threads[i].start();
        }
        for (int i = 0; i < 10_000; i++) {
            threads[i].join();
        }
        System.out.println("score = " + candidate.score);
        System.out.println("allScore = " + allScore);
    }

}

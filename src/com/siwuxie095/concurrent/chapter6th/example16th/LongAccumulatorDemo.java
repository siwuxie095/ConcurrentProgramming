package com.siwuxie095.concurrent.chapter6th.example16th;

import java.util.Random;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * @author Jiajing Li
 * @date 2020-10-01 23:00:37
 */
@SuppressWarnings("all")
public class LongAccumulatorDemo {

    public static void main(String[] args) throws InterruptedException {
        LongAccumulator longAccumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> {
                Random random = new Random();
                long value = random.nextLong();
                longAccumulator.accumulate(value);
            });
            threads[i].start();
        }
        for (int i = 0; i < 1000; i++) {
            threads[i].join();
        }
        System.out.println(longAccumulator.longValue());
    }

}

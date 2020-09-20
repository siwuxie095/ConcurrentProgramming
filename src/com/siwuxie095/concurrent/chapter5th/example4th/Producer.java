package com.siwuxie095.concurrent.chapter5th.example4th;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jiajing Li
 * @date 2020-09-20 16:46:03
 */
@SuppressWarnings("all")
public class Producer implements Runnable {

    private volatile boolean isRunning = true;

    /**
     * 内存缓冲区（任务队列）
     */
    private BlockingQueue<ProducedConsumedData> queue;

    /**
     * 总数
     */
    private static AtomicInteger count = new AtomicInteger();

    private static final int SLEEP_TIME = 1000;

    public Producer(BlockingQueue<ProducedConsumedData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        ProducedConsumedData data = null;
        Random random = new Random();
        System.out.println("start producer id = " + Thread.currentThread().getId());

        try {
            while (isRunning) {
                // 随机休眠 1000 以内的毫秒数
                Thread.sleep(random.nextInt(SLEEP_TIME));
                // 构造任务数据
                data = new ProducedConsumedData(count.incrementAndGet());
                System.out.println(data + " is put into queue");
                // 提交数据到缓冲区中
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println("failed to put data: " + data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        isRunning = false;
    }
}

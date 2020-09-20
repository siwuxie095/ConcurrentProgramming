package com.siwuxie095.concurrent.chapter5th.example4th;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-20 16:56:10
 */
@SuppressWarnings("all")
public class Consumer implements Runnable {

    private volatile boolean isRunning = true;

    /**
     * 内存缓冲区（任务队列）
     */
    private BlockingQueue<ProducedConsumedData> queue;

    private static final int SLEEP_TIME = 1000;

    public Consumer(BlockingQueue<ProducedConsumedData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("start consumer id = " + Thread.currentThread().getId());
        Random random = new Random();
        try {
            while (isRunning) {
                // 提取任务
                ProducedConsumedData data = queue.poll();
                if (Objects.nonNull(data)) {
                    // 计算平方
                    int res = data.getData() * data.getData();
                    System.out.println(MessageFormat.format("{0} * {1} = {2}", data.getData(), data.getData(), res));
                    Thread.sleep(random.nextInt(SLEEP_TIME));
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

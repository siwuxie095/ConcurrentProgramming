package com.siwuxie095.concurrent.chapter3rd.example10th;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiajing Li
 * @date 2020-09-06 18:41:39
 */
@SuppressWarnings("all")
class ScheduledThreadPoolDemo {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(1000);
                //Thread.sleep(8000);
                // 除以 1000 后，输出单位为秒
                System.out.println(System.currentTimeMillis() / 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

}

package com.siwuxie095.concurrent.chapter5th.example7th;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-25 08:24:52
 */
@SuppressWarnings("all")
class Plus implements Runnable {

    public static BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (true) {
            try {
                Msg msg = queue.take();
                msg.c = msg.b + msg.c;
                Multiply.queue.add(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

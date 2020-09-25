package com.siwuxie095.concurrent.chapter5th.example7th;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-25 08:27:20
 */
@SuppressWarnings("all")
class Multiply implements Runnable {

    public static BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (true) {
            Msg msg = null;
            try {
                msg = queue.take();
                msg.b = msg.b * msg.c;
                Divide.queue.add(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.siwuxie095.concurrent.chapter5th.example7th;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Jiajing Li
 * @date 2020-09-25 08:30:21
 */
@SuppressWarnings("all")
class Divide implements Runnable {

    public static BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (true) {
            try {
                Msg msg = queue.take();
                msg.b = msg.b / 2;
                System.out.println(msg.exp + " = " + msg.b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

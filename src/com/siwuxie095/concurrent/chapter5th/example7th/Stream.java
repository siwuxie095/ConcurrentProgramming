package com.siwuxie095.concurrent.chapter5th.example7th;

/**
 * @author Jiajing Li
 * @date 2020-09-25 08:37:24
 */
@SuppressWarnings("all")
class Stream {

    public static void main(String[] args) {
        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Divide()).start();

        for (int i = 1; i <= 1000; i++) {
            for (int j = 1; j <= 1000; j++) {
                Msg msg = new Msg();
                msg.b = i;
                msg.c = j;
                msg.exp = "((" + i + " + " + j + ") * " + i + ") / 2";
                Plus.queue.add(msg);
            }
        }
    }

}

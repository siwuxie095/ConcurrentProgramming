package com.siwuxie095.concurrent.chapter5th.example5th;

import com.lmax.disruptor.WorkHandler;

/**
 * @author Jiajing Li
 * @date 2020-09-21 22:48:13
 */
@SuppressWarnings("all")
public class Consumer implements WorkHandler<ProducedConsumedData> {

    @Override
    public void onEvent(ProducedConsumedData event) throws Exception {
        System.out.println(Thread.currentThread().getId() + ":Event: -- " + event.getValue() * event.getValue() + " -- ");
    }
}

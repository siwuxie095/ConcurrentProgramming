package com.siwuxie095.concurrent.chapter5th.example5th;

import com.lmax.disruptor.EventFactory;

/**
 * @author Jiajing Li
 * @date 2020-09-21 22:53:08
 */
@SuppressWarnings("all")
public class ProducedConsumedDataFactory implements EventFactory<ProducedConsumedData> {

    @Override
    public ProducedConsumedData newInstance() {
        return new ProducedConsumedData();
    }
}

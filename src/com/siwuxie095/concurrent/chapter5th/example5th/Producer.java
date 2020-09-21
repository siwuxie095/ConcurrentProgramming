package com.siwuxie095.concurrent.chapter5th.example5th;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author Jiajing Li
 * @date 2020-09-21 22:54:39
 */
@SuppressWarnings("all")
public class Producer {

    private final RingBuffer<ProducedConsumedData> ringBuffer;

    public Producer(RingBuffer<ProducedConsumedData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void pushData(ByteBuffer byteBuffer) {
        // grab the next sequence
        long sequence = ringBuffer.next();
        try {
            // get the entry in the disruptor by the sequence
            ProducedConsumedData event = ringBuffer.get(sequence);
            // fill with data
            event.setValue(byteBuffer.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}

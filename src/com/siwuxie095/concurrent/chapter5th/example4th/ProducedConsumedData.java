package com.siwuxie095.concurrent.chapter5th.example4th;

/**
 * 任务：生产者生产的数据（也是消费者消费的数据）
 *
 * @author Jiajing Li
 * @date 2020-09-20 16:42:43
 */
@SuppressWarnings("all")
public final class ProducedConsumedData {

    private final int data;

    public ProducedConsumedData(int data) {
        this.data = data;
    }

    public ProducedConsumedData(String data) {
        this.data = Integer.valueOf(data);
    }

    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ProducedConsumedData{" +
                "data=" + data +
                '}';
    }
}

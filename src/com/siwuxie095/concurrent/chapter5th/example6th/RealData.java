package com.siwuxie095.concurrent.chapter5th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-23 08:54:23
 */
@SuppressWarnings("all")
class RealData implements Data {

    protected final String result;

    public RealData(String param) {
        // RealData 的构造过程可能很慢，需要用户等待很久，这里使用 sleep 模拟
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(param);
            try {
                // 模拟一个很慢的操作
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.result = builder.toString();
    }

    @Override
    public String getResult() {
        return result;
    }
}

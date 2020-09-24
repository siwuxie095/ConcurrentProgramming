package com.siwuxie095.concurrent.chapter5th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-23 08:54:08
 */
@SuppressWarnings("all")
class FutureData implements Data {
    /**
     * FutureData 是 RealData 的包装
     */
    protected RealData realData = null;

    protected boolean isReady = false;

    public synchronized void setRealData(RealData realData) {
        if (isReady) {
            return;
        }
        this.realData = realData;
        isReady = true;
        // RealData 已经被注入，通知 getResult()
        notifyAll();
    }

    /**
     * 等待 RealData 构造完成
     * @return
     */
    @Override
    public synchronized String getResult() {
        while (!isReady) {
            try {
                // 一直等待，直到 RealData 被注入
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.getResult();
    }
}

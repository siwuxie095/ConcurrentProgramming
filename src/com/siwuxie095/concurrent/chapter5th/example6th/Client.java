package com.siwuxie095.concurrent.chapter5th.example6th;

/**
 * @author Jiajing Li
 * @date 2020-09-23 08:53:32
 */
@SuppressWarnings("all")
class Client {

    public Data request(final String queryStr) {
        final FutureData futureData = new FutureData();
        // RealData 的构建过程很慢，所以在单独的线程中进行
        new Thread() {
            @Override
            public void run() {
                RealData realData = new RealData(queryStr);
                futureData.setRealData(realData);
            }
        }.start();
        // FutureData 会被立即返回
        return futureData;
    }

}

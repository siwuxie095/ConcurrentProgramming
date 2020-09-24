package com.siwuxie095.concurrent.chapter5th.example6th;

import java.util.concurrent.Callable;

/**
 * @author Jiajing Li
 * @date 2020-09-24 08:30:30
 */
@SuppressWarnings("all")
class JdkRealData implements Callable<String> {

    private String param;

    public JdkRealData(String param) {
        this.param = param;
    }

    @Override
    public String call() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(param);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}

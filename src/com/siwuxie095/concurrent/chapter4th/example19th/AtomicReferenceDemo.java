package com.siwuxie095.concurrent.chapter4th.example19th;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Jiajing Li
 * @date 2020-09-14 07:37:24
 */
@SuppressWarnings("all")
class AtomicReferenceDemo {

    private static AtomicReference<Integer> money = new AtomicReference<>();

    public static void main(String[] args) {
        // 设置账户初始值小于 20，显然，这是一个需要被充值的账户
        money.set(19);
        // 商家赠予线程：模拟多个线程同时更新后台数据库，为用户充值
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        while (true) {
                            Integer m = money.get();
                            if (m < 20) {
                                if (money.compareAndSet(m, m + 20)) {
                                    System.out.println("余额小于 20 元，充值成功，余额：" + money.get() + " 元");
                                    break;
                                }
                            } else {
                                System.out.println("余额大于 20 元，无须充值");
                                break;
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        // 用户消费线程：模拟消费行为
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    while (true) {
                        Integer m = money.get();
                        if (m > 10) {
                            System.out.println("大于 10 元");
                            if (money.compareAndSet(m, m - 10)) {
                                System.out.println("成功消费 10 元，余额：" + money.get() + " 元");
                                break;
                            }
                        } else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}

package com.siwuxie095.concurrent.chapter4th.example20th;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Jiajing Li
 * @date 2020-09-14 08:33:36
 */
@SuppressWarnings("all")
class AtomicStampedReferenceDemo {

    // 设置账户初始值小于 20，同时设置时间戳为 0。显然，这是一个需要被充值的账户
    private static AtomicStampedReference<Integer> money = new AtomicStampedReference<>(19, 0);

    public static void main(String[] args) {
        // 商家赠予线程：模拟多个线程同时更新后台数据库，为用户充值
        for (int i = 0; i < 3; i++) {
            final int stamp = money.getStamp();
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        while (true) {
                            Integer m = money.getReference();
                            if (m < 20) {
                                if (money.compareAndSet(m, m + 20, stamp, stamp + 1)) {
                                    System.out.println("余额小于 20 元，充值成功，余额：" + money.getReference() + " 元");
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
                        int stamp = money.getStamp();
                        Integer m = money.getReference();
                        if (m > 10) {
                            System.out.println("大于 10 元");
                            if (money.compareAndSet(m, m - 10, stamp, stamp + 1)) {
                                System.out.println("成功消费 10 元，余额：" + money.getReference() + " 元");
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

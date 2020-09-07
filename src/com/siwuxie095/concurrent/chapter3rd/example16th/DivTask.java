package com.siwuxie095.concurrent.chapter3rd.example16th;

import java.util.concurrent.*;

/**
 * @author Jiajing Li
 * @date 2020-09-07 21:55:34
 */
@SuppressWarnings("all")
class DivTask implements Runnable {

    private int a;
    private int b;

    public DivTask(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double res = a / b;
        System.out.println(res);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(0,
                Integer.MAX_VALUE,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());

        for (int i = 0; i < 5; i++) {
            // 线程池会吃掉异常堆栈
            executor.submit(new DivTask(100, i));
            // 获取部分异常堆栈 法一
            //executor.execute(new DivTask(100, i));
            // 获取部分异常堆栈 法二
            //Future future = executor.submit(new DivTask(100, i));
            //future.get();
        }
    }
}

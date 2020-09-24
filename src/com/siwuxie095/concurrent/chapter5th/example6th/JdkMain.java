package com.siwuxie095.concurrent.chapter5th.example6th;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Jiajing Li
 * @date 2020-09-24 08:32:50
 */
@SuppressWarnings("all")
class JdkMain {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 构造 FutureTask
        FutureTask<String> futureTask = new FutureTask<>(new JdkRealData("JDK "));
        ExecutorService executor = Executors.newFixedThreadPool(1);
        //
        /*
         * 执行 FutureTask，相当于当前包中的另一个例子 client.request("DIY ") 发送请求。
         * 开启线程进行 JdkRealData 的 call() 执行。
         */
        executor.submit(futureTask);
        System.out.println("请求完毕");
        /*
         * 这里依然可以做额外的业务逻辑的处理，用一个 sleep 代替。
         */
        Thread.sleep(2000);
        // 等待获取真实的数据：如果 call() 方法没有执行完成，依然会等待
        System.out.println("数据 = " + futureTask.get());
        executor.shutdown();
    }

}

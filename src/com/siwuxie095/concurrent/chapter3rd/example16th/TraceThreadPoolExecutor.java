package com.siwuxie095.concurrent.chapter3rd.example16th;

import java.util.concurrent.*;

/**
 * @author Jiajing Li
 * @date 2020-09-07 22:37:38
 */
class TraceThreadPoolExecutor extends ThreadPoolExecutor {

    public TraceThreadPoolExecutor(int corePoolSize,
                                   int maximumPoolSize,
                                   long keepAliveTime,
                                   TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, clientTrace()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, clientTrace()));
    }

    private Exception clientTrace() {
        return new Exception("client thread name = " + Thread.currentThread().getName() + ", client stack trace as following");
    }

    private Runnable wrap(final Runnable task, final Exception clientStack) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                clientStack.printStackTrace();
                throw e;
            }
        };
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = new TraceThreadPoolExecutor(0,
                Integer.MAX_VALUE,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
        /*
         * 异常堆栈中可以看到是在哪里提交的任务
         */
        for (int i = 0; i < 5; i++) {
            executor.execute(new DivTask(100, i));
        }
    }
}

package com.siwuxie095.concurrent.chapter5th.example5th;

/**
 * @author Jiajing Li
 * @date 2020-09-22 22:37:49
 */
@SuppressWarnings("all")
public class FalseSharing implements Runnable {
    /**
     * change to the num of your real physical cpu
     */
    private static final int NUM_THREADS = 4;

    private static final long ITERATIONS = 500L * 1000L * 1000L;

    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }

    final static class VolatileLong {

        private volatile long value = 0L;
        // comment out
        private long p1, p2, p3, p4, p5, p6, p7;
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        runTest();
        long end = System.currentTimeMillis();
        System.out.println("duration: " + (end - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

}

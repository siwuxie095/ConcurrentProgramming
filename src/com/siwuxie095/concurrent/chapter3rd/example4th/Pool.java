package com.siwuxie095.concurrent.chapter3rd.example4th;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Jiajing Li
 * @date 2020-09-04 22:20:45
 */
@SuppressWarnings("all")
class Pool {

    private static final int MAX_AVAILABLE = 5;
    
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    private Object[] items = new Object[MAX_AVAILABLE];
    private boolean[] used = new boolean[MAX_AVAILABLE];

    Pool() {
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            items[i] = i;
        }
    }
    
    public Object getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }
    
    public void putItem(Object item) {
        if (markAsUnused(item)) {
            available.release();
        }
    }


    private synchronized Object getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        // not reached
        return null;
    }

    private boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            if (item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        Pool pool = new Pool();
        for (int i = 0; i < 30; i++) {
            executorService.submit(() -> {
                try {
                    Object item = pool.getItem();
                    System.out.println("item = " + item);
                    Thread.sleep(1000);
                    pool.putItem(item);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}

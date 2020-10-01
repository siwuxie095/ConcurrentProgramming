package com.siwuxie095.concurrent.chapter6th.example15th;

import java.util.concurrent.locks.StampedLock;

/**
 * @author Jiajing Li
 * @date 2020-10-01 17:49:11
 */
@SuppressWarnings("all")
public class Point {

    private double x, y;

    private final StampedLock stampedLock = new StampedLock();

    /**
     * 写方法
     */
    public void move(double deltaX, double deltaY) {
        // 悲观
        long stamp = stampedLock.writeLock();
        try {
            x += deltaX;
            y+= deltaY;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    /**
     * 只读方法
     */
    double distanceFromOrigin() {
        // 乐观
        long stamp = stampedLock.tryOptimisticRead();
        double currentX = x, currentY = y;
        if (!stampedLock.validate(stamp)) {
            // 悲观
            stamp = stampedLock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    public void moveIfAtOrigin(double newX, double newY) {
        long stamp = stampedLock.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                /*
                 * 试图转为写锁（尝试将获取的读锁升级为写锁）
                 *
                 * 返回 1，表示已经是写模式；
                 * 返回 2，表示读模式，但没有更多的读取者；
                 * 返回 3，表示乐观模式并且这个锁可能被获取。
                 */
                long ws = stampedLock.tryConvertToWriteLock(stamp);
                // 升级成功，更新 stamp，并设置坐标值，然后退出循环
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    // 读锁升级写锁失败，显式获取写锁，然后循环重试
                    stampedLock.unlockRead(stamp);
                    stamp = stampedLock.writeLock();
                }
            }
        } finally {
            stampedLock.unlock(stamp);
        }
    }

}

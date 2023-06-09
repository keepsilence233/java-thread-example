package qx.leizige.thread.lock.locks;


import java.util.concurrent.locks.StampedLock;

/**
 * stampedLock
 * <p>
 * StampedLock是比ReentrantReadWriteLock更快的一种锁，StampedLock 支持三种模式，分别是:写锁、悲观读锁和乐观读，不支持可重入
 * ReadWriteLock 支持多个线程同时读，但是当多个线程同时读的时候，所有的写操作会被阻塞
 * StampedLock 提供的乐观读，是允许一个线程获取写锁的，也就是说不是所有的写操作都被阻塞
 * </p>
 */
public class StampedLockExample {


    /**
     * 以下是JDK1.8中StampedLock自带的示例
     */
    class Point {
        private double x, y;
        private final StampedLock sl = new StampedLock();

        void move(double deltaX, double deltaY) {
            long stamp = sl.writeLock();    //涉及对共享资源的修改，使用写锁-独占操作
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        /**
         * 只读方法
         */
        double distanceFromOrigin() {
            long stamp = sl.tryOptimisticRead();    // 使用乐观读锁
            double currentX = x, currentY = y;      // 拷贝共享资源到本地方法栈中
            if (!sl.validate(stamp)) {              // 如果有写锁被占用，可能造成数据不一致，所以要切换到普通读锁模式
                stamp = sl.readLock();
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }

        void moveIfAtOrigin(double newX, double newY) { // upgrade
            // Could instead start with optimistic, not read mode
            long stamp = sl.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = sl.tryConvertToWriteLock(stamp);  //读锁转换为写锁
                    if (ws != 0L) {
                        stamp = ws;
                        x = newX;
                        y = newY;
                        break;
                    } else {
                        sl.unlockRead(stamp);
                        stamp = sl.writeLock();
                    }
                }
            } finally {
                sl.unlock(stamp);
            }
        }
    }
}

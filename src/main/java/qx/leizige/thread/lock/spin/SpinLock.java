package qx.leizige.thread.lock.spin;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自旋锁
 *
 * @author leizige
 */
public class SpinLock {


    private AtomicBoolean ab = new AtomicBoolean(false);


    // 循环检测尝试获取锁
    public void lock() {
        while (!tryLock()) {
            // doSomething...
        }
    }


    public boolean tryLock() {
        // 尝试获取锁，成功返回true，失败返回false
        return ab.compareAndSet(false, true);
    }

    /**
     * 释放锁
     */
    public void unLock() {
        ab.set(false);
    }

    public static void main(String[] args) {

    }

}

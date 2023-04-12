package qx.leizige.thread.lock.locks;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author leizige
 * created 2021/08/10
 */
public class ReentrantLockExample {

    /**
     * ReentrantLock 默认非公平锁
     * 可重入锁原理：
     * <p>
     * 可重入锁的原理是在锁内部维护一个线程标示，用来标示该锁目前被哪个线程占用，然后关联一个计数器。
     * 一开始计数器值为 0，说明该锁没有被任何线程占用。当一个线程获取了该锁时，计数器的值会变成 1，这时其他线程再来获取该锁时会发现锁的所有者不是自己而被阻塞挂起。
     * 但是当获取了该锁的线程再次获取锁时发现锁拥有者是自己，就会把计数器值加＋1, 当释放锁后计数器值－1。
     * 当计数器值为 0 时，锁里面的线程标示被重置为 null，这时候被阻塞的线程会被唤醒来竞争获取该锁。
     * </p>
     */
    private static final Lock lock = new ReentrantLock(true);

    public static void method1() {
        try {
            lock.lock();
            System.out.println("method1方法获得锁 ......");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("method1方法释放锁 ......");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void method2() {
        try {
            lock.lock();
            System.out.println("method2方法获得锁 ......");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("method2方法释放锁 ......");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    static class Task1 implements Runnable {
        @Override
        public void run() {
            method1();
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            method2();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(new Task1()).start();
            new Thread(new Task2()).start();
        }
        TimeUnit.SECONDS.sleep(7);
    }
}

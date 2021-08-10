package qx.leizige.thread.lock.reentrant;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author leizige
 * created 2021/08/10
 */
public class ReentrantLockTest {

    /**
     * ReentrantLock 默认非公平锁
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

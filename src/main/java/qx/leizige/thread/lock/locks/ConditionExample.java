package qx.leizige.thread.lock.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionExample {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private volatile Boolean ready = Boolean.FALSE;

    public static void main(String[] args) {
        ConditionExample test = new ConditionExample();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task 1 is done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test.prepare();
        };

        Runnable task2 = () -> {
            test.waitForReady();
            System.out.println("Task 2 is done");
        };

        executor.submit(task1);
        executor.submit(task2);

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForReady() {
        lock.lock();
        try {
            while (!ready) {
                condition.await();
            }
            System.out.println("Ready...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private void prepare() {
        lock.lock();
        try {
            ready = Boolean.TRUE;
            condition.signalAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}

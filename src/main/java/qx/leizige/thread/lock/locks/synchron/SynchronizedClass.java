package qx.leizige.thread.lock.locks.synchron;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * created 2021/08/10
 */
public class SynchronizedClass {

    public void method1() {
        synchronized (SynchronizedClass.class) {
            System.out.println("method1方法获得锁 ......");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("method1方法释放锁 ......");
        }
    }

    public void method2() {
        synchronized (SynchronizedClass.class) {
            System.out.println("method2方法获得锁 ......");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("method2方法释放锁 ......");
        }
    }


    static class Task1 implements Runnable {

        private SynchronizedClass synchronizedClass;

        Task1(SynchronizedClass synchronizedClass) {
            this.synchronizedClass = synchronizedClass;
        }

        @Override
        public void run() {
            synchronizedClass.method1();
        }
    }

    static class Task2 implements Runnable {

        private SynchronizedClass synchronizedClass;

        Task2(SynchronizedClass synchronizedClass) {
            this.synchronizedClass = synchronizedClass;
        }

        @Override
        public void run() {
            synchronizedClass.method2();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        SynchronizedClass sync = new SynchronizedClass();
        for (int i = 0; i < 3; i++) {
            new Thread(new Task1(sync)).start();
            new Thread(new Task2(sync)).start();
        }
        TimeUnit.SECONDS.sleep(7);
    }
}

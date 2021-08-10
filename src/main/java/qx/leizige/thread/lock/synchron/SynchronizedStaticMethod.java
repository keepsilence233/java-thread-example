package qx.leizige.thread.lock.synchron;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * created 2021/08/10
 */
public class SynchronizedStaticMethod {

    public static synchronized void method1() {
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

    public static synchronized void method2() {
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

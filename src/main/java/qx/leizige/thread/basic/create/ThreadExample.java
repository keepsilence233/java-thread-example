package qx.leizige.thread.basic.create;

import java.util.concurrent.TimeUnit;

/**
 * 通过扩展Thread创建线程
 */
public class ThreadExample {

    public static void main(String[] args) {
        MyThread myThread1 = new MyThread();
        myThread1.start();


        MyThread myThread2 = new MyThread();
        myThread2.start();

        System.out.println(Thread.currentThread().getName());
    }


    private static class MyThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

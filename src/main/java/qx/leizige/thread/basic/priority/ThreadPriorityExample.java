package qx.leizige.thread.basic.priority;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * 线程优先级
 * 2022/04/04
 */
public class ThreadPriorityExample {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.setName("thread 1");
        thread.setPriority(Thread.MIN_PRIORITY);    //更改线程优先级
        thread.start();

        Thread thread2 = new MyThread();
        thread2.setName("thread 2");
        thread2.setPriority(Thread.MAX_PRIORITY);
        thread2.start();
    }

    private static class MyThread extends Thread{
        private int c;
        @Override
        public void run() {
            String threadName = Thread.currentThread()
                    .getName();

            System.out.println(threadName + " started.");
            for (int i = 0; i < 1000; i++) {
                c++;
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(threadName + " ended.");
        }
    }
}

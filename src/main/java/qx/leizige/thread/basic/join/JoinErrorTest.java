package qx.leizige.thread.basic.join;

import java.util.concurrent.TimeUnit;

public class JoinErrorTest {
    public static void main(String[] args) {

        Thread thread_a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ",begin run ......");
            while (true) {
                //......
            }
        }, "thread_a");

        Thread mainThread = Thread.currentThread();

        Thread thread_b = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainThread.interrupt();
        }, "thread_b");

        thread_a.start();
        thread_b.start();

        try {
            thread_a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

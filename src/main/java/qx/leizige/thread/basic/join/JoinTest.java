package qx.leizige.thread.basic.join;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * created 2021/08/09
 */
public class JoinTest {
    /**
     * Thread#join():由Thread类直接提供,无参(可自定义等待时间),返回值为void
     * 使用场景:等待某几件事情完成后才能继续往下执行,比如多个线程执行加载资源,需要等待多个线程加载完毕后汇总处理
     */
    public static void main(String[] args) throws InterruptedException {

        Thread thread_a = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ",over ......");
        }, "thread_a");

        Thread thread_b = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ",over ......");
        }, "thread_b");

        Thread thread_c = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ",over ......");
        }, "thread_c");

        //start thread
        thread_a.start();
        thread_b.start();
        thread_c.start();

        System.out.println("wait all thread over ......");

        long start = System.currentTimeMillis();

        //等待子线程执行完毕返回
        thread_a.join();
        thread_b.join();
        thread_c.join();

        long end = System.currentTimeMillis();

        System.out.println("all thread over ......");
        System.err.println("耗时:" + (end - start) / 1000);

    }
}

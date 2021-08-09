package qx.leizige.thread.basic.sleep;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author leizige
 * created 2021/08/09
 */
public class SleepTest {

    //独占锁
    private static final Lock lock = new ReentrantLock();

    /**
     * Thread 类中的静态方法 sleep
     * 当一个线程调用sleep方法后,调用线程会暂时让出指定时间的执行权，也就是在这期间不参与CPU的调度，但是该线程所拥有的监视器资源，比如锁还是持有不让出的
     * 睡眠时间结束后该函数正常返回，线程就处于就绪状态，然后参与CPU的调度，获取到CPU资源后就可以继续运行了
     */
    public static void main(String[] args) {


        Thread thread_a = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + ", sleep ......");
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + ", awaked ......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread_a");

        Thread thread_b = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + ", sleep ......");
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + ", awaked ......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "thread_b");

        //启动线程
        thread_a.start();
        thread_b.start();

    }
}

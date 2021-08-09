package qx.leizige.thread.basic.yield;

/**
 * @author leizige
 * created 2021/08/09
 */
public class YieldTest implements Runnable {

    /**
     * yield():让出cpu执行权
     * 一个线程调用yield方法时，当前线程会让出CPU使用权，然后处于就绪状态，线程调度器会从线程就绪队列里面获取一个线程优先级最高的线程，当然也有可能会调度到刚刚让出CPU的那个线程来获取CPU执行权。
     */
    YieldTest() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            if (i % 5 == 0) {
                System.out.println(Thread.currentThread() + " yield cpu......");

                Thread.yield();  //注释掉这里看执行结果区别
            }
        }
        System.out.println(Thread.currentThread() + " is over ......");
    }

    public static void main(String[] args) {
        new YieldTest();
        new YieldTest();
        new YieldTest();
    }
}

package qx.leizige.thread.lock.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * 先调用unpark，后调用park
 * {@link <a href="https://pdai.tech/md/java/thread/java-thread-x-lock-LockSupport.html">...</a>}
 */
public class LockSupportExample2 {

    public static void main(String[] args) {
        MyThread myThread = new MyThread(Thread.currentThread());
        myThread.start();
        try {
            // 主线程睡眠3s
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before park");
        // 获取许可
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("after park");
    }


    static class MyThread extends Thread {

        private Object object;

        public MyThread(Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            System.out.println("before unpark");
            // 释放许可
            LockSupport.unpark((Thread) object);
            System.out.println("after unpark");
        }
    }


}

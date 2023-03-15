package qx.leizige.thread.lock.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * 先调用park，后调用unpark
 * {@link <a href="https://pdai.tech/md/java/thread/java-thread-x-lock-LockSupport.html">...</a>}
 */
public class LockSupportExample1 {

    public static void main(String[] args) {
        MyThread myThread = new MyThread(Thread.currentThread());
        myThread.start();
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 获取blocker
            System.out.println("Blocker info " + LockSupport.getBlocker((Thread) object));
            // 释放许可
            LockSupport.unpark((Thread) object);
            // 休眠500ms，保证先执行park中的setBlocker(t, null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 再次获取blocker
            System.out.println("Blocker info " + LockSupport.getBlocker((Thread) object));

            System.out.println("after unpark");
        }
    }


}

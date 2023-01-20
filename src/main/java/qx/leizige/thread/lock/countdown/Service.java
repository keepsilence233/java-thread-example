package qx.leizige.thread.lock.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * 所有服务的基类，具体实现在execute方法实现。
 */
public abstract class Service implements Runnable {
    private final CountDownLatch latch;

    public Service(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            execute();
        } finally {
            if (latch != null)
                latch.countDown();
        }
    }

    public abstract void execute();
}
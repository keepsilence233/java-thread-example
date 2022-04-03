package qx.leizige.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author leizige
 * 线程池测试
 * 2022/04/03
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws InterruptedException {


//        ThreadPoolExecutor executor = new ThreadPoolExecutor(10,
//                20, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(15),
//                new MyThreadFactory()
//        );

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(15));

        for (int i = 1; i < 100; i++) {
            int finalI = i;
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "-----------" + finalI);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }


    }
}

class MyThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    MyThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
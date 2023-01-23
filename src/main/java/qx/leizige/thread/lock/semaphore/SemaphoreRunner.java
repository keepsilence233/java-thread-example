package qx.leizige.thread.lock.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore 信号量，许可，用于控制在一段时间内，可并发访问执行的线程数量
 */
public class SemaphoreRunner {


    private final static int THREAD_NUM = 30;

    private final static ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);

    private final static Semaphore semaphore = new Semaphore(10);

    /**
     * 假如需读取几个万个文件的数据，因为都是IO密集型，我们可以启动几十个线程并发的读取，但是如果读取到内存后，还需要存储到数据库，而数据库的连接数只有10个，
     * 这时候我们就必须要控制只有10个线程同时获取到数据库连接，否则会抛出异常提示无法连接数据库。针对这种情况，我们就可以使用Semaphore来做流量控制。
     */
    public static void main(String[] args) {
        for (int i = 0; i < THREAD_NUM; i++) {
            executor.execute(() -> {
                try {
                    // 获取一个"许可证"
                    semaphore.acquire();

                    // 模拟数据保存
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("save date...");

                    // 执行完后,归还"许可证"
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
}

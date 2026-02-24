package qx.leizige.thread.lock.semaphore;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EnhancedSemaphoreExample {
    // 1. 定义信号量：模拟 3 个数据库连接
    private static final Semaphore DB_CONNECTIONS = new Semaphore(3);
    private static final AtomicInteger ACTIVE_DB_USERS = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // 2. 线程池：核心线程10个，队列容量100
        // 这样即使 10 个任务同时来，也不会因为没有线程而阻塞或丢失
        ExecutorService businessPool = new ThreadPoolExecutor(
                10, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy() // 策略：如果队列满了，让调用者自己跑，防止丢失
        );

        System.out.println("--- 系统启动：准备处理 10 个并发请求，数据库连接限流 3 ---");

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            businessPool.execute(() -> {
                System.out.println("任务 " + taskId + " [业务层]: 已进入线程池，准备申请数据库连接...");

                try {
                    // 3. 在线程内部申请信号量
                    // 注意：这里是线程池里的线程在等，而不是产生任务的线程在等
                    DB_CONNECTIONS.acquire();

                    int currentUsers = ACTIVE_DB_USERS.incrementAndGet();
                    System.out.println(">>> 任务 " + taskId + " [数据库层]: 成功拿到连接！当前占用数: " + currentUsers);

                    // 模拟数据库操作耗时
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 4. 务必释放，给队列里的后续任务机会
                    int remainingUsers = ACTIVE_DB_USERS.decrementAndGet();
                    System.out.println("<<< 任务 " + taskId + " [数据库层]: 完成，释放连接。当前占用数: " + remainingUsers);
                    DB_CONNECTIONS.release();
                }
            });
        }

        businessPool.shutdown();
        businessPool.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("--- 所有任务处理完毕 ---");
    }
}

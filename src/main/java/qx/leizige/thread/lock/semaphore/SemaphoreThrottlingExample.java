package qx.leizige.thread.lock.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreThrottlingExample {

    public static void main(String[] args) {
        // 1. 定义限流阈值：假设我们只有 3 个公共资源（比如只有3个数据库连接）
        Semaphore semaphore = new Semaphore(3);

        // 模拟 10 个用户同时发起请求
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            final int userId = i;
            executorService.execute(() -> {
                try {
                    // 2. 尝试获取许可 (Acquire)
                    System.out.println("用户 " + userId + " 正在排队等待进入资源...");

                    // 获取许可，如果没有可用许可，线程会在此阻塞
                    //可以尝试 tryAcquire or tryAcquire(1000) 如果大量线程通过 tryAcquire 挂起阻塞或者直接丢弃任务,都不是一个很好的解决方案
                    // 可以通过 EnhancedSemaphoreExample 中的线程池队列做缓冲(减少阻塞挂起的线程数量) 或者使用 Guava 的 RateLimiter
                    semaphore.acquire();

                    System.out.println(">>> 用户 " + userId + " 成功抢到资源，开始处理业务...");

                    // 模拟业务处理耗时
                    long duration = (long) (Math.random() * 5);
                    TimeUnit.SECONDS.sleep(duration);

                    System.out.println("<<< 用户 " + userId + " 业务处理完成，释放资源。");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 3. 释放许可 (Release) - 必须放在 finally 中确保一定会被释放
                    semaphore.release();
                }
            });
        }

        executorService.shutdown();
    }
}
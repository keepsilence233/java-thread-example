package qx.leizige.thread.lock.semaphore;

import com.google.common.util.concurrent.RateLimiter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class ControllerRateLimitSimulator {

    // 1. 定义限流器：每秒只放行 2 个令牌 (QPS = 2)
    private static final RateLimiter limiter = RateLimiter.create(2.0);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        // 模拟 Tomcat 线程池，处理并发请求
        ExecutorService tomcatPool = Executors.newFixedThreadPool(10);

        System.out.println("[" + LocalTime.now().format(dtf) + "] 流量洪峰到达，10个请求同时涌入...");

        for (int i = 1; i <= 10; i++) {
            final int requestId = i;
            tomcatPool.execute(() -> {
                handleRequest(requestId);
            });
        }

        tomcatPool.shutdown();
        tomcatPool.awaitTermination(1, TimeUnit.MINUTES);
    }

    /**
     * 模拟 Controller 中的方法
     */
    public static void handleRequest(int requestId) {
        // 2. 尝试获取令牌（模拟 Controller 层的 AOP 拦截）
        // 我们设置 500ms 的超时时间：如果 0.5s 内能排到队，就处理；否则直接拒绝
        boolean acquired = limiter.tryAcquire(500, TimeUnit.MILLISECONDS);

        if (acquired) {
            try {
                // 3. 成功获取令牌，执行业务
                System.out.println(String.format("[%s] 接口 %d: ✅ 拿到令牌，开始处理业务...",
                        LocalTime.now().format(dtf), requestId));

                // 模拟业务耗时
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // 如果业务代码中手动调用了 Thread.sleep()、Object.wait() 或 Thread.join() 等阻塞方法时
                // 如果线程收到中断信号：会抛出 InterruptedException 并且清除线程中断标志位(设置为false)
                // 如果 catch 中捕获了该异常但是并没有重新抛出，那么该线程的中断状态就丢失了，上层调用者无法得知线程被中断
                // 此时通过调用 Thread.currentThread().interrupt() 将标志位手动设置为 true，确保代码或上层框架(如线程池)能感知到中断请求并作出正确相应
                Thread.currentThread().interrupt();
            }
        } else {
            // 4. 获取失败，执行降级逻辑（避免线程死等）
            System.err.println(String.format("[%s] 接口 %d: ❌ 被限流！直接返回 HTTP 429 (Too Many Requests)",
                    LocalTime.now().format(dtf), requestId));
        }
    }
}
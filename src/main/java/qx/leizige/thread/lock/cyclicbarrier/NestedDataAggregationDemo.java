package qx.leizige.thread.lock.cyclicbarrier;

/**
 * 多维数据的汇总处理
 * 比如统计一家跨国公司全年的财务报表：
 * <p>
 * 第一阶段：四个线程分别计算四个季度的报表。
 * <p>
 * 第二阶段：等四个季度都算完了，屏障触发，执行汇总逻辑。
 * <p>
 * 第三阶段：汇总完后，屏障重置，四个线程继续并行为下一年做同样的计算。
 */

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NestedDataAggregationDemo {

    // 假设我们要计算年度总销售额
    private static final AtomicInteger annualTotal = new AtomicInteger(0);

    public static void main(String[] args) {
        int quarterCount = 4;

        // 1. 定义屏障：当4个季度都算完时，执行年度汇总动作
        CyclicBarrier barrier = new CyclicBarrier(quarterCount, () -> {
            System.out.println("\n===== [Barrier Action] 所有季度统计完毕，年度总销售额: " + annualTotal.get() + " =====");
        });

        // 2. 启动4个季度主线程
        ExecutorService quarterPool = Executors.newFixedThreadPool(quarterCount);

        for (int i = 1; i <= quarterCount; i++) {
            quarterPool.execute(new QuarterMainTask(i, barrier));
        }

        quarterPool.shutdown();
    }

    /**
     * 季度主任务：负责调度本季度内部的子任务
     */
    static class QuarterMainTask implements Runnable {
        private final int quarter;
        private final CyclicBarrier barrier;

        public QuarterMainTask(int quarter, CyclicBarrier barrier) {
            this.quarter = quarter;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.println(">>> 季度 [" + quarter + "] 启动，检测到海量数据，开始拆分子任务...");

            try {
                // 3. 【内层并发】模拟每个季度内部分成 3 个月份并行处理
                CompletableFuture<Integer> m1 = CompletableFuture.supplyAsync(() -> processMonth(quarter, 1));
                CompletableFuture<Integer> m2 = CompletableFuture.supplyAsync(() -> processMonth(quarter, 2));
                CompletableFuture<Integer> m3 = CompletableFuture.supplyAsync(() -> processMonth(quarter, 3));

                // 等待本季度的月份数据全部跑完
                CompletableFuture.allOf(m1, m2, m3).join();

                // 汇总本季度结果
                int quarterSum = m1.get() + m2.get() + m3.get();
                annualTotal.addAndGet(quarterSum);
                System.out.println("### 季度 [" + quarter + "] 子任务全部完成，季度小计: " + quarterSum);

                // 4. 【外层同步】在本季度逻辑完成后，去屏障处集合
                barrier.await();

            } catch (Exception e) {
                System.err.println("季度 " + quarter + " 计算出错");
                e.printStackTrace();
            }
        }

        // 模拟具体月份的耗时计算
        private int processMonth(int q, int m) {
            try {
                Thread.sleep(1000); // 模拟大数据量查询
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int score = (int) (Math.random() * 100);
            System.out.println("    [子任务] 季度" + q + " 第" + m + "个月完成计算: " + score);
            return score;
        }
    }
}
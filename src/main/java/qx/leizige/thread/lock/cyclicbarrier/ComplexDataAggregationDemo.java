package qx.leizige.thread.lock.cyclicbarrier;

import java.util.*;
import java.util.concurrent.*;

public class ComplexDataAggregationDemo {

    // 1. 使用 ConcurrentHashMap 存储最终结果：Key=季度, Value=月份数据列表
    private static final Map<Integer, List<Integer>> annualReport = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int quarterCount = 4;

        // 2. 屏障动作：此时 Map 已经填充完毕，进行最后的组装或格式化输出
        CyclicBarrier barrier = new CyclicBarrier(quarterCount, () -> {
            System.out.println("\n===== [年度汇总报告生成] =====");
            // 此时可以进行排序、计算总和、转成 JSON 等操作
            annualReport.forEach((quarter, data) -> {
                int sum = data.stream().mapToInt(Integer::intValue).sum();
                System.out.println("季度 " + quarter + " 的详细明细: " + data + " | 小计: " + sum);
            });
        });

        ExecutorService executor = Executors.newFixedThreadPool(quarterCount);

        for (int i = 1; i <= quarterCount; i++) {
            executor.execute(new QuarterTask(i, barrier));
        }

        executor.shutdown();
    }

    static class QuarterTask implements Runnable {
        private final int quarter;
        private final CyclicBarrier barrier;

        public QuarterTask(int quarter, CyclicBarrier barrier) {
            this.quarter = quarter;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                // 3. 内层继续分子线程处理（如每个月一个任务）
                CompletableFuture<Integer> m1 = CompletableFuture.supplyAsync(() -> fetchData(quarter, 1));
                CompletableFuture<Integer> m2 = CompletableFuture.supplyAsync(() -> fetchData(quarter, 2));
                CompletableFuture<Integer> m3 = CompletableFuture.supplyAsync(() -> fetchData(quarter, 3));

                // 等待子任务完成并收集
                List<Integer> quarterResults = Arrays.asList(m1.join(), m2.join(), m3.join());

                // 4. 将本季度结果安全地放入共享 Map
                // 即使多个季度主线程同时 put，ConcurrentHashMap 也能保证安全
                annualReport.put(quarter, quarterResults);

                System.out.println("季度 " + quarter + " 数据已装载。");

                // 到达屏障
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int fetchData(int q, int m) {
            // 模拟查询耗时
            return (int) (Math.random() * 1000);
        }
    }
}
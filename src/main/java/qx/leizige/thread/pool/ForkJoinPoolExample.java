package qx.leizige.thread.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link java.util.concurrent.ForkJoinPool}
 * ForkJoinPool 适用于那些可以被拆分成独立子任务并行执行的计算密集型任务
 * <p>
 * 1. 继承 RecursiveTask 或 RecursiveAction 类，实现 compute() 方法，该方法用于执行任务。
 * <p>
 * 2. 创建 ForkJoinPool 对象，指定并行执行任务的最大线程数。
 * <p>
 * 3. 创建 Fork/Join 任务对象，并将其提交到 ForkJoinPool 中。
 * <p>
 * 4. 调用 Fork/Join 任务的 join() 方法来返回任务执行的结果。
 * </p>
 */
public class ForkJoinPoolExample {
    public static void main(String[] args) {

        long[] array = new long[1000000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        long startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        Sum sum = new Sum(array, 0, array.length);
        AtomicLong result = pool.invoke(sum);
        System.out.println("The sum is: " + result.get());
        long endTime = System.currentTimeMillis();
        System.out.println("Time cost: " + (endTime - startTime) + "ms");

    }


    /**
     * 递归任务
     */
    static class Sum extends RecursiveTask<AtomicLong> {

        private static final long THRESHOLD = 10000;
        private final long[] array;
        private final int start;
        private final int end;

        Sum(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        /**
         * 如果任务的大小小于阈值（10000），则直接计算该任务的和；否则，将任务拆分成两个子任务，并分别进行计算，最后将子任务的结果汇总。
         */
        @Override
        protected AtomicLong compute() {
            AtomicLong sum = new AtomicLong(0);
            if (end - start < THRESHOLD) {
                for (int i = start; i < end; i++) {
                    sum.addAndGet(array[i]);
                }
                return sum;
            }
            int middle = (start + end) / 2;
            Sum left = new Sum(array, start, middle);
            Sum right = new Sum(array, middle, end);
            left.fork();
            right.fork();
            return new AtomicLong(left.join().addAndGet(right.join().get()));
        }
    }
}

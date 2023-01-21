package qx.leizige.thread.lock.cyclicbarrier;

import java.util.concurrent.*;

/**
 * CyclicBarrier是一个同步辅助类，它允许一组线程相互等待直到所有线程都到达一个公共的屏障点
 */
public class CyclicBarrierExample {

    private final static int THREAD_NUM = 3;
    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_NUM, () -> {
        System.out.println("全部线程已到达屏障");
    });

    /**
     * 假设有一家公司要全体员工进行团建活动，活动内容为翻越三个障碍物，每一个人翻越障碍物所用的时间是不一样的。
     * 但是公司要求所有人在翻越当前障碍物之后再开始翻越下一个障碍物，也就是所有人翻越第一个障碍物之后，才开始翻越第二个，以此类推。
     * 类比地，每一个员工都是一个“其他线程”。当所有人都翻越的所有的障碍物之后，程序才结束。而主线程可能早就结束了，这里我们不用管主线程。
     */
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int j = 0; j < THREAD_NUM; j++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "到达屏障！");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + "到达屏障后执行操作！");
            });
        }
        executorService.shutdown();
    }
}

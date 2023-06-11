package qx.leizige.thread.pool;

import java.util.concurrent.*;

/**
 * {@link java.util.concurrent.CompletionService}
 * <p>
 * CompletionService 是一个异步计算框架，它在 Executor 中提交一组 Callable 任务，并在这些 Callable 任务计算完成后按照完成时间顺序获取这些任务的计算结果
 * CompletionService 的内部维护了一个阻塞队列，当任务执行结束就把任务的执行结果加入到阻塞队列中
 * </p>
 */
public class CompletionServiceExample {

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        //异步执行向电商S1、S2、S3询价
        cs.submit(CompletionServiceExample::getPriceByS1);
        cs.submit(CompletionServiceExample::getPriceByS2);
        cs.submit(CompletionServiceExample::getPriceByS3);

        //拿到询价结果异步处理保存到数据库
        for (int i = 0; i < 3; i++) {
            //take:如果阻塞队列为空,调用take方法的线程会阻塞
            //poll:如果阻塞队列为空,调用poll方法会返回null值
            Integer result = cs.take().get();
            executor.execute(() -> save(result));
        }

        executor.shutdown();
    }


    static Integer getPriceByS1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        return 500;
    }

    static Integer getPriceByS2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return 100;
    }

    static Integer getPriceByS3() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return 300;
    }

    static void save(Integer price) {
        System.out.println(Thread.currentThread().getName() + "   price: " + price + " save...");
    }
}

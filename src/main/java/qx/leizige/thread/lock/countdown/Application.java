package qx.leizige.thread.lock.countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author leizige
 * @link <a href="https://www.jianshu.com/p/1716ce690637">...</a>
 * 应用启动类，使用线程池执行每个服务的任务 负责初始化闭锁，然后等待，直到所有服务都被检测完 
 */
public class Application {


    /**
     * <p>
     *     CountDownLatch也叫闭锁，在JDK1.5被引入，允许一个或多个线程等待其他线程完成操作后再执行 
     * </p>
     * <p>
     *     CountDownLatch内部会维护一个初始值为线程数量的计数器，主线程执行await方法，如果计数器大于0，则阻塞等待 
     *     当一个线程完成任务后，计数器值减1 当计数器为0时，表示所有的线程已经完成任务，等待的主线程被唤醒继续执行 
     * </p>
     * <p>
     *     await(), 此函数将会使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断
     *     countDown(), 此函数将递减锁存器的计数，如果计数到达零，则释放所有等待的线程
     * </p>
     */
    private final CountDownLatch countDownLatch = new CountDownLatch(1);


    public void startUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        List<Service> services = new ArrayList<>();
        services.add(new DatabaseCheckerService(latch));
        services.add(new HealthCheckService(latch));
        Executor executor = Executors.newFixedThreadPool(services.size());
        for (Service service : services) {
            executor.execute(service);
        }
        latch.await();
        System.out.println("all service is start up");
    }

    /**
     * 应用程序的主线程希望在负责启动框架服务的线程已经完成之后再执行 
     */
    public static void main(String[] args) throws Exception {
        new Application().startUp();
    }
}

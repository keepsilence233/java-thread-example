package qx.leizige.thread.lock.countdown;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 服务具体实现类，类似的还有DatabaseCheckerService，这里我们使用TimeUnit.SECONDS.sleep模拟长时间的操作。
 */
class HealthCheckService extends Service {

    public HealthCheckService(CountDownLatch latch) {
        super(latch);
    }

    @Override
    public void execute() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package qx.leizige.thread.pool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * {@link java.util.concurrent.CompletableFuture} 使用示例
 */
public class CompletableFutureExample {

    public static void main(String[] args) {

        //任务1：洗水壶 --> 烧开水
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1:洗水壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T1:烧开水...");
            sleep(15, TimeUnit.SECONDS);
        });

        //任务2：洗茶壶 --> 洗茶杯 --> 拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("T1:洗茶壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T1:洗茶杯...");
            sleep(2, TimeUnit.SECONDS);

            System.out.println("T1:拿茶叶...");
            sleep(1, TimeUnit.SECONDS);

            return "龙井";
        });

        CompletableFuture<String> f3 = f2.thenCombine(f2, (a, tf) -> {
            System.out.println("T1: 拿到茶叶:" + tf);
            System.out.println("T1: 泡茶...");
            return " 上茶:" + tf;
        });
        System.out.println(f3.join());
    }


    static void sleep(int time, TimeUnit unit) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

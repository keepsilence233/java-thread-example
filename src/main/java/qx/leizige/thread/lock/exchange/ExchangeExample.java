package qx.leizige.thread.lock.exchange;

import java.time.LocalDateTime;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *     Exchange 交换器是用来实现两个线程间的数据交换的，Exchanger 可以交互任意数据类型的数据，只要在创建的时候定义泛型类型即可。
 * </p>
 * <p>
 *     它的核心方法为 exchange，当线程执行到此方法之后，会休眠等待另一个线程也进入交换点，如果另一个线程也进入了交换点（也执行到了 exchange 方法），此时二者会交换数据，并执行后续的流程。
 * </p>
 */
public class ExchangeExample {

    private final static Exchanger<String> exchange = new Exchanger<>();

    public static void main(String[] args) {

        /**
         * 线程A:准备筹钱中... Time + currentTime
         * 线程A准备好了1000块... Time + currentTime
         * 线程B:准备物品中... Time + currentTime
         * 线程B准备好了1吨煤炭... Time + currentTime
         * 线程A交易完成,得到：1吨煤炭
         * 线程B交易完成,得到：1000块
         */

        Thread a = new Thread(() -> {
            System.out.println("线程A:准备筹钱中... Time " + LocalDateTime.now());

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String money = "1000块";
            System.out.println("线程A准备好了1000块... Time" + LocalDateTime.now());

            try {
                String result = exchange.exchange(money);
                System.out.println("线程A交易完成,得到：" + result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "A");


        Thread b = new Thread(() -> {

            System.out.println("线程B:准备物品中... Time " + LocalDateTime.now());

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String money = "1吨煤炭";
            System.out.println("线程B准备好了1吨煤炭... Time" + LocalDateTime.now());

            try {
                String result = exchange.exchange(money);
                System.out.println("线程B交易完成,得到：" + result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "B");
        a.start();
        b.start();
    }
}

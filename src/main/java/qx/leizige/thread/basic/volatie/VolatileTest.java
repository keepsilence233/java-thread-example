package qx.leizige.thread.basic.volatie;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * created 2021/08/09
 * volatile解决共享变量value的内存可见性问题
 */
public class VolatileTest {

    /**
     * 作用
     * <p>
     * 防止JVM对 Long/Double 等64位的非原子性协议进行的 误操作（读取半个数据）;
     * 可以使某一个变量对所有的线程立即可见（某一个线程如果修改了工作内存中的变量副本，那么加上Volatile关键字之后，该变量就会立即同步到其他线程的工作内存当中）。
     * 禁止指令 "重排序" 优化。
     * </p>
     */
    private static volatile Integer num = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(" num old value = " + num);

        new Thread(() -> {
            System.out.println("进入[" + Thread.currentThread().getName() + "]线程");

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addNum();

            System.out.println("[" + Thread.currentThread().getName() + "]线程 update num,value = " + num);
        }).start();


        /**
         * 如果num不使用volatile修饰,子线程update num = 100之后不会刷新主内存,所以main线程获取到的num值为0
         */
        while (num.equals(0)) {
            //empty
        }

        System.out.println(Thread.currentThread().getName() + "运行结束! num value = " + num);
    }


    private static void addNum() {
        num = 1000;
    }

}

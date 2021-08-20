package qx.leizige.thread.basic.volatie;

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

    }

}

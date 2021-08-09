package qx.leizige.thread.basic.threadlocal;

/**
 * @author leizige
 * created 2021/08/09
 */
public class InheritableThreadLocalTest {

    /**
     * threadLocal 不支持继承性,子线程获取不到父线程设置的值
     * InheritableThreadLocal继承自ThreadLocal，其提供了一个特性，
     * 就是让子线程可以访问在父线程中设置的本地变量。
     */
//    private final static ThreadLocal<String> localVariable = new ThreadLocal<>();

    private final static ThreadLocal<String> localVariable = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        localVariable.set("hello world");

        new Thread(() -> {

            System.out.println(" child thread localVariable.get() = " + localVariable.get());
        }).start();

        System.out.println(" main thread localVariable.get() = " + localVariable.get());
    }
}

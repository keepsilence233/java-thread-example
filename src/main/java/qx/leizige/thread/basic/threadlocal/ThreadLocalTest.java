package qx.leizige.thread.basic.threadlocal;

/**
 * @author leizige
 * created 2021/08/09
 */
public class ThreadLocalTest {

    /**
     * ThreadLocal是JDK包提供的，它提供了线程本地变量，也就是如果你创建了一个ThreadLocal变量，
     * 那么访问这个变量的每个线程都会有这个变量的一个本地副本。当多个线程操作这个变量时，
     * 实际操作的是自己本地内存里面的变量，从而避免了线程安全问题。
     * 创建一个ThreadLocal变量后，每个线程都会复制一个变量到自己的本地内存
     */
    private final static ThreadLocal<String> localVariable = new ThreadLocal<>();

    static void print() {
        System.out.println("str : " + localVariable.get());
        localVariable.remove();
    }

    public static void main(String[] args) {

        new Thread(() -> {
            localVariable.set(Thread.currentThread().getName());
            print();
            System.out.println(Thread.currentThread().getName() + " remove after : " + localVariable.get());
        }, "A").start();

        new Thread(() -> {
            localVariable.set(Thread.currentThread().getName());
            print();
            System.out.println(Thread.currentThread().getName() + " remove after : " + localVariable.get());
        }, "B").start();

        new Thread(() -> {
            localVariable.set(Thread.currentThread().getName());
            print();
            System.out.println(Thread.currentThread().getName() + " remove after : " + localVariable.get());
        }, "C").start();

    }
}

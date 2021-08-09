package qx.leizige.thread.basic.daemon;

@SuppressWarnings("all")
public class DaemonThreadTest {

    /**
     * Java中的线程分为两类，分别为daemon线程（守护线程）和user线程（用户线程）
     * <p>
     *     在JVM启动时会调用main函数，main函数所在的线程就是一个用户线程，其实在JVM内部同时还启动了好多守护线程，
     *     比如垃圾回收线程。那么守护线程和用户线程有什么区别呢？区别之一是当最后一个非守护线程结束时，
     *     JVM会正常退出，而不管当前是否有守护线程，也就是说守护线程是否结束并不影响JVM的退出。
     *     言外之意，只要有一个用户线程还没结束，正常情况下JVM就不会退出。
     * </p>
     */
    public static void main(String[] args) {

        /**
         * 用户线程(main线程)结束之后,守护线程(thread)也会结束
         */
        Thread thread = new Thread(() -> {
            for (; ; ) {
                //......
            }
        });
        //设置为守护线程,只需要设置线程的daemon参数为true即可
        thread.setDaemon(true);
        thread.start();

        System.out.println("main thread is over ......");
    }
}

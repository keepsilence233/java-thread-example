package qx.leizige.thread.basic.volatie;

/**
 * 在这里使用synchronized和使用volatile是等价的，都解决了共享变量value的内存可见性问题，
 * 但是前者是独占锁，同时只能有一个线程调用get（）方法，其他调用线程会被阻塞，同时会存在线程上下文切换和线程重新调度的开销，
 * 这也是使用锁方式不好的地方。而后者是非阻塞算法，不会造成线程上下文切换的开销。
 */
public class ThreadNotSafeInteger {

    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void increment() {
        ++value;
    }

    /* volatile
    private volatile int value;

    public int getValue() {
        return value;
    }

    public void increment(int value) {
        this.value = value;
    }
     */
}

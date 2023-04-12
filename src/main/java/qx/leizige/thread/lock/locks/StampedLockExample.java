package qx.leizige.thread.lock.locks;


import java.util.concurrent.locks.StampedLock;

/**
 * stampedLock
 * {@link <a href="https://pdai.tech/md/java/java8/java8-stampedlock.html">...</a>}
 */
public class StampedLockExample {

    public static void main(String[] args) {


        StampedLock sl = new StampedLock();


        long stamp = sl.readLock();

        try {
            //
        } finally {
            sl.unlock(stamp);
        }



        sl.writeLock();

    }
}

package qx.leizige.thread.lock.locks;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author leizige
 * create by 2023/04/13 01:07
 * <p>
 * 读写多允许多个线程同时读共享变量，而互斥锁是不允许的。这是在读多写少场景下性能优化互斥锁的关键条件
 * </p>
 */
public class ReentrantReadWriteLockExample {


    /**
     * 以下是JDK1.8中ReentrantReadWriteLock自带的示例,模拟了缓存读写场景
     */
    static class CacheData {

        Object data;
        volatile boolean cacheValid;    //判断缓存是否有效boolean值
        private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        private final ReentrantReadWriteLock.ReadLock r = rwl.readLock();
        private final ReentrantReadWriteLock.WriteLock w = rwl.writeLock();

        void processCacheData() {
            r.lock();       //1、读锁
            if (!cacheValid) {  //2、读操作,如果缓存无效
                r.unlock(); //3、释放读锁,因为不允许锁的升级
                w.lock();   //4、获取写锁（获取写锁之前必须先释放读锁）（如果没有释放读锁就获取写锁,会导致锁永久等待。最终导致相关线程都被阻塞,永远也没有机会被唤醒）

                try {
                    //重新缓存有效性,如果无效
                    if (!cacheValid) {
                        data = new Object();    //5、设置缓存为最新值
                        cacheValid = Boolean.TRUE;  //6、设置缓存有效
                    }
                    r.lock();   //7、释放写锁前将锁降级为读锁（已存在写锁的情况下获取读锁,写锁降级为读锁）
                } finally {
                    w.unlock(); //8、释放写锁,保持读锁
                }
            }

            try {
                //操作缓存数据
            } finally {
                r.unlock(); //9、释放读锁
            }
        }
    }

    /**
     * 以下是JDK1.8中ReentrantReadWriteLock自带的示例
     * ReentrantReadWriteLock 可用于在某些类型的集合的某些使用中提高并发性。这通常只有在预期集合很大、由比写入线程更多的读取线程访问并且需要的开销超过同步开销的操作时才值得。
     * 例如，这里有一个使用 TreeMap 的类，预计会很大并且会被并发访问。
     */
    static class RWDictionary {
        private final Map<String, Object> m = new TreeMap<>();
        private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        private final ReentrantReadWriteLock.ReadLock r = rwl.readLock();
        private final ReentrantReadWriteLock.WriteLock w = rwl.writeLock();

        public Object get(String key) {
            r.lock();
            try {
                return m.get(key);
            } finally {
                r.unlock();
            }
        }

        public String[] allKeys() {
            r.lock();
            try {
                return m.keySet().toArray(new String[0]);
            } finally {
                r.unlock();
            }
        }

        public Object put(String key, Object value) {
            w.lock();
            try {
                return m.put(key, value);
            } finally {
                w.unlock();
            }
        }

        public void clear() {
            w.lock();
            try {
                m.clear();
            } finally {
                w.unlock();
            }
        }
    }


}

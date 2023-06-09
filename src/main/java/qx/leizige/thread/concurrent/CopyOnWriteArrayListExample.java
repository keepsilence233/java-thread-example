package qx.leizige.thread.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWrite，顾名思义就是写的时候会将共享变量新复制一份出来，这样做的好处是读操作完全无锁
 * CopyOnWriteArrayList 仅适用于写操作非常少的场景，而且能够容忍读写的短暂不一致
 * CopyOnWriteArrayList 迭代器是只读的，不支持增删改。因为迭代器遍历的仅仅是一个快照，而对快照进行增删改是没有意义的
 */
public class CopyOnWriteArrayListExample {
    private static final CopyOnWriteArrayList<Object> c = new CopyOnWriteArrayList<>();
}

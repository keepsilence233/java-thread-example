package qx.leizige.thread.lock.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 带版本号的AtomicInteger
 * 给数据带上时间戳，不仅比较 value 值是否相等，还要比较 Stamp (邮戳/版本号)是否一致
 */
public class AtomicStampedReferenceExample {

    public static void main(String[] args) throws InterruptedException {
        // 初始化：初始值为 "A"，初始版本号为 1
        AtomicStampedReference<String> asr = new AtomicStampedReference<>("A", 1);

        // 线程 1：模拟一个比较慢的更新操作
        Thread t1 = new Thread(() -> {
            String reference = asr.getReference();
            int stamp = asr.getStamp(); // 获取当前版本号
            System.out.println("线程1 - 初始状态: 值=" + reference + ", 版本号=" + stamp);

            try {
                // 睡眠 2 秒，等待线程 2 完成 ABA 操作
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 尝试将 A 修改为 C
            // 只有当 值是 "A" 且 版本号是 1 时，才会修改成功
            boolean success = asr.compareAndSet("A", "C", stamp, stamp + 1);
            System.out.println("线程1 - CAS修改(A->C)结果: " + success +
                    ", 当前值: " + asr.getReference() +
                    ", 当前版本号: " + asr.getStamp());
        });

        // 线程 2：快速完成 ABA 操作
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500); // 确保线程 1 已经拿到了初始版本号
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // A -> B
            asr.compareAndSet("A", "B", asr.getStamp(), asr.getStamp() + 1);
            System.out.println("线程2 - A修改为B, 当前版本号: " + asr.getStamp());

            // B -> A
            asr.compareAndSet("B", "A", asr.getStamp(), asr.getStamp() + 1);
            System.out.println("线程2 - B修改回A, 当前版本号: " + asr.getStamp());
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}

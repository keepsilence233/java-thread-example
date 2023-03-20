package qx.leizige.thread.pool;

/**
 * 测试线程池中addWorker的retry变量使用
 * {@link java.util.concurrent.ThreadPoolExecutor#addWorker(Runnable, boolean)}
 */
public class TestThreadPoolAddWorkerRetry {
    public static void main(String[] args) {
//        retry:
//        for (; ; ) {
//
//            if (1 == 1) {
//                continue retry;
//            }
//            break retry;
//        }

        int count = 0;
        retry:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                count++;
                if (count == 4) {
                    continue retry;
                }
                System.out.print(count + " ");
            }
        }
        // 1 2 3 5 6 7 8 9 10 11 12 13 14
    }
}

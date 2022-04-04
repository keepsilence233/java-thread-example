package qx.leizige.thread.basic.volatie;

import java.util.concurrent.TimeUnit;

/**
 * Java 内存模型确保声明为volatile的字段对所有线程始终可见。
 */
public class VolatileRefExample {


    private static volatile Data data = new Data(-1, -1);


    private static class Data {

        private int a;
        private int b;

        public Data(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }


    /**
     * 我们将创建读取器和写入器线程。编写器线程始终为同一 Data 实例上的 Data.a 和 Data.b 分配相同的值。
     * 由于数据引用是“易变的”，我们预计 a 和 b 将始终具有相同的值。
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            int a = i;
            int b = i;

            //我们将整个过程重复了 3 次，并且始终发现 a 和 b 对读者线程而言并非始终可见，因为 a!=b 有时是正确的。
            //问题是变量 a 和 b 的单独读取缺少与写入线程的发生前关系。同步修复参考：VolatileRefExample2

            //writer
            Thread writerThread = new Thread(() -> {
                data.setA(a);
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.setB(b);
            });

            //reader
            Thread readerThread = new Thread(() -> {
                int x = data.getA();
                int y = data.getB();
                if (x != y) {
                    System.out.printf("a = %s, b = %s%n", x, y);
                }
            });

            writerThread.start();
            readerThread.start();
            writerThread.join();
            readerThread.join();

        }
        System.out.println("finished");
    }
}

package qx.leizige.thread.basic.wait;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author leizige
 * created 2021/08/09
 */
public class WaitTest {
    /**
     * 调用共享变量的wait()方法需要获取共享变量监视器锁,否则会抛出异常
     *  <p>
     *      1、指定synchronized代码块,使用共享变量作为参数
     *      synchronized（共享变量）{
     *            //doSomething
     *        }
     *  </p>
     *  <p>
     *      2、调用该共享变量的方法，并且该方法使用了synchronized修饰。
     *      synchronized void add（int a,int b）{
     *           //doSomething
     *      }
     *  </p>
     *  一个线程调用共享对象的notify()方法后，会唤醒一个在该共享变量上调用wait系列方法后被挂起的线程。
     *  一个共享变量上可能会有多个线程在等待，具体唤醒哪个等待的线程是随机的。
     */
    static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) throws Exception {

        new Thread(() -> {
            //获取queue共享资源对象监视器锁
            synchronized (queue) {
                for (int i = 1; i <= 20; i++) {
                    if (queue.size() == 5) {
                        try {
                            //队列满了等待
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //添加元素
                        queue.add("element" + i);
                        queue.notifyAll();
                    }
                }
            }
        }, "A").start();


        new Thread(() -> {
            synchronized (queue) {
                while (true) {
                    if (queue.size() == 0) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String take = null;
                        try {
                            take = queue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(take);
                        queue.notifyAll();
                    }
                }
            }

        }, "B").start();

        Thread.sleep(2000);
        System.exit(0);

    }
}

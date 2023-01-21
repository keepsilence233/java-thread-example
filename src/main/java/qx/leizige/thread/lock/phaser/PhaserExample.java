package qx.leizige.thread.lock.phaser;


import java.util.concurrent.Phaser;

/**
 * Phaser 支持线程动态地向它注册
 */
public class PhaserExample {

    /**
     * 模拟了100米赛跑 ,10名选手 ,只等裁判一声令下。当所有人都到达终点时 ,比赛结束。
     */
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(1);
        for (int i = 0; i < 10; i++) {
            phaser.register();  //注册一个 party
            new Thread(new Player(phaser), "player" + i).start();
        }
        System.out.println("Gam start");
        //注销当前线程,比赛开始
        phaser.arriveAndDeregister();
        //是否非终止态一直等待
        while (!phaser.isTerminated()) {
        }
        System.out.println("Gam Over");
    }


    static class Player implements Runnable {

        private final Phaser phaser;

        Player(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                // 等待创建好所有线程再开始,每个线程到这里进行阻塞,等待所有线程到达栅栏
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(Thread.currentThread().getName() + " ready");

                phaser.arriveAndAwaitAdvance();
                // doWork()

                //等待所有选手准备好到达,到达后,该线程从phaser中注销,不在进行下面的阶段
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(Thread.currentThread().getName() + " arrived");
                phaser.arriveAndDeregister(); //到达并注销
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }


}

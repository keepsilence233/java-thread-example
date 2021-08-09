package qx.leizige.thread.basic.volatie;

import java.util.concurrent.TimeUnit;

/**
 * @author leizige
 * created 2021/08/09
 */
public class VolatileTest {


    public static void main(String[] args) throws InterruptedException {

        ThreadNotSafeInteger t = new ThreadNotSafeInteger();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    t.increment();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(3);

        System.err.println(t.getValue());

    }

}

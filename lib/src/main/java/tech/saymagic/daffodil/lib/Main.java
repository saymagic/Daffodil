package tech.saymagic.daffodil.lib;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caoyanming on 2017/6/10.
 */

public class Main {

    private static AtomicInteger mInteger = new AtomicInteger();


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    test(mInteger.incrementAndGet(), "sss");
                }
            });
            thread.start();
        }
    }

    public static void test(int a, String s) {
//        MethodRemember.onMethodEnter(a, s);
//        MethodRemember.onMethodExit(null);
    }

}

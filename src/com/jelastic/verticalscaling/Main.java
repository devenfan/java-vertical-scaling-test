package com.jelastic.verticalscaling;

import java.io.IOException;

/**
 *
 * @author ruslan
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        int sleep = 10;
        int mode = 1;
        int recycleSleep = 1000;
        if (args.length > 0) {
            sleep = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            mode = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            recycleSleep = Integer.parseInt(args[2]);
        }

        Monitor monitor = new Monitor();
        LoadTest loadTest = new LoadTest(mode, sleep, recycleSleep);

        new Thread(monitor).start();
        new Thread(loadTest).start();

        int max = 10;
        int i = 0;
        while (i++ < max) {
            Thread.sleep(6 * 1000);
        }
        monitor.kill();
        loadTest.kill();
    }

}

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
        BaseLoadTest loadTest = null;
        switch (mode) {
            case 1:
                loadTest = new BigObjectLoadTest(false, sleep, recycleSleep);
                break;
            case 2:
                loadTest = new SmallObjectLoadTest(false, sleep, recycleSleep);
                break;
            default:
                loadTest = new SmallObjectLoadTest((mode % 2 == 0), sleep, recycleSleep);
                break;
        }

        new Thread(monitor).start();
        new Thread(loadTest).start();

        int max = 120;
        int i = 0;
        while (i++ < max) {
            Thread.sleep(1000);
        }
        monitor.kill();
        loadTest.kill();
    }

}

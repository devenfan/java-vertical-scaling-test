package com.jelastic.verticalscaling;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ruslan
 */
public class LoadTest implements Runnable {

    private static final int SIZE_1K = 1024;
    private static final int SIZE_1M = 1024 * 1024;

    private static final int OBJ_CONTAINER_SIZE = 100;
    private static volatile Object objectContainer[] = new Object[OBJ_CONTAINER_SIZE];

    private boolean isRunning = false;
    private boolean isKilled = false;

    private int mode = 1;
    private int sleep = 0;
    private int recycleSleep = 1000;

    private int numCycles = 10;
    private int loadTimes = 1024 * (256 - 32);

    public LoadTest(int mode, int sleep) {
        this.mode = mode;
        this.sleep = sleep;
    }

    public LoadTest(int mode, int sleep, int recycleSleep) {
        this.mode = mode;
        this.sleep = sleep;
        this.recycleSleep = recycleSleep;
    }

    public void kill() {
        this.isKilled = true;
    }

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        isRunning = true;
        System.out.println("[LoadTest] begin... ");

        while(!isKilled) {
            System.out.println("[LoadTest] Let's do it, in mode[" + mode + "], sleep interval is " + sleep);
            try {
                switch (mode) {
                    case 1:
                        simpleWithoutRecycle(loadTimes);
                        break;
                    case 2:
                        sampleWithRecycle(loadTimes);
                        break;
                    default:
                        System.out.println("Unknown load mode: " + mode);
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                try {
                    //没内存了，休息一会儿
                    System.out.println("[LoadTest] no enough memory, take a rest...");
                    SignalTool.waitSignal(SignalTool.SIGNAL_ID_DOWN, recycleSleep);
                    continue;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isRunning = false;
        System.out.println("[LoadTest] done, total time = " + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * Sample n times, each time consume 1KB
     * @param n
     * @throws InterruptedException
     * @throws OutOfMemoryError
     */
    private void simpleWithoutRecycle(int n) throws InterruptedException, OutOfMemoryError {
        List list = new LinkedList();
        for (int i = 0; i < n; i++) {
            list.add(new byte[SIZE_1K]);
            if (i % SIZE_1K == 0) {
                //Sleep every 1M
                Thread.sleep(sleep);
                if(list.size() > (128 * SIZE_1K)) {
                    list.clear();
                    Thread.sleep(recycleSleep);
                }
            }
        }
        System.out.println("[LoadTest] simpleWithoutRecycle is finished. Total " + (n * 1.0 / SIZE_1K) + "MB...");
    }

    /**
     * Sample n times, each time consume 1KB
     * @param n
     * @throws InterruptedException
     * @throws OutOfMemoryError
     */
    private void sampleWithRecycle(int n) throws InterruptedException, OutOfMemoryError {
        for (int k = 0; k < numCycles; k++) {
            List list = new LinkedList();
            for (int i = 0; i < n; i++) {
                list.add(new byte[SIZE_1K]);
                for (int r = 0; r < OBJ_CONTAINER_SIZE; r++) {
                    objectContainer[r] = new byte[SIZE_1K];
                }
                if (i % SIZE_1K == 0) {
                    //Sleep every 1M
                    Thread.sleep(sleep);
                }
            }
            System.out.println("[LoadTest] sampleWithRecycle is finished. Total " + (n * 1.0 / SIZE_1K) + "MB...");
            Thread.sleep(recycleSleep);
            list.clear();
            System.out.println("[LoadTest] sampleWithRecycle Calling list.clear(). Sleeping...");
            Thread.sleep(recycleSleep);
            System.gc();
            System.out.println("[LoadTest] sampleWithRecycle Calling System.gc(). Sleeping...");
            Thread.sleep(recycleSleep);
        }
    }

}

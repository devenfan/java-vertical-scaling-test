package com.jelastic.verticalscaling;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ruslan
 */
public class BigObjectLoadTest extends BaseLoadTest {


    private static final int OBJ_CONTAINER_SIZE = 100;
    private static volatile Object objectContainer[] = new Object[OBJ_CONTAINER_SIZE];

    private int numCycles = 10;

    private static final int LOAD_TIMES = (int)(512 / 1.2);

    public BigObjectLoadTest(boolean explictRecycle, int sleep) {
        this.name = getClass().getSimpleName();
        this.explictRecycle = explictRecycle;
        this.sleep = sleep;
        this.loadTimes = LOAD_TIMES;
    }

    public BigObjectLoadTest(boolean explictRecycle, int sleep, int recycleSleep) {
        this.name = getClass().getSimpleName();
        this.explictRecycle = explictRecycle;
        this.sleep = sleep;
        this.recycleSleep = recycleSleep;
        this.loadTimes = LOAD_TIMES;
    }

    /**
     * Sample n times, each time consume 1KB
     * @param n
     * @throws InterruptedException
     * @throws OutOfMemoryError
     */
    @Override
    protected void simpleWithoutRecycle(int n) throws InterruptedException, OutOfMemoryError {
        List list = new LinkedList();
        for (int i = 0; i < n; i++) {
            list.add(new byte[SIZE_1M + SIZE_1K]);
            //Sleep every 1.1M
            sleepAwhile(sleep);
        }
        System.out.println("[LoadTest] simpleWithoutRecycle is finished. Total " + n + "MB...");
    }

    /**
     * Sample n times, each time consume 1KB
     * @param n
     * @throws InterruptedException
     * @throws OutOfMemoryError
     */
    @Override
    protected void sampleWithRecycle(int n) throws InterruptedException, OutOfMemoryError {
        for (int k = 0; k < numCycles; k++) {
            List list = new LinkedList();
            for (int i = 0; i < n; i++) {
                byte[] arr = new byte[SIZE_1M + SIZE_1K];
                list.add(arr);
                for (int r = 0; r < OBJ_CONTAINER_SIZE; r++) {
                    objectContainer[r] = arr;
                }
                //Sleep every 1.1M
                sleepAwhile(sleep);
            }
            System.out.println("[LoadTest] sampleWithRecycle is finished. Total " + (n) + "MB...");
            sleepAwhile(recycleSleep);
            list.clear();
            System.out.println("[LoadTest] sampleWithRecycle Calling list.clear(). Sleeping...");
            sleepAwhile(recycleSleep);
            System.gc();
            System.out.println("[LoadTest] sampleWithRecycle Calling System.gc(). Sleeping...");
            sleepAwhile(recycleSleep);
        }
    }

}

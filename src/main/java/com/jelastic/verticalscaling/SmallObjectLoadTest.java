package com.jelastic.verticalscaling;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ruslan
 */
public class SmallObjectLoadTest extends BaseLoadTest {


    private static final int OBJ_CONTAINER_SIZE = 100;
    private static volatile Object objectContainer[] = new Object[OBJ_CONTAINER_SIZE];

    private int numCycles = 10;

    public SmallObjectLoadTest(boolean explictRecycle, int sleep, int recycleSleep, int maxMemoryByMB, double loadFactor) {
        this.name = getClass().getSimpleName();
        this.explictRecycle = explictRecycle;
        this.sleep = sleep;
        this.recycleSleep = recycleSleep;
        this.loadTimes = (int)(maxMemoryByMB * (SIZE_1M / SIZE_1K) * loadFactor);
        this.loadFactor = loadFactor;
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
            list.add(new byte[SIZE_1K]);
            if(i % (SIZE_1K * 1.1) == 0) {
                //Sleep every 1.1M
                sleepAwhile(sleep);
            }
        }
//        println("simpleWithoutRecycle is finished. Total " + (n / SIZE_1K) + "MB...");
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
                byte[] arr = new byte[SIZE_1K];
                list.add(arr);
                for (int r = 0; r < OBJ_CONTAINER_SIZE; r++) {
                    objectContainer[r] = arr;
                }
                if(i % (SIZE_1K * 1.1) == 0) {
                    //Sleep every 1.1M
                    sleepAwhile(sleep);
                }
            }
//            println("sampleWithRecycle is finished. Total " + (n / SIZE_1K) + "MB...");
            sleepAwhile(recycleSleep);
            list.clear();
//            println("sampleWithRecycle Calling list.clear(). Sleeping...");
            sleepAwhile(recycleSleep);
            System.gc();
//            println("sampleWithRecycle Calling System.gc(). Sleeping...");
            sleepAwhile(recycleSleep);
        }
    }

}

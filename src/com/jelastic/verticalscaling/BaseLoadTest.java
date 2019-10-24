package com.jelastic.verticalscaling;

import java.util.Date;

/**
 * BaseLoadTest
 *
 * @author Deven
 * @version : BaseLoadTest, v 0.1 2019-10-24 17:55 Deven Exp$
 */
public abstract class BaseLoadTest implements Runnable {

    protected static final int SIZE_1K      = 1024;
    protected static final int SIZE_1M      = 1024 * 1024;

    private boolean            isRunning    = false;
    private boolean            isKilled     = false;

    protected String           name         = "LoadTest";
    protected boolean          explictRecycle = false;
    protected int              sleep          = 0;
    protected int              recycleSleep   = 1000;
    protected int              loadTimes      = 512;

    public BaseLoadTest() {
    }

    public BaseLoadTest(boolean explictRecycle, int sleep, int recycleSleep, int loadTimes) {
        this.explictRecycle = explictRecycle;
        this.sleep = sleep;
        this.recycleSleep = recycleSleep;
        this.loadTimes = loadTimes;
    }

    public void kill() {
        this.isKilled = true;
    }

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        isRunning = true;
        println("begin, current time is " + new Date() + "... ");

        while (!isKilled) {
            println("Let's do it, explictRecycle is " + explictRecycle + ", sleep interval is " + sleep + ", recycle sleep is " + recycleSleep);
            try {
                if(!explictRecycle) {
                    simpleWithoutRecycle(loadTimes);
                } else {
                    sampleWithRecycle(loadTimes);
                }
                //take a rest
                sleepAwhile(recycleSleep);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                try {
                    //没内存了，休息一会儿
                    println("no enough memory, take a rest...");
                    SignalTool.waitSignal(SignalTool.SIGNAL_ID_DOWN, recycleSleep > 0 ? recycleSleep : 1000);
                    continue;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        isRunning = false;
        println("done, total time = " + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * Sample n times, each time consume 1KB
     *
     * @param n
     *
     * @throws InterruptedException
     * @throws OutOfMemoryError
     */
    protected abstract void simpleWithoutRecycle(int n) throws InterruptedException, OutOfMemoryError;

    /**
     * Sample n times, each time consume 1KB
     *
     * @param n
     *
     * @throws InterruptedException
     * @throws OutOfMemoryError
     */
    protected abstract void sampleWithRecycle(int n) throws InterruptedException, OutOfMemoryError;

    /**
     * sleep a while
     * @param interval
     * @throws InterruptedException
     */
    protected void sleepAwhile(int interval) throws InterruptedException {
        if (interval > 0) {
            Thread.sleep(interval);
        }
    }

    protected void println(String str) {
        System.out.println("[" + name + "] " + str);
    }

}

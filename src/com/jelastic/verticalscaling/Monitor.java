package com.jelastic.verticalscaling;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ruslan
 */
public class Monitor implements Runnable {

    private volatile boolean isKilled;

    private volatile double memPercentage = 0;

    public void kill() {
        this.isKilled = true;
    }

    @Override
    public void run() {

        System.out.println("[MemoryUsage] Monitor Started");

        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss.SSS");

        while (!isKilled) {
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            java.lang.management.MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            System.out.println("[MemoryUsage] " + ft.format(new Date()) + " -> " + memoryUsageToString(mu));
            double oldPercent = memPercentage;
            double newPercent = 1.0 * mu.getUsed() / mu.getMax();
            if(oldPercent <= 0.1 && newPercent > 0.1) {
                SignalTool.notifySignal(SignalTool.SIGNAL_ID_UP);
            } else if(oldPercent >= 0.5 && newPercent < 0.5) {
                SignalTool.notifySignal(SignalTool.SIGNAL_ID_DOWN);
            }
            memPercentage = newPercent;
        }
        System.out.println("[MemoryUsage] Monitor Stopped");
    }

    private String getIndicatorString(String name, double value) {
        return (name + ": " + Math.round(value / 1024 / 1024) + "M ");
    }

    private String memoryUsageToString(java.lang.management.MemoryUsage mu) {
        return getIndicatorString("Init", mu.getInit()) + " "
               + getIndicatorString("Used", mu.getUsed()) + " "
               + getIndicatorString("Committed", mu.getCommitted()) + " "
               + getIndicatorString("Max", mu.getMax()) + " ";
    }

}

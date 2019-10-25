package com.jelastic.verticalscaling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SignalTool
 *
 * @author Deven
 * @version : SignalTool, v 0.1 2019-10-23 10:03 Deven Exp$
 */
public class SignalTool {

    private static Map<String, Object> signalMap = new ConcurrentHashMap();

    public static final String SIGNAL_ID_UP   = "UP";
    public static final String SIGNAL_ID_DOWN = "DOWN";

    public static void notifySignal(String signalId) {
        if(signalId == null || signalId.trim().isEmpty()) {
            throw new IllegalArgumentException("illegal signalId");
        }
        signalMap.putIfAbsent(signalId, new Object());
        Object signal = signalMap.get(signalId);
        synchronized (signal) {
            signal.notifyAll();
        }
    }

    public static void waitSignal(String signalId, long milliSeconds) throws InterruptedException {
        if(signalId == null || signalId.trim().isEmpty()) {
            throw new IllegalArgumentException("illegal signalId");
        }
        signalMap.putIfAbsent(signalId, new Object());
        Object signal = signalMap.get(signalId);
        synchronized (signal) {
            signal.wait(milliSeconds);
        }
    }

}

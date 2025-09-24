package com.bentzn.util;

/**
 * Util class for time-consuming tasks.
 *
 * @author Grok/bentzn
 * @generated 2025-09-23T00:00:00Z
 */
public final class TimeConsumingTasksUtil {

    private TimeConsumingTasksUtil() {
    }



    /**
     * Performs a wall clock time-consuming task.
     */
    public static void performWallClockTimeConsumingTask() {
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }



    /**
     * Performs a CPU time-consuming task.
     */
    public static void performCpuTimeConsumingTask() {
        int cntCalibration = 10000;
        long timeStartCal = System.currentTimeMillis();
        double sum = 0.0;
        for (int i = 0; i < cntCalibration; i++) {
            sum += Math.sin((double) i);
        }
        long timeEndCal = System.currentTimeMillis();
        long msTakenCal = timeEndCal - timeStartCal;
        if (msTakenCal <= 0)
            msTakenCal = 1;
        double iterationsPerMs = (double) cntCalibration / msTakenCal;
        long msTarget = 300;
        // Factor 3 to adjust for Hotspot optimization (probably)
        long cntIterations = (long) (iterationsPerMs * msTarget * 3);
        for (long i = 0; i < cntIterations; i++) {
            sum += Math.sin((double) i);
        }
        // Use sum to prevent optimization
        if (sum == 0.0)
            throw new RuntimeException("Unexpected sum");
    }
}
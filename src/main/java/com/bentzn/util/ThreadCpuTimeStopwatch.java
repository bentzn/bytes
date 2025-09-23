package com.bentzn.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Utility class for measuring CPU time usage of the current thread.
 * 
 * <p>
 * This class provides a simple start/stop interface for measuring the actual
 * CPU time consumed by the current thread during code execution. It uses the
 * {@link ThreadMXBean} to retrieve precise CPU time measurements in
 * nanoseconds.
 * </p>
 * 
 * <p>
 * <strong>Usage Example:</strong>
 * </p>
 * 
 * <pre>{@code
 * ThreadCpuTimeStopwatch util = new ThreadCpuTimeStopwatch();
 * util.start();
 * // Your code to measure here
 * long cpuTimeNs = util.stop();
 * }</pre>
 * 
 * <p>
 * <strong>Key Features:</strong>
 * </p>
 * <ul>
 * <li>Measures actual CPU time, not wall-clock time</li>
 * <li>Returns results in nanoseconds for high precision</li>
 * <li>Thread-safe for single-threaded measurement</li>
 * <li>Automatic initialization of ThreadMXBean</li>
 * </ul>
 * 
 * <p>
 * <strong>Important Notes:</strong>
 * </p>
 * <ul>
 * <li>Only measures CPU time of the current thread</li>
 * <li>Requires {@code ThreadCpuTimeEnabled} to be supported by the JVM</li>
 * <li>Start must be called before stop</li>
 * <li>Nested measurements are not supported</li>
 * </ul>
 * 
 * @author Grok/bentzn
 * @generated 2025-09-22T00:00:00Z
 * @see ThreadMXBean
 * @see ManagementFactory#getThreadMXBean()
 */
public class ThreadCpuTimeStopwatch {
    /** ThreadMXBean instance for CPU time measurement */
    private final ThreadMXBean threadMXBean;

    /** Start CPU time in nanoseconds */
    private long startTimeNs;

    /** Flag indicating if measurement is currently active */
    private boolean isRunning;

    /**
     * Constructs a new CPU time utility and initializes the ThreadMXBean.
     * 
     * <p>
     * Automatically enables CPU time measurement for the current JVM.
     * </p>
     */
    public ThreadCpuTimeStopwatch() {
        threadMXBean = ManagementFactory.getThreadMXBean();
        threadMXBean.setThreadCpuTimeEnabled(true);
    }



    /**
     * Starts measuring CPU time for the current thread.
     * 
     * @throws IllegalStateException if measurement is already running
     */
    public void start() {
        if (isRunning) {
            throw new IllegalStateException("Already running");
        }
        startTimeNs = threadMXBean.getThreadCpuTime(Thread.currentThread().threadId());
        isRunning = true;
    }



    /**
     * Stops measuring CPU time and returns the elapsed CPU time in nanoseconds.
     * 
     * @return elapsed CPU time in nanoseconds
     * @throws IllegalStateException if measurement has not been started
     */
    public long stopNanos() {
        if (!isRunning) {
            throw new IllegalStateException("Not running");
        }
        long threadId = Thread.currentThread().threadId();
        long endTimeNs = threadMXBean.getThreadCpuTime(threadId);
        isRunning = false;
        return endTimeNs - startTimeNs;
    }
    
    
    
    /**
     * Stops measuring CPU time and returns the elapsed CPU time in millis.
     * 
     * @return elapsed CPU time in millis
     * @throws IllegalStateException if measurement has not been started
     */
    public long stop() {
        return stopNanos() / 1_000_000;
    }
}
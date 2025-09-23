package com.bentzn.util;

public interface StopWatch_i {

    /**
     * Starts measuring CPU time for the current thread.
     * 
     * @throws IllegalStateException if measurement is already running
     */
    void start();



    /**
     * Stops measuring CPU time and returns the elapsed CPU time in millis.
     * 
     * @return elapsed CPU time in millis
     * @throws IllegalStateException if measurement has not been started
     */
    long stop();

}
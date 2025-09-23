package com.bentzn.util;

public class StopWatchWallClock implements StopWatch_i{
    
    
    private volatile long time0;

    @Override
    public void start() {
        time0 = System.currentTimeMillis();
    }

    @Override
    public long stop() {
        return System.currentTimeMillis() - time0;
    }

}

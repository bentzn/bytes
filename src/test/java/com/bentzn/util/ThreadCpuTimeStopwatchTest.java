package com.bentzn.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * JUnit 5 tests for CpuTimeUtility.
 * 
 * @author Grok/bentzn
 * @generated 2025-09-22T00:00:00Z
 */
public class ThreadCpuTimeStopwatchTest {

    private ThreadCpuTimeStopwatch util;

    @BeforeEach
    void setUp() {
        util = new ThreadCpuTimeStopwatch();
    }



    @Test
    void testMeasureSimpleOperation() {
        util.start();
        // Simple CPU-intensive operation
        int sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += i;
        }
        if (sum < 0)
            fail();
        long cpuTimeNs = util.stopNanos();

        assertTrue(cpuTimeNs > 0, "CPU time should be positive");
        // Should be restartable after stop
        assertDoesNotThrow(() -> util.start());
    }



    @Test
    void testStartTwice() {
        util.start();
        assertThrows(IllegalStateException.class, () -> util.start());
    }



    @Test
    void testStopWithoutStart() {
        assertThrows(IllegalStateException.class, util::stop);
    }



    @Test
    void testThreadCpuTimeSupported() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        assertTrue(bean.isThreadCpuTimeSupported() || bean.isThreadCpuTimeEnabled(),
                "Thread CPU time should be supported or enabled");
    }
}
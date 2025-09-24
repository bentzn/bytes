package com.bentzn.util;

/**
 * Test class for TimeConsumingTasksUtil.
 *
 * @author Grok/bentzn
 * @generated 2025-09-23T11:10:00Z
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimeConsumingTasksUtilTest {

    @Test
    void testPerformWallClockTimeConsumingTask() {
        long timeStart = System.currentTimeMillis();
        TimeConsumingTasksUtil.performWallClockTimeConsumingTask();
        long timeEnd = System.currentTimeMillis();
        long duration = timeEnd - timeStart;
        assertTrue(duration >= 450 && duration <= 550);
    }

    @Test
    void testPerformCpuTimeConsumingTask() {
        long timeStart = System.currentTimeMillis();
        TimeConsumingTasksUtil.performCpuTimeConsumingTask();
        long timeEnd = System.currentTimeMillis();
        long duration = timeEnd - timeStart;
        System.out.println(duration);
        assertTrue(duration > 100);
    }

}
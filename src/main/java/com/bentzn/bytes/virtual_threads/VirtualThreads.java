package com.bentzn.bytes.virtual_threads;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import com.bentzn.util.StopWatchWallClock;
import com.bentzn.util.TimeConsumingTasksUtil;

public class VirtualThreads {

    private static final int cntElements = 10_000;
    
    private static Supplier<AbstractTask> taskSupplier;
    
    public static void main(String[] args) throws InterruptedException {
        taskSupplier = TaskCpu::new;
        run("CPU intensive");

        taskSupplier = TaskWallClock::new;
        run("Time intensive");
        
    }

    public static void run(String strId) throws InterruptedException {
        // Warm up
        executeWithVirtualThreads();
        executeAsCommandPattern();
        executeWithThreadPool();

        // Test
        StopWatchWallClock stopWatch = new StopWatchWallClock();
        
        System.out.println(strId);

        stopWatch.start();
        executeWithVirtualThreads();
        System.out.println("Time, virtual threads: " + stopWatch.stop());

        stopWatch.start();
        executeAsCommandPattern();
        System.out.println("Time, command pattern: " + stopWatch.stop());

        stopWatch.start();
        executeWithThreadPool();
        System.out.println("Time, thread pool:     " + stopWatch.stop());
        
    }



    private static void executeWithVirtualThreads() throws InterruptedException {
        List<AbstractTask> lstTasks = getTaskList();
        for (AbstractTask task : lstTasks) {
            Thread.ofVirtual().start(() -> {
                task.performTask();
            });
        }

        for (AbstractTask task : lstTasks) {
            while (!task.isDone()) {
                Thread.sleep(1);
            }
        }
    }



    private static void executeAsCommandPattern() throws InterruptedException {
        List<AbstractTask> lstTasks = getTaskList();
        Queue<AbstractTask> queue = new ConcurrentLinkedQueue<>(lstTasks);

        int cntThreads = 1000;
        Thread[] threads = new Thread[cntThreads];
        for (int i = 0; i < cntThreads; i++) {
            threads[i] = new Thread() {
                public void run() {
                    AbstractTask task = null;
                    while ((task = queue.poll()) != null) {
                        task.performTask();
                    }
                }
            };
            threads[i].setDaemon(true);
            threads[i].start();
        }

        for (AbstractTask task : lstTasks) {
            // could've used join() here
            // but to keep things equal...
            while (!task.isDone()) {
                Thread.sleep(1);
            }
        }
    }



    private static void executeWithThreadPool() throws InterruptedException {
        ExecutorService svcExecutor = Executors.newFixedThreadPool(1000);
        List<AbstractTask> lstTasks = getTaskList();
        for (AbstractTask task : lstTasks) {
            svcExecutor.submit(() -> {
                task.performTask();
            });
        }

        for (AbstractTask task : lstTasks) {
            while (!task.isDone()) {
                Thread.sleep(1);
            }
        }
        
        svcExecutor.close();
    }



    private static List<AbstractTask> getTaskList() {
        List<AbstractTask> lstTasks = new LinkedList<>();
        for (int i = 0; i < cntElements; i++) {
            lstTasks.add(taskSupplier.get());
        }

        return lstTasks;
    }
}

abstract class AbstractTask {

    protected volatile boolean done = false;

    abstract void performTask();



    public boolean isDone() {
        return done;
    }
}

class TaskWallClock extends AbstractTask {

    public void performTask() {
        TimeConsumingTasksUtil.performWallClockTimeConsumingTask();
        done = true;
    }
}

class TaskCpu extends AbstractTask {

    public void performTask() {
        TimeConsumingTasksUtil.performCpuTimeConsumingTask();
        done = true;
    }
}
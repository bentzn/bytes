package com.bentzn.bytes.streams;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import com.bentzn.util.ThreadCpuTimeStopwatch;

public class StreamsPerformance {


    public static void main(String[] args) throws InterruptedException {
        // List to work with
        System.out.println("Populate list...");
        List<Integer> lst = new LinkedList<>();
        Random random = new Random();
        for (int i = 0; i < 1_000_000; i++) {
            lst.add(random.nextInt());
        }

        // Warm up for the compiler
        System.out.println("Warm up...");
        run(lst, 10_000, new ResultsCombined());

        // Now test
        System.out.println("Testing...");

        ResultsCombined resultsCombined = new ResultsCombined();
        run(lst, 100, resultsCombined);
        System.out.println("Wall clock time:");
        System.out.println("Stream, Optional:   " + (resultsCombined.wallClockTime.timeStreamOptional));
        System.out.println("Stream, ForEach:    " + (resultsCombined.wallClockTime.timeStreamForEach));
        System.out.println("Stream, parallel:   " + (resultsCombined.wallClockTime.timeStreamParallel));
        System.out.println("For-each loop:      " + (resultsCombined.wallClockTime.timeForEachLoop));
        System.out.println("CPU time:");
        System.out.println("Stream, Optional:    " + (resultsCombined.cpuTime.timeStreamOptional));
        System.out.println("Stream, ForEach:     " + (resultsCombined.cpuTime.timeStreamForEach));
        System.out.println("Stream, parallel:    " + (resultsCombined.cpuTime.timeStreamParallel));
        System.out.println("For-each loop:       " + (resultsCombined.cpuTime.timeForEachLoop));
        System.out.println("");
        System.out.println("");
    }



    private static void run(List<Integer> lst, int loops, ResultsCombined results) throws InterruptedException {
        long time0;
        ThreadCpuTimeStopwatch cpuTimeUtil = new ThreadCpuTimeStopwatch();

        // Stream/optional
        time0 = System.currentTimeMillis();
        cpuTimeUtil.start();
        for (int i = 0; i < loops; i++) {
            calculateWithStream(lst);
        }
        results.wallClockTime.timeStreamOptional += (System.currentTimeMillis() - time0);
        results.cpuTime.timeStreamOptional += cpuTimeUtil.stop();

        // Stream/for-each
        time0 = System.currentTimeMillis();
        cpuTimeUtil.start();
        for (int i = 0; i < loops; i++) {
            calculateWithStreamForEach(lst);
        }
        results.wallClockTime.timeStreamForEach += (System.currentTimeMillis() - time0);
        results.cpuTime.timeStreamForEach += cpuTimeUtil.stop();

        // Stream, parallel
        time0 = System.currentTimeMillis();
        cpuTimeUtil.start();
        for (int i = 0; i < loops; i++) {
            calculateWithParallelStream(lst);
        }
        results.wallClockTime.timeStreamParallel += (System.currentTimeMillis() - time0);
        results.cpuTime.timeStreamParallel += cpuTimeUtil.stop();

        // For-each-loop
        time0 = System.currentTimeMillis();
        cpuTimeUtil.start();
        for (int i = 0; i < loops; i++) {
            calculateWithForLoop(lst);
        }
        results.wallClockTime.timeForEachLoop += (System.currentTimeMillis() - time0);
        results.cpuTime.timeForEachLoop += cpuTimeUtil.stop();
    }



    private static int calculateWithStream(List<Integer> lst) {
        return Optional.of(lst.stream().mapToInt(Integer::intValue).sum()).get();
    }



    private static int calculateWithStreamForEach(List<Integer> lst) {
        SumConsumer sumConsumer = new SumConsumer();
        lst.forEach(sumConsumer::accept);
        return sumConsumer.getResult();
    }



    private static int calculateWithParallelStream(List<Integer> lst) {
        return lst.parallelStream().mapToInt(Integer::intValue).sum();
    }



    private static int calculateWithForLoop(List<Integer> lst) {
        int result = 0;
        for (Integer val : lst) {
            result += val.intValue();
        }

        return result;
    }
}

class SumConsumer implements Consumer<Integer> {

    private int result = 0;

    @Override
    public void accept(Integer t) {
        result += t;
    }



    public int getResult() {
        return result;
    }
}

class ResultsTime {
    long timeStreamOptional;
    long timeStreamForEach;
    long timeStreamParallel;
    long timeForEachLoop;
}

class ResultsCombined {
    ResultsTime wallClockTime = new ResultsTime();
    ResultsTime cpuTime = new ResultsTime();
}

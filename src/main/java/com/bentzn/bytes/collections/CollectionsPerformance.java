package com.bentzn.bytes.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.function.Supplier;

import com.bentzn.util.heap.HeapUsageUtil;
import com.bentzn.util.stopwatch.StopWatchThreadCpuTime;

public class CollectionsPerformance {

    private static Random random = new Random();

    public static void main(String[] args) {
        run(1_000_000);
    }



    public static void run(int cntElements) {
        StopWatchThreadCpuTime stopwatch = new StopWatchThreadCpuTime();
        HeapUsageUtil heapUsageUtil = new HeapUsageUtil();

        Map<String, Supplier<Collection<String>>> map = new LinkedHashMap<>();
        map.put("LinkedList", LinkedList::new);
        map.put("ArrayList", ArrayList::new);
        map.put("HashSet", HashSet::new);
        map.put("TreeSet", TreeSet::new);

        // Warm up
        for (String key : map.keySet()) {
            createCollection(cntElements, map.get(key));
        }

        for (String key : map.keySet()) {
            System.out.println(key);

            // Allocation
            System.out.println("Allocation");
            stopwatch.start();
            heapUsageUtil.start();
            Collection<String> coll = createCollection(cntElements, map.get(key));
            System.out.println("    heapusage: " + heapUsageUtil.stopKb());
            System.out.println("    CPU time: " + stopwatch.stop());

            // Iteration
            System.out.println("Iteration");
            heapUsageUtil.start();
            stopwatch.start();
            for (String str : coll) {
                if (str == null)
                    throw new NullPointerException();
            }
            System.out.println("    heapusage: " + heapUsageUtil.stopKb());
            System.out.println("    CPU time: " + stopwatch.stop());

            // contains
            System.out.println("Contains");
            List<String> sample = new ArrayList<>(coll);
            Collections.shuffle(sample);
            sample = sample.subList(0, (int) cntElements / 1000);
            heapUsageUtil.start();
            stopwatch.start();
            for (String str : sample) {
                if (!coll.contains(str))
                    throw new NullPointerException();
            }
            System.out.println("    heapusage: " + heapUsageUtil.stopKb());
            System.out.println("    CPU time: " + stopwatch.stop());

            // Random access (lists only)
            if (coll instanceof List lst) {
                System.out.println("Random access");
                heapUsageUtil.start();
                stopwatch.start();
                for (int i = 0; i < 1000; i++) {
                    if (lst.get(random.nextInt(0, cntElements)) == null)
                        throw new NullPointerException();
                }
                System.out.println("    heapusage: " + heapUsageUtil.stopKb());
                System.out.println("    CPU time: " + stopwatch.stop());
            }

            // to avoid optimizations
            System.out.println(coll.stream().count());

            System.out.println();
        }
    }



    static Collection<String> createCollection(int cntElements, Supplier<Collection<String>> supp) {
        Collection<String> coll = supp.get();
        for (int i = 0; i < cntElements; i++) {
            coll.add("element_" + i);
        }
        return coll;
    }

}

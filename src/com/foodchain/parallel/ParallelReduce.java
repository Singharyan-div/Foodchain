package com.foodchain.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/** Fork-join reduction for total, maximum, and average supply statistics. */
public final class ParallelReduce {
    private static final int SEQUENTIAL_THRESHOLD = 2;
    private ParallelReduce() { }

    public static Statistics analyze(long[] values) {
        if (values == null) throw new IllegalArgumentException("Values are required.");
        if (values.length == 0) return new Statistics(0, 0, 0, 0.0);
        ForkJoinPool pool = new ForkJoinPool();
        try {
            Summary summary = pool.invoke(new ReduceTask(values, 0, values.length));
            return new Statistics(summary.total, summary.maximum, summary.count,
                    (double) summary.total / summary.count);
        } finally {
            pool.shutdown();
        }
    }

    private static final class ReduceTask extends RecursiveTask<Summary> {
        private static final long serialVersionUID = 1L;
        private final long[] values;
        private final int start;
        private final int end;

        private ReduceTask(long[] values, int start, int end) {
            this.values = values;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Summary compute() {
            if (end - start <= SEQUENTIAL_THRESHOLD) {
                long total = 0;
                long maximum = Long.MIN_VALUE;
                for (int i = start; i < end; i++) {
                    total = Math.addExact(total, values[i]);
                    maximum = Math.max(maximum, values[i]);
                }
                return new Summary(total, maximum, end - start);
            }
            int middle = start + (end - start) / 2;
            ReduceTask left = new ReduceTask(values, start, middle);
            ReduceTask right = new ReduceTask(values, middle, end);
            left.fork();
            Summary rightResult = right.compute();
            Summary leftResult = left.join();
            return new Summary(Math.addExact(leftResult.total, rightResult.total),
                    Math.max(leftResult.maximum, rightResult.maximum),
                    leftResult.count + rightResult.count);
        }
    }

    private static final class Summary {
        private final long total;
        private final long maximum;
        private final int count;
        private Summary(long total, long maximum, int count) {
            this.total = total;
            this.maximum = maximum;
            this.count = count;
        }
    }

    public static final class Statistics {
        private final long total;
        private final long maximum;
        private final int count;
        private final double average;
        private Statistics(long total, long maximum, int count, double average) {
            this.total = total;
            this.maximum = maximum;
            this.count = count;
            this.average = average;
        }
        public long getTotal() { return total; }
        public long getMaximum() { return maximum; }
        public int getCount() { return count; }
        public double getAverage() { return average; }
    }
}

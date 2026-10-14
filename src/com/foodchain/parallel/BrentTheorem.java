package com.foodchain.parallel;

/** Simple Brent-theorem work/depth estimate for a parallel supply-chain job. */
public final class BrentTheorem {
    private BrentTheorem() { }

    public static Estimate estimate(long totalWork, long criticalPath, int processors) {
        if (totalWork < 0 || criticalPath < 0 || processors <= 0 || criticalPath > totalWork) {
            throw new IllegalArgumentException("Require work >= critical path >= 0 and at least one processor.");
        }
        long averageWork = ceilDivide(totalWork, processors);
        long lowerBound = Math.max(averageWork, criticalPath);
        long upperBound = Math.addExact(averageWork, criticalPath);
        return new Estimate(lowerBound, upperBound, processors);
    }

    private static long ceilDivide(long numerator, int denominator) {
        return numerator / denominator + (numerator % denominator == 0 ? 0 : 1);
    }

    public static final class Estimate {
        private final long lowerBound;
        private final long brentUpperBound;
        private final int processors;
        private Estimate(long lowerBound, long brentUpperBound, int processors) {
            this.lowerBound = lowerBound;
            this.brentUpperBound = brentUpperBound;
            this.processors = processors;
        }
        public long getLowerBound() { return lowerBound; }
        public long getBrentUpperBound() { return brentUpperBound; }
        public int getProcessors() { return processors; }
    }
}

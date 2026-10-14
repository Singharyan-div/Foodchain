package com.foodchain.dsa.dp;

/** Matrix-chain dynamic programming for minimizing supply-data operation cost. */
public final class MatrixChainOptimizer {
    private MatrixChainOptimizer() { }

    /** dimensions[i-1] x dimensions[i] describes matrix i; O(n^3) time, O(n^2) space. */
    public static Result optimize(int[] dimensions) {
        if (dimensions == null || dimensions.length < 2) {
            throw new IllegalArgumentException("At least one matrix is required.");
        }
        for (int dimension : dimensions) {
            if (dimension <= 0) throw new IllegalArgumentException("Matrix dimensions must be positive.");
        }
        int matrices = dimensions.length - 1;
        long[][] cost = new long[matrices][matrices];
        int[][] split = new int[matrices][matrices];
        for (int length = 2; length <= matrices; length++) {
            for (int start = 0; start + length <= matrices; start++) {
                int end = start + length - 1;
                cost[start][end] = Long.MAX_VALUE;
                for (int middle = start; middle < end; middle++) {
                    long multiplication;
                    long candidate;
                    try {
                        multiplication = Math.multiplyExact(
                                Math.multiplyExact((long) dimensions[start], dimensions[middle + 1]),
                                dimensions[end + 1]);
                        candidate = Math.addExact(Math.addExact(cost[start][middle],
                                cost[middle + 1][end]), multiplication);
                    } catch (ArithmeticException exception) {
                        continue;
                    }
                    if (candidate < cost[start][end]) {
                        cost[start][end] = candidate;
                        split[start][end] = middle;
                    }
                }
                if (cost[start][end] == Long.MAX_VALUE) {
                    throw new IllegalArgumentException("Required multiplication cost exceeds the supported range.");
                }
            }
        }
        return new Result(cost[0][matrices - 1], buildOrder(split, 0, matrices - 1));
    }

    private static String buildOrder(int[][] split, int start, int end) {
        if (start == end) return "A" + (start + 1);
        int middle = split[start][end];
        return "(" + buildOrder(split, start, middle) + " x "
                + buildOrder(split, middle + 1, end) + ")";
    }

    public static final class Result {
        private final long scalarMultiplications;
        private final String parenthesization;

        private Result(long scalarMultiplications, String parenthesization) {
            this.scalarMultiplications = scalarMultiplications;
            this.parenthesization = parenthesization;
        }

        public long getScalarMultiplications() { return scalarMultiplications; }
        public String getParenthesization() { return parenthesization; }
    }
}

package com.foodchain.dsa.dp;

import java.util.List;

/** Optimal BST DP using successful-key and unsuccessful-gap frequencies. */
public final class OptimalBinarySearchTree {
    private OptimalBinarySearchTree() { }

    public static Result build(List<String> sortedKeys, double[] successful, double[] unsuccessful) {
        if (sortedKeys == null || successful == null || unsuccessful == null) {
            throw new IllegalArgumentException("Keys and frequency arrays are required.");
        }
        int n = sortedKeys.size();
        if (successful.length != n || unsuccessful.length != n + 1) {
            throw new IllegalArgumentException("Expected n successful and n+1 unsuccessful frequencies.");
        }
        double[][] cost = new double[n + 2][n + 1];
        double[][] weight = new double[n + 2][n + 1];
        int[][] root = new int[n + 2][n + 1];
        for (int i = 1; i <= n + 1; i++) {
            cost[i][i - 1] = unsuccessful[i - 1];
            weight[i][i - 1] = unsuccessful[i - 1];
        }
        for (int length = 1; length <= n; length++) {
            for (int i = 1; i + length - 1 <= n; i++) {
                int j = i + length - 1;
                cost[i][j] = Double.POSITIVE_INFINITY;
                weight[i][j] = weight[i][j - 1] + successful[j - 1] + unsuccessful[j];
                for (int candidateRoot = i; candidateRoot <= j; candidateRoot++) {
                    double candidate = cost[i][candidateRoot - 1]
                            + cost[candidateRoot + 1][j] + weight[i][j];
                    if (candidate < cost[i][j]) {
                        cost[i][j] = candidate;
                        root[i][j] = candidateRoot;
                    }
                }
            }
        }
        String tree = n == 0 ? "(empty index)" : describe(root, sortedKeys, 1, n);
        double expected = n == 0 ? unsuccessful[0] : cost[1][n];
        return new Result(expected, tree);
    }

    private static String describe(int[][] root, List<String> keys, int first, int last) {
        if (first > last) return "-";
        int keyIndex = root[first][last];
        return "(" + describe(root, keys, first, keyIndex - 1) + " <- "
                + keys.get(keyIndex - 1) + " -> "
                + describe(root, keys, keyIndex + 1, last) + ")";
    }

    public static final class Result {
        private final double weightedSearchCost;
        private final String tree;

        private Result(double weightedSearchCost, String tree) {
            this.weightedSearchCost = weightedSearchCost;
            this.tree = tree;
        }

        public double getWeightedSearchCost() { return weightedSearchCost; }
        public String getTree() { return tree; }
    }
}

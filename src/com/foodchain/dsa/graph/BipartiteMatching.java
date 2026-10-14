package com.foodchain.dsa.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Maximum-cardinality bipartite matching with augmenting paths. */
public final class BipartiteMatching {
    private BipartiteMatching() { }

    public static Result solve(int leftSize, int rightSize, List<int[]> eligiblePairs) {
        if (leftSize < 0 || rightSize < 0 || eligiblePairs == null) {
            throw new IllegalArgumentException("Valid partition sizes and eligible pairs are required.");
        }
        List<List<Integer>> adjacency = new ArrayList<List<Integer>>(leftSize);
        for (int i = 0; i < leftSize; i++) adjacency.add(new ArrayList<Integer>());
        for (int[] pair : eligiblePairs) {
            if (pair == null || pair.length != 2 || pair[0] < 0 || pair[0] >= leftSize
                    || pair[1] < 0 || pair[1] >= rightSize) {
                throw new IllegalArgumentException("An eligible pair has an invalid endpoint.");
            }
            adjacency.get(pair[0]).add(pair[1]);
        }
        int[] rightMatch = new int[rightSize];
        Arrays.fill(rightMatch, -1);
        int matched = 0;
        for (int left = 0; left < leftSize; left++) {
            boolean[] visitedRight = new boolean[rightSize];
            if (augment(left, adjacency, visitedRight, rightMatch)) matched++;
        }
        int[] leftMatch = new int[leftSize];
        Arrays.fill(leftMatch, -1);
        for (int right = 0; right < rightSize; right++) {
            if (rightMatch[right] >= 0) leftMatch[rightMatch[right]] = right;
        }
        return new Result(matched, leftMatch);
    }

    private static boolean augment(int left, List<List<Integer>> adjacency,
            boolean[] visitedRight, int[] rightMatch) {
        for (Integer boxedRight : adjacency.get(left)) {
            int right = boxedRight.intValue();
            if (visitedRight[right]) continue;
            visitedRight[right] = true;
            if (rightMatch[right] == -1
                    || augment(rightMatch[right], adjacency, visitedRight, rightMatch)) {
                rightMatch[right] = left;
                return true;
            }
        }
        return false;
    }

    public static final class Result {
        private final int cardinality;
        private final int[] rightByLeft;

        private Result(int cardinality, int[] rightByLeft) {
            this.cardinality = cardinality;
            this.rightByLeft = rightByLeft;
        }

        public int getCardinality() { return cardinality; }
        public int[] getRightByLeft() { return Arrays.copyOf(rightByLeft, rightByLeft.length); }
    }
}

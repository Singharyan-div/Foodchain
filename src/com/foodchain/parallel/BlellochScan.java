package com.foodchain.parallel;

import java.util.Arrays;
import java.util.stream.IntStream;

/** Work-efficient up-sweep/down-sweep prefix scan for inventory quantities. */
public final class BlellochScan {
    private BlellochScan() { }

    /** Returns inclusive prefix totals; O(n) work and O(n) auxiliary space. */
    public static long[] inclusive(long[] values) {
        if (values == null) throw new IllegalArgumentException("Values are required.");
        if (values.length == 0) return new long[0];
        int size = 1;
        while (size < values.length) {
            if (size > Integer.MAX_VALUE / 2) throw new IllegalArgumentException("Input is too large.");
            size *= 2;
        }
        long[] tree = Arrays.copyOf(values, size);
        for (int stride = 1; stride < size; stride *= 2) {
            final int currentStride = stride;
            IntStream.range(0, size / (currentStride * 2)).parallel().forEach(block -> {
                int index = (block + 1) * currentStride * 2 - 1;
                tree[index] = Math.addExact(tree[index], tree[index - currentStride]);
            });
            if (stride > size / 2) break;
        }
        tree[size - 1] = 0;
        for (int stride = size / 2; stride >= 1; stride /= 2) {
            final int currentStride = stride;
            IntStream.range(0, size / (currentStride * 2)).parallel().forEach(block -> {
                int index = (block + 1) * currentStride * 2 - 1;
                long leftTotal = tree[index - currentStride];
                tree[index - currentStride] = tree[index];
                tree[index] = Math.addExact(tree[index], leftTotal);
            });
            if (stride == 1) break;
        }
        long[] inclusive = new long[values.length];
        for (int i = 0; i < values.length; i++) inclusive[i] = Math.addExact(tree[i], values[i]);
        return inclusive;
    }
}

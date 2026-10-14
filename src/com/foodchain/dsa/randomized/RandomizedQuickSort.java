package com.foodchain.dsa.randomized;

import java.util.Comparator;
import java.util.Random;

/** In-place three-way randomized quicksort for demand-ranked products. */
public final class RandomizedQuickSort {
    private RandomizedQuickSort() { }

    public static <T> void sort(T[] values, Comparator<? super T> order, Random random) {
        if (values == null || order == null || random == null) {
            throw new IllegalArgumentException("Values, comparator, and random source are required.");
        }
        sortRange(values, 0, values.length - 1, order, random);
    }

    private static <T> void sortRange(T[] values, int low, int high,
            Comparator<? super T> order, Random random) {
        while (low < high) {
            T pivot = values[low + random.nextInt(high - low + 1)];
            int less = low;
            int current = low;
            int greater = high;
            while (current <= greater) {
                int comparison = order.compare(values[current], pivot);
                if (comparison < 0) swap(values, less++, current++);
                else if (comparison > 0) swap(values, current, greater--);
                else current++;
            }
            if (less - low < high - greater) {
                sortRange(values, low, less - 1, order, random);
                low = greater + 1;
            } else {
                sortRange(values, greater + 1, high, order, random);
                high = less - 1;
            }
        }
    }

    private static <T> void swap(T[] values, int first, int second) {
        T temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }
}

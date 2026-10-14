package com.foodchain.dsa.suffix;

import java.util.Arrays;

/** Suffix-array builders: SA-IS for indexing and doubling for comparison. */
public final class SuffixArray {
    private SuffixArray() { }

    /**
     * Builds suffix positions in lexicographic order with SA-IS.
     * The returned array has one position per UTF-16 character in {@code text}.
     */
    public static int[] build(String text) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null.");
        int n = text.length();
        if (n == 0) return new int[0];

        boolean[] present = new boolean[Character.MAX_VALUE + 1];
        for (int i = 0; i < n; i++) present[text.charAt(i)] = true;
        int[] rank = new int[present.length];
        int upper = -1;
        for (int character = 0; character < present.length; character++) {
            if (present[character]) rank[character] = ++upper;
        }
        int[] symbols = new int[n];
        for (int i = 0; i < n; i++) symbols[i] = rank[text.charAt(i)];
        return saIs(symbols, upper);
    }

    /**
     * Reference prefix-doubling implementation useful for smaller inputs and
     * algorithm comparison. Runs in O(n log^2 n) time and O(n) auxiliary space.
     */
    public static int[] buildByDoubling(String text) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null.");
        final int n = text.length();
        Integer[] order = new Integer[n];
        int[] rank = new int[n];
        int[] nextRank = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
            rank[i] = text.charAt(i);
        }
        for (int width = 1; width < n; width = width > n / 2 ? n : width * 2) {
            final int currentWidth = width;
            final int[] currentRank = rank;
            Arrays.sort(order, (left, right) -> {
                int first = Integer.compare(currentRank[left], currentRank[right]);
                if (first != 0) return first;
                int leftSecond = left + currentWidth < n ? currentRank[left + currentWidth] : -1;
                int rightSecond = right + currentWidth < n ? currentRank[right + currentWidth] : -1;
                return Integer.compare(leftSecond, rightSecond);
            });
            nextRank[order[0]] = 0;
            for (int i = 1; i < n; i++) {
                int previous = order[i - 1];
                int current = order[i];
                boolean different = rank[previous] != rank[current];
                int previousSecond = previous + width < n ? rank[previous + width] : -1;
                int currentSecond = current + width < n ? rank[current + width] : -1;
                if (previousSecond != currentSecond) different = true;
                nextRank[current] = nextRank[previous] + (different ? 1 : 0);
            }
            int[] swap = rank;
            rank = nextRank;
            nextRank = swap;
            if (rank[order[n - 1]] == n - 1) break;
            if (width == n) break;
        }
        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = order[i];
        return result;
    }

    private static int[] saIs(int[] text, int upper) {
        int n = text.length;
        if (n == 0) return new int[0];
        if (n == 1) return new int[] { 0 };
        if (n < 24) return naiveSuffixArray(text);

        int[] suffixes = new int[n];
        boolean[] isS = new boolean[n];
        for (int i = n - 2; i >= 0; i--) {
            isS[i] = text[i] == text[i + 1] ? isS[i + 1] : text[i] < text[i + 1];
        }

        int[] sumL = new int[upper + 1];
        int[] sumS = new int[upper + 1];
        for (int i = 0; i < n; i++) {
            if (isS[i]) sumL[text[i] + 1]++;
            else sumS[text[i]]++;
        }
        for (int i = 0; i <= upper; i++) {
            sumS[i] += sumL[i];
            if (i < upper) sumL[i + 1] += sumS[i];
        }

        int lmsCount = 0;
        for (int i = 1; i < n; i++) if (!isS[i - 1] && isS[i]) lmsCount++;
        int[] lms = new int[lmsCount];
        int lmsIndex = 0;
        for (int i = 1; i < n; i++) {
            if (!isS[i - 1] && isS[i]) lms[lmsIndex++] = i;
        }

        induce(text, isS, sumL, sumS, lms, suffixes);
        if (lmsCount > 0) {
            int[] lmsMap = new int[n];
            Arrays.fill(lmsMap, -1);
            for (int i = 0; i < lmsCount; i++) lmsMap[lms[i]] = i;

            int[] sortedLms = new int[lmsCount];
            int sortedCount = 0;
            for (int position : suffixes) {
                if (position >= 0 && lmsMap[position] != -1) sortedLms[sortedCount++] = position;
            }

            int[] reduced = new int[lmsCount];
            int reducedUpper = 0;
            reduced[lmsMap[sortedLms[0]]] = 0;
            for (int i = 1; i < lmsCount; i++) {
                int previous = sortedLms[i - 1];
                int current = sortedLms[i];
                int previousEnd = lmsMap[previous] + 1 < lmsCount
                        ? lms[lmsMap[previous] + 1] : n;
                int currentEnd = lmsMap[current] + 1 < lmsCount
                        ? lms[lmsMap[current] + 1] : n;
                boolean same = previousEnd - previous == currentEnd - current;
                if (same) {
                    int left = previous;
                    int right = current;
                    while (left < previousEnd) {
                        if (text[left] != text[right] || isS[left] != isS[right]) {
                            same = false;
                            break;
                        }
                        left++;
                        right++;
                    }
                    if (same && previousEnd < n && currentEnd < n) {
                        same = text[previousEnd] == text[currentEnd]
                                && isS[previousEnd] == isS[currentEnd];
                    } else if (previousEnd != currentEnd) {
                        same = false;
                    }
                }
                if (!same) reducedUpper++;
                reduced[lmsMap[current]] = reducedUpper;
            }

            int[] reducedSuffixes = saIs(reduced, reducedUpper);
            for (int i = 0; i < lmsCount; i++) sortedLms[i] = lms[reducedSuffixes[i]];
            induce(text, isS, sumL, sumS, sortedLms, suffixes);
        }
        return suffixes;
    }

    private static void induce(int[] text, boolean[] isS, int[] sumL, int[] sumS,
            int[] orderedLms, int[] suffixes) {
        Arrays.fill(suffixes, -1);
        int[] buffer = Arrays.copyOf(sumS, sumS.length);
        for (int position : orderedLms) {
            suffixes[buffer[text[position]]++] = position;
        }
        buffer = Arrays.copyOf(sumL, sumL.length);
        suffixes[buffer[text[text.length - 1]]++] = text.length - 1;
        for (int i = 0; i < suffixes.length; i++) {
            int position = suffixes[i];
            if (position >= 1 && !isS[position - 1]) {
                suffixes[buffer[text[position - 1]]++] = position - 1;
            }
        }
        buffer = Arrays.copyOf(sumL, sumL.length);
        for (int i = suffixes.length - 1; i >= 0; i--) {
            int position = suffixes[i];
            if (position >= 1 && isS[position - 1]) {
                suffixes[--buffer[text[position - 1] + 1]] = position - 1;
            }
        }
    }

    private static int[] naiveSuffixArray(final int[] text) {
        Integer[] order = new Integer[text.length];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, (left, right) -> {
            int a = left;
            int b = right;
            while (a < text.length && b < text.length) {
                if (text[a] != text[b]) return Integer.compare(text[a], text[b]);
                a++;
                b++;
            }
            return Integer.compare(text.length - left, text.length - right);
        });
        int[] result = new int[order.length];
        for (int i = 0; i < order.length; i++) result[i] = order[i];
        return result;
    }
}

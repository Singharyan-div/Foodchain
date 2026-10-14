package com.foodchain.dsa.suffix;

/** Builds the LCP array in linear time using Kasai's algorithm. */
public final class KasaiLcp {
    private KasaiLcp() { }

    /**
     * Returns lcp[r] = LCP(text[sa[r]..], text[sa[r - 1]..]); lcp[0] is zero.
     * Runs in O(n) time and uses O(n) auxiliary space.
     */
    public static int[] build(String text, int[] suffixArray) {
        if (text == null || suffixArray == null) {
            throw new IllegalArgumentException("Text and suffix array are required.");
        }
        int n = text.length();
        if (suffixArray.length != n) {
            throw new IllegalArgumentException("Suffix-array length must equal text length.");
        }
        int[] rank = new int[n];
        for (int order = 0; order < n; order++) {
            int start = suffixArray[order];
            if (start < 0 || start >= n) {
                throw new IllegalArgumentException("Suffix position is outside the text.");
            }
            rank[start] = order;
        }

        int[] lcp = new int[n];
        int common = 0;
        for (int start = 0; start < n; start++) {
            int order = rank[start];
            if (order == 0) {
                common = 0;
                continue;
            }
            int previousStart = suffixArray[order - 1];
            while (start + common < n && previousStart + common < n
                    && text.charAt(start + common) == text.charAt(previousStart + common)) {
                common++;
            }
            lcp[order] = common;
            if (common > 0) common--;
        }
        return lcp;
    }
}

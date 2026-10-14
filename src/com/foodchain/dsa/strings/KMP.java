package com.foodchain.dsa.strings;

import java.util.ArrayList;
import java.util.List;

/** KMP search for product text and quality-report terms: O(n + m) time, O(m) space. */
public final class KMP {
    private KMP() { }

    /** Returns every starting index of pattern in text. An empty pattern has no matches. */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<Integer>();
        if (text == null || pattern == null || pattern.length() == 0 || pattern.length() > text.length()) {
            return matches;
        }

        int[] prefixLength = buildPrefixLength(pattern);
        int textIndex = 0;
        int patternIndex = 0;
        while (textIndex < text.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                textIndex++;
                patternIndex++;
                if (patternIndex == pattern.length()) {
                    matches.add(textIndex - patternIndex);
                    patternIndex = prefixLength[patternIndex - 1];
                }
            } else if (patternIndex > 0) {
                patternIndex = prefixLength[patternIndex - 1];
            } else {
                textIndex++;
            }
        }
        return matches;
    }

    private static int[] buildPrefixLength(String pattern) {
        int[] prefixLength = new int[pattern.length()];
        int matched = 0;
        for (int i = 1; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(matched)) {
                prefixLength[i++] = ++matched;
            } else if (matched > 0) {
                matched = prefixLength[matched - 1];
            } else {
                prefixLength[i++] = 0;
            }
        }
        return prefixLength;
    }
}

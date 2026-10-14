package com.foodchain.dsa.strings;

import java.util.ArrayList;
import java.util.List;

/** Z-array pattern matching for finding repeated phrases across product descriptions. */
public final class ZFunction {
    private ZFunction() { }

    /** Returns Z[i], the common-prefix length of value and value starting at i. */
    public static int[] calculate(String value) {
        int[] characters = new int[value.length()];
        for (int i = 0; i < value.length(); i++) characters[i] = value.charAt(i);
        return calculate(characters);
    }

    /** Finds pattern in text with a -1 separator that cannot equal any Java char. */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<Integer>();
        if (text == null || pattern == null || pattern.length() == 0 || pattern.length() > text.length()) {
            return matches;
        }
        int patternLength = pattern.length();
        int[] combined = new int[patternLength + 1 + text.length()];
        for (int i = 0; i < patternLength; i++) combined[i] = pattern.charAt(i);
        combined[patternLength] = -1;
        for (int i = 0; i < text.length(); i++) combined[patternLength + 1 + i] = text.charAt(i);

        int[] z = calculate(combined);
        for (int i = patternLength + 1; i < combined.length; i++) {
            if (z[i] >= patternLength) matches.add(i - patternLength - 1);
        }
        return matches;
    }

    private static int[] calculate(int[] value) {
        int[] z = new int[value.length];
        int left = 0;
        int right = 0;
        for (int i = 1; i < value.length; i++) {
            if (i <= right) z[i] = Math.min(right - i + 1, z[i - left]);
            while (i + z[i] < value.length && value[z[i]] == value[i + z[i]]) z[i]++;
            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }
        return z;
    }
}

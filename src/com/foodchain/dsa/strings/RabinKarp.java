package com.foodchain.dsa.strings;

import java.util.ArrayList;
import java.util.List;

/** Rolling-hash search for product IDs and shipment tracking codes. */
public final class RabinKarp {
    private static final long BASE = 256L;
    private static final long MODULUS = 1_000_000_007L;

    private RabinKarp() { }

    /** Returns every starting index; matching characters are checked after a hash match. */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<Integer>();
        if (text == null || pattern == null || pattern.length() == 0 || pattern.length() > text.length()) {
            return matches;
        }

        int patternLength = pattern.length();
        long highestPower = 1L;
        for (int i = 1; i < patternLength; i++) {
            highestPower = (highestPower * BASE) % MODULUS;
        }

        long patternHash = 0L;
        long windowHash = 0L;
        for (int i = 0; i < patternLength; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MODULUS;
            windowHash = (windowHash * BASE + text.charAt(i)) % MODULUS;
        }

        for (int start = 0; start <= text.length() - patternLength; start++) {
            if (patternHash == windowHash && matchesAt(text, pattern, start)) {
                matches.add(start);
            }
            if (start < text.length() - patternLength) {
                long withoutLeading = (windowHash - text.charAt(start) * highestPower) % MODULUS;
                if (withoutLeading < 0) withoutLeading += MODULUS;
                windowHash = (withoutLeading * BASE + text.charAt(start + patternLength)) % MODULUS;
            }
        }
        return matches;
    }

    private static boolean matchesAt(String text, String pattern, int start) {
        for (int i = 0; i < pattern.length(); i++) {
            if (text.charAt(start + i) != pattern.charAt(i)) return false;
        }
        return true;
    }
}

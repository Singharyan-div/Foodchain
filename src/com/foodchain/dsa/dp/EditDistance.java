package com.foodchain.dsa.dp;

/** Dynamic-programming edit distances for correcting catalogue names. */
public final class EditDistance {
    private EditDistance() { }

    public static int levenshtein(String first, String second) {
        if (first == null || second == null) throw new IllegalArgumentException("Names cannot be null.");
        int[][] dp = new int[first.length() + 1][second.length() + 1];
        for (int i = 0; i <= first.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= second.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                int replace = dp[i - 1][j - 1]
                        + (Character.toLowerCase(first.charAt(i - 1))
                                == Character.toLowerCase(second.charAt(j - 1)) ? 0 : 1);
                dp[i][j] = Math.min(replace,
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
            }
        }
        return dp[first.length()][second.length()];
    }

    /** Damerau-Levenshtein variant allowing one adjacent transposition. */
    public static int damerauLevenshtein(String first, String second) {
        if (first == null || second == null) throw new IllegalArgumentException("Names cannot be null.");
        int[][] dp = new int[first.length() + 1][second.length() + 1];
        for (int i = 0; i <= first.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= second.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                int replace = dp[i - 1][j - 1]
                        + (Character.toLowerCase(first.charAt(i - 1))
                                == Character.toLowerCase(second.charAt(j - 1)) ? 0 : 1);
                dp[i][j] = Math.min(replace,
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                if (i > 1 && j > 1
                        && Character.toLowerCase(first.charAt(i - 1))
                                == Character.toLowerCase(second.charAt(j - 2))
                        && Character.toLowerCase(first.charAt(i - 2))
                                == Character.toLowerCase(second.charAt(j - 1))) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }
        return dp[first.length()][second.length()];
    }
}

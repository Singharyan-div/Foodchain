package com.foodchain.dsa.randomized;

/** Deterministic Miller-Rabin primality test for positive signed 64-bit integers. */
public final class MillerRabin {
    private static final long[] BASES = { 2L, 325L, 9375L, 28178L,
            450775L, 9780504L, 1795265022L };
    private static final int[] SMALL_PRIMES = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37 };

    private MillerRabin() { }

    public static boolean isPrime(long number) {
        if (number < 2) return false;
        for (int prime : SMALL_PRIMES) {
            if (number == prime) return true;
            if (number % prime == 0) return false;
        }
        long oddPart = number - 1;
        int powersOfTwo = 0;
        while ((oddPart & 1L) == 0L) {
            oddPart >>>= 1;
            powersOfTwo++;
        }
        for (long base : BASES) {
            long reducedBase = base % number;
            if (reducedBase == 0) continue;
            long value = powMod(reducedBase, oddPart, number);
            if (value == 1 || value == number - 1) continue;
            boolean reachedMinusOne = false;
            for (int round = 1; round < powersOfTwo; round++) {
                value = multiplyMod(value, value, number);
                if (value == number - 1) {
                    reachedMinusOne = true;
                    break;
                }
                if (value == 1) return false;
            }
            if (!reachedMinusOne) return false;
        }
        return true;
    }

    private static long powMod(long base, long exponent, long modulus) {
        long result = 1L;
        while (exponent > 0) {
            if ((exponent & 1L) != 0) result = multiplyMod(result, base, modulus);
            base = multiplyMod(base, base, modulus);
            exponent >>>= 1;
        }
        return result;
    }

    private static long multiplyMod(long first, long second, long modulus) {
        long result = 0L;
        long addend = first;
        long multiplier = second;
        while (multiplier > 0) {
            if ((multiplier & 1L) != 0) result = addMod(result, addend, modulus);
            addend = addMod(addend, addend, modulus);
            multiplier >>>= 1;
        }
        return result;
    }

    private static long addMod(long first, long second, long modulus) {
        return first >= modulus - second ? first - (modulus - second) : first + second;
    }
}

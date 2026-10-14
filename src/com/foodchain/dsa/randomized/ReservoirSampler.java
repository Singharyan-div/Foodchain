package com.foodchain.dsa.randomized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Incremental reservoir sampling: uniform k-item sample using O(k) memory. */
public final class ReservoirSampler<T> {
    private final int capacity;
    private final Random random;
    private final List<T> reservoir;
    private long seen;

    public ReservoirSampler(int capacity, Random random) {
        if (capacity < 0 || random == null) {
            throw new IllegalArgumentException("Capacity must be non-negative and Random is required.");
        }
        this.capacity = capacity;
        this.random = random;
        this.reservoir = new ArrayList<T>(Math.min(capacity, 1024));
    }

    public void accept(T item) {
        if (seen == Long.MAX_VALUE) throw new IllegalStateException("Stream is too long to count.");
        seen++;
        if (reservoir.size() < capacity) {
            reservoir.add(item);
        } else if (capacity > 0) {
            long slot = nextLong(seen);
            if (slot < capacity) reservoir.set((int) slot, item);
        }
    }

    private long nextLong(long bound) {
        long bits;
        long value;
        do {
            bits = random.nextLong() >>> 1;
            value = bits % bound;
        } while (bits - value + (bound - 1) < 0L);
        return value;
    }

    public long getSeenCount() { return seen; }
    public List<T> getSample() { return Collections.unmodifiableList(new ArrayList<T>(reservoir)); }
}

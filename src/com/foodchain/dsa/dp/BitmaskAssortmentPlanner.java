package com.foodchain.dsa.dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.foodchain.model.Product;

/** Finds the largest distinct-SKU assortment that fits a rupee budget. */
public final class BitmaskAssortmentPlanner {
    private static final int MAX_PRODUCTS = 20;

    private BitmaskAssortmentPlanner() { }

    /** O(2^n) subset DP, suitable for a small candidate assortment (n <= 20). */
    public static Result plan(List<Product> products, double budgetRupees) {
        if (products == null) throw new IllegalArgumentException("Product list is required.");
        if (Double.isNaN(budgetRupees) || Double.isInfinite(budgetRupees) || budgetRupees < 0) {
            throw new IllegalArgumentException("Budget must be a non-negative amount.");
        }
        if (products.size() > MAX_PRODUCTS) {
            throw new IllegalArgumentException("Bitmask planning supports at most 20 candidate products.");
        }
        long budgetPaise = Math.round(budgetRupees * 100.0);
        int states = 1 << products.size();
        long[] cost = new long[states];
        boolean[] feasible = new boolean[states];
        feasible[0] = true;
        int bestMask = 0;
        int bestCount = 0;
        long bestCost = 0;
        long[] prices = new long[products.size()];
        for (int i = 0; i < products.size(); i++) {
            double price = products.get(i).getPricePerUnit();
            if (price < 0 || Double.isNaN(price) || Double.isInfinite(price)) {
                throw new IllegalArgumentException("Product prices must be finite and non-negative.");
            }
            prices[i] = Math.round(price * 100.0);
        }
        for (int mask = 1; mask < states; mask++) {
            int bit = Integer.numberOfTrailingZeros(mask);
            int previous = mask & (mask - 1);
            feasible[mask] = feasible[previous] && prices[bit] <= budgetPaise - cost[previous];
            if (feasible[mask]) {
                cost[mask] = cost[previous] + prices[bit];
            }
            int count = Integer.bitCount(mask);
            if (feasible[mask]
                    && (count > bestCount || (count == bestCount && cost[mask] > bestCost))) {
                bestMask = mask;
                bestCount = count;
                bestCost = cost[mask];
            }
        }
        List<Product> selected = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            if ((bestMask & (1 << i)) != 0) selected.add(products.get(i));
        }
        return new Result(selected, bestCost / 100.0, budgetRupees - bestCost / 100.0);
    }

    public static final class Result {
        private final List<Product> selectedProducts;
        private final double totalCost;
        private final double remainingBudget;

        private Result(List<Product> selectedProducts, double totalCost, double remainingBudget) {
            this.selectedProducts = Collections.unmodifiableList(selectedProducts);
            this.totalCost = totalCost;
            this.remainingBudget = remainingBudget;
        }

        public List<Product> getSelectedProducts() { return selectedProducts; }
        public double getTotalCost() { return totalCost; }
        public double getRemainingBudget() { return remainingBudget; }
    }
}

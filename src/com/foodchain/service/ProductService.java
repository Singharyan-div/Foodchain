package com.foodchain.service;

import com.foodchain.model.Product;

public final class ProductService {
    private final FoodChainData data;

    public ProductService(FoodChainData data) { this.data = data; }

    public void add(Product product) {
        if (data.findProduct(product.getProductId()) != null) {
            throw new IllegalArgumentException("Product ID already exists.");
        }
        if (data.findSupplier(product.getSupplierId()) == null) {
            throw new IllegalArgumentException("Supplier ID was not found.");
        }
        if (product.getPricePerUnit() <= 0 || product.getShelfLifeDays() <= 0) {
            throw new IllegalArgumentException("Price and shelf life must be positive.");
        }
        data.addProduct(product);
    }

    public void updatePrice(String productId, double newPrice) {
        Product product = data.findProduct(productId);
        if (product == null) throw new IllegalArgumentException("Product ID was not found.");
        if (newPrice <= 0) throw new IllegalArgumentException("Price must be positive.");
        product.setPricePerUnit(newPrice);
    }
}

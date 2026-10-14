package com.foodchain.model;

public class Inventory {
    private final String inventoryId;
    private String productId;
    private String warehouseId;
    private int quantity;
    private String lastUpdated;

    public Inventory(String inventoryId, String productId, String warehouseId,
            int quantity, String lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    public String getInventoryId() { return inventoryId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public void receive(int amount, String updateDate) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Received quantity must be positive.");
        }
        if (amount > Integer.MAX_VALUE - quantity) {
            throw new IllegalArgumentException("Received quantity exceeds the supported inventory range.");
        }
        quantity += amount;
        lastUpdated = updateDate;
    }

    public void dispatch(int amount, String updateDate) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Dispatch quantity must be positive.");
        }
        if (amount > quantity) {
            throw new IllegalStateException("Not enough stock. Available quantity: " + quantity);
        }
        quantity -= amount;
        lastUpdated = updateDate;
    }
}

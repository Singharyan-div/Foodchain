package com.foodchain.service;

import java.time.LocalDate;

import com.foodchain.model.Inventory;
import com.foodchain.model.Warehouse;

public final class InventoryService {
    private final FoodChainData data;

    public InventoryService(FoodChainData data) { this.data = data; }

    public void receive(String productId, String warehouseId, int quantity) {
        if (data.findProduct(productId) == null) throw new IllegalArgumentException("Product ID was not found.");
        Warehouse warehouse = data.findWarehouse(warehouseId);
        if (warehouse == null) throw new IllegalArgumentException("Warehouse ID was not found.");
        if (quantity <= 0) throw new IllegalArgumentException("Received quantity must be positive.");

        long storedUnits = 0;
        for (Inventory item : data.getInventories()) {
            if (item.getWarehouseId().equalsIgnoreCase(warehouseId)) {
                storedUnits += item.getQuantity();
            }
        }
        if (storedUnits + quantity > warehouse.getCapacityUnits()) {
            throw new IllegalStateException("Warehouse capacity exceeded. Free units: "
                    + Math.max(0, warehouse.getCapacityUnits() - storedUnits));
        }

        Inventory inventory = data.findInventory(productId, warehouseId);
        if (inventory == null) {
            data.addInventory(new Inventory(data.nextInventoryId(), productId, warehouseId,
                    quantity, LocalDate.now().toString()));
        } else {
            inventory.receive(quantity, LocalDate.now().toString());
        }
    }

    public void dispatch(String productId, String warehouseId, int quantity) {
        Inventory inventory = data.findInventory(productId, warehouseId);
        if (inventory == null) throw new IllegalStateException("No inventory record exists at this warehouse.");
        inventory.dispatch(quantity, LocalDate.now().toString());
    }

    public int available(String productId, String warehouseId) {
        Inventory inventory = data.findInventory(productId, warehouseId);
        return inventory == null ? 0 : inventory.getQuantity();
    }
}

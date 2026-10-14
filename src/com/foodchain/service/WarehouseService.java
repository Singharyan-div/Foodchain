package com.foodchain.service;

import com.foodchain.model.DistributionCenter;
import com.foodchain.model.Warehouse;

public final class WarehouseService {
    private final FoodChainData data;

    public WarehouseService(FoodChainData data) { this.data = data; }

    public void addWarehouse(Warehouse warehouse) {
        if (data.findWarehouse(warehouse.getWarehouseId()) != null) {
            throw new IllegalArgumentException("Warehouse ID already exists.");
        }
        if (warehouse.getCapacityUnits() <= 0) {
            throw new IllegalArgumentException("Warehouse capacity must be positive.");
        }
        data.addWarehouse(warehouse);
    }

    public void addDistributionCenter(DistributionCenter center) {
        if (data.findDistributionCenter(center.getCenterId()) != null) {
            throw new IllegalArgumentException("Distribution center ID already exists.");
        }
        if (data.findWarehouse(center.getWarehouseId()) == null) {
            throw new IllegalArgumentException("Linked warehouse ID was not found.");
        }
        if (center.getCapacityUnits() <= 0) {
            throw new IllegalArgumentException("Distribution center capacity must be positive.");
        }
        data.addDistributionCenter(center);
    }
}

package com.foodchain.service;

import com.foodchain.model.Farmer;
import com.foodchain.model.Supplier;

public final class SupplierService {
    private final FoodChainData data;

    public SupplierService(FoodChainData data) { this.data = data; }

    public void addSupplier(Supplier supplier) {
        if (data.findSupplier(supplier.getSupplierId()) != null) {
            throw new IllegalArgumentException("Supplier ID already exists.");
        }
        data.addSupplier(supplier);
    }

    public void addFarmer(Farmer farmer) {
        if (data.findFarmer(farmer.getFarmerId()) != null) {
            throw new IllegalArgumentException("Farmer ID already exists.");
        }
        if (data.findSupplier(farmer.getSupplierId()) == null) {
            throw new IllegalArgumentException("Supplier ID was not found.");
        }
        data.addFarmer(farmer);
    }

    public void updateCertification(String supplierId, boolean certified) {
        Supplier supplier = data.findSupplier(supplierId);
        if (supplier == null) throw new IllegalArgumentException("Supplier ID was not found.");
        supplier.setQualityCertified(certified);
    }
}

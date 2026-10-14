package com.foodchain.model;

public class DistributionCenter {
    private final String centerId;
    private String name;
    private String location;
    private String warehouseId;
    private int capacityUnits;

    public DistributionCenter(String centerId, String name, String location,
            String warehouseId, int capacityUnits) {
        this.centerId = centerId;
        this.name = name;
        this.location = location;
        this.warehouseId = warehouseId;
        this.capacityUnits = capacityUnits;
    }

    public String getCenterId() { return centerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public int getCapacityUnits() { return capacityUnits; }
    public void setCapacityUnits(int capacityUnits) { this.capacityUnits = capacityUnits; }
}

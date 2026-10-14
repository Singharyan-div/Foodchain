package com.foodchain.model;

public class Warehouse {
    private final String warehouseId;
    private String name;
    private String location;
    private int capacityUnits;

    public Warehouse(String warehouseId, String name, String location, int capacityUnits) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.capacityUnits = capacityUnits;
    }

    public String getWarehouseId() { return warehouseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getCapacityUnits() { return capacityUnits; }
    public void setCapacityUnits(int capacityUnits) { this.capacityUnits = capacityUnits; }
}

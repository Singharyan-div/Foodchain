package com.foodchain.model;

public class Farmer {
    private final String farmerId;
    private String name;
    private String supplierId;
    private String location;

    public Farmer(String farmerId, String name, String supplierId, String location) {
        this.farmerId = farmerId;
        this.name = name;
        this.supplierId = supplierId;
        this.location = location;
    }

    public String getFarmerId() { return farmerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}

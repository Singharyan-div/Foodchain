package com.foodchain.model;

public class Supplier {
    private final String supplierId;
    private String name;
    private String region;
    private boolean qualityCertified;

    public Supplier(String supplierId, String name, String region, boolean qualityCertified) {
        this.supplierId = supplierId;
        this.name = name;
        this.region = region;
        this.qualityCertified = qualityCertified;
    }

    public String getSupplierId() { return supplierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public boolean isQualityCertified() { return qualityCertified; }
    public void setQualityCertified(boolean qualityCertified) { this.qualityCertified = qualityCertified; }
}

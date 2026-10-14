package com.foodchain.model;

public class Product {
    private final String productId;
    private String name;
    private String category;
    private String supplierId;
    private String description;
    private double pricePerUnit;
    private int shelfLifeDays;
    private boolean refrigerated;

    public Product(String productId, String name, String category, String supplierId,
            String description, double pricePerUnit, int shelfLifeDays, boolean refrigerated) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.supplierId = supplierId;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
        this.shelfLifeDays = shelfLifeDays;
        this.refrigerated = refrigerated;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public int getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(int shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }
    public boolean isRefrigerated() { return refrigerated; }
    public void setRefrigerated(boolean refrigerated) { this.refrigerated = refrigerated; }
}

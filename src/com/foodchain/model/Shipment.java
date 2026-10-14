package com.foodchain.model;

public class Shipment {
    private final String trackingCode;
    private String orderId;
    private String warehouseId;
    private String distributionCenterId;
    private int quantity;
    private String status;

    public Shipment(String trackingCode, String orderId, String warehouseId,
            String distributionCenterId, int quantity, String status) {
        this.trackingCode = trackingCode;
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.distributionCenterId = distributionCenterId;
        this.quantity = quantity;
        this.status = status;
    }

    public String getTrackingCode() { return trackingCode; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getDistributionCenterId() { return distributionCenterId; }
    public void setDistributionCenterId(String distributionCenterId) { this.distributionCenterId = distributionCenterId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

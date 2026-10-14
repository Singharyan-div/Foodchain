package com.foodchain.model;

public class Order {
    private final String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private String status;
    private String orderDate;

    public Order(String orderId, String customerId, String productId, int quantity,
            String status, String orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
        this.orderDate = orderDate;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
}

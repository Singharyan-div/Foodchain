package com.foodchain.service;

import java.time.LocalDate;

import com.foodchain.model.Order;

public final class OrderService {
    private final FoodChainData data;

    public OrderService(FoodChainData data) { this.data = data; }

    public Order placeOrder(String customerId, String productId, int quantity) {
        if (data.findCustomer(customerId) == null) throw new IllegalArgumentException("Customer ID was not found.");
        if (data.findProduct(productId) == null) throw new IllegalArgumentException("Product ID was not found.");
        if (quantity <= 0) throw new IllegalArgumentException("Order quantity must be positive.");
        Order order = new Order(data.nextOrderId(), customerId, productId, quantity,
                "Pending", LocalDate.now().toString());
        data.addOrder(order);
        return order;
    }

    public void updateStatus(String orderId, String newStatus) {
        Order order = data.findOrder(orderId);
        if (order == null) throw new IllegalArgumentException("Order ID was not found.");
        if (!"Pending".equals(order.getStatus()) && !"Processing".equals(order.getStatus())) {
            throw new IllegalStateException("Only pending or processing orders can be changed here.");
        }
        if (!"Processing".equals(newStatus) && !"Cancelled".equals(newStatus)) {
            throw new IllegalArgumentException("Choose Processing or Cancelled.");
        }
        order.setStatus(newStatus);
    }
}

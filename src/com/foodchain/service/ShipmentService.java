package com.foodchain.service;

import com.foodchain.model.DistributionCenter;
import com.foodchain.model.Order;
import com.foodchain.model.Shipment;

public final class ShipmentService {
    private final FoodChainData data;
    private final InventoryService inventoryService;

    public ShipmentService(FoodChainData data, InventoryService inventoryService) {
        this.data = data;
        this.inventoryService = inventoryService;
    }

    public Shipment createShipment(String orderId, String warehouseId, String centerId) {
        Order order = data.findOrder(orderId);
        if (order == null) throw new IllegalArgumentException("Order ID was not found.");
        if ("Cancelled".equals(order.getStatus()) || "Shipped".equals(order.getStatus())
                || "Delivered".equals(order.getStatus())) {
            throw new IllegalStateException("This order cannot be shipped in its current status.");
        }
        if (data.findWarehouse(warehouseId) == null) throw new IllegalArgumentException("Warehouse ID was not found.");
        DistributionCenter center = data.findDistributionCenter(centerId);
        if (center == null) throw new IllegalArgumentException("Distribution center ID was not found.");
        if (!center.getWarehouseId().equalsIgnoreCase(warehouseId)) {
            throw new IllegalArgumentException("Selected center is not assigned to that warehouse.");
        }

        inventoryService.dispatch(order.getProductId(), warehouseId, order.getQuantity());
        Shipment shipment = new Shipment(data.nextTrackingCode(), orderId, warehouseId,
                centerId, order.getQuantity(), "Dispatched");
        data.addShipment(shipment);
        order.setStatus("Shipped");
        return shipment;
    }

    public void updateStatus(String trackingCode, String newStatus) {
        Shipment shipment = data.findShipment(trackingCode);
        if (shipment == null) throw new IllegalArgumentException("Tracking code was not found.");
        if (!"In Transit".equals(newStatus) && !"Delivered".equals(newStatus)) {
            throw new IllegalArgumentException("Choose In Transit or Delivered.");
        }
        if ("Delivered".equals(shipment.getStatus())) {
            throw new IllegalStateException("A delivered shipment cannot move back to an earlier status.");
        }
        shipment.setStatus(newStatus);
        Order order = data.findOrder(shipment.getOrderId());
        if (order != null) order.setStatus("Delivered".equals(newStatus) ? "Delivered" : "In Transit");
    }
}

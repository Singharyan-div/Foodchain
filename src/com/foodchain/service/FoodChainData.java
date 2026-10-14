package com.foodchain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.foodchain.model.Customer;
import com.foodchain.model.DistributionCenter;
import com.foodchain.model.Farmer;
import com.foodchain.model.Inventory;
import com.foodchain.model.Order;
import com.foodchain.model.Product;
import com.foodchain.model.QualityReport;
import com.foodchain.model.Shipment;
import com.foodchain.model.Supplier;
import com.foodchain.model.Warehouse;

/** Shared in-memory collections and ID lookups for the application services. */
public final class FoodChainData {
    private final List<Product> products = new ArrayList<Product>();
    private final List<Supplier> suppliers = new ArrayList<Supplier>();
    private final List<Farmer> farmers = new ArrayList<Farmer>();
    private final List<Warehouse> warehouses = new ArrayList<Warehouse>();
    private final List<DistributionCenter> distributionCenters = new ArrayList<DistributionCenter>();
    private final List<Customer> customers = new ArrayList<Customer>();
    private final List<Inventory> inventories = new ArrayList<Inventory>();
    private final List<Order> orders = new ArrayList<Order>();
    private final List<Shipment> shipments = new ArrayList<Shipment>();
    private final List<QualityReport> qualityReports = new ArrayList<QualityReport>();
    private int inventorySequence = 5;
    private int orderSequence = 2101;
    private int shipmentSequence = 8841;

    public FoodChainData() {
        seedData();
    }

    private void seedData() {
        suppliers.add(new Supplier("SUP-01", "Green Valley Farms", "Pune", true));
        suppliers.add(new Supplier("SUP-02", "Harvest Cooperative", "Nashik", true));
        suppliers.add(new Supplier("SUP-03", "Orchard Fresh", "Shimla", false));

        farmers.add(new Farmer("F-001", "Anil Deshmukh", "SUP-01", "Pune District"));
        farmers.add(new Farmer("F-002", "Meera Patil", "SUP-01", "Satara"));
        farmers.add(new Farmer("F-003", "Ramesh Pawar", "SUP-02", "Nashik"));

        products.add(new Product("P-101", "Organic Tomatoes", "Vegetables", "SUP-01",
                "Fresh produce: red tomatoes from local farms", 125.00, 7, false));
        products.add(new Product("P-102", "Basmati Rice", "Grains", "SUP-02",
                "Aromatic long-grain rice", 220.00, 365, false));
        products.add(new Product("P-103", "Fresh Spinach", "Vegetables", "SUP-01",
                "Fresh produce: leafy greens; keep refrigerated", 90.00, 5, true));
        products.add(new Product("P-104", "Himalayan Apples", "Fruits", "SUP-03",
                "Crisp apples from hill orchards", 185.00, 30, false));

        warehouses.add(new Warehouse("WH-01", "Central Produce Hub", "Mumbai", 500));
        warehouses.add(new Warehouse("WH-02", "North Distribution Store", "Delhi", 420));
        distributionCenters.add(new DistributionCenter("DC-01", "Mumbai Market Center",
                "Mumbai", "WH-01", 300));
        distributionCenters.add(new DistributionCenter("DC-02", "Delhi Market Center",
                "Delhi", "WH-02", 250));

        customers.add(new Customer("CUS-01", "Mumbai Wholesale Market", "Mumbai Market", "Mumbai"));
        customers.add(new Customer("CUS-02", "Delhi Retail Market", "Delhi Market", "Delhi"));
        customers.add(new Customer("CUS-03", "Pune Fresh Market", "Pune Market", "Pune"));

        String today = LocalDate.now().toString();
        inventories.add(new Inventory("INV-0001", "P-101", "WH-01", 120, today));
        inventories.add(new Inventory("INV-0002", "P-102", "WH-02", 240, today));
        inventories.add(new Inventory("INV-0003", "P-103", "WH-01", 90, today));
        inventories.add(new Inventory("INV-0004", "P-104", "WH-02", 75, today));

        qualityReports.add(new QualityReport("QR-01", "P-101", today,
                "Freshness and visual inspection passed.", true));
        qualityReports.add(new QualityReport("QR-02", "P-103", today,
                "Cold-storage temperature log verified.", true));
        qualityReports.add(new QualityReport("QR-03", "P-104", today,
                "Mold detected on one sample; isolate the batch for follow-up inspection.", false));
    }

    public List<Product> getProducts() { return Collections.unmodifiableList(products); }
    public List<Supplier> getSuppliers() { return Collections.unmodifiableList(suppliers); }
    public List<Farmer> getFarmers() { return Collections.unmodifiableList(farmers); }
    public List<Warehouse> getWarehouses() { return Collections.unmodifiableList(warehouses); }
    public List<DistributionCenter> getDistributionCenters() { return Collections.unmodifiableList(distributionCenters); }
    public List<Customer> getCustomers() { return Collections.unmodifiableList(customers); }
    public List<Inventory> getInventories() { return Collections.unmodifiableList(inventories); }
    public List<Order> getOrders() { return Collections.unmodifiableList(orders); }
    public List<Shipment> getShipments() { return Collections.unmodifiableList(shipments); }
    public List<QualityReport> getQualityReports() { return Collections.unmodifiableList(qualityReports); }

    public void addProduct(Product item) { products.add(item); }
    public void addSupplier(Supplier item) { suppliers.add(item); }
    public void addFarmer(Farmer item) { farmers.add(item); }
    public void addWarehouse(Warehouse item) { warehouses.add(item); }
    public void addDistributionCenter(DistributionCenter item) { distributionCenters.add(item); }
    public void addInventory(Inventory item) { inventories.add(item); }
    public void addOrder(Order item) { orders.add(item); }
    public void addShipment(Shipment item) { shipments.add(item); }

    public Product findProduct(String id) {
        for (Product item : products) if (item.getProductId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public Supplier findSupplier(String id) {
        for (Supplier item : suppliers) if (item.getSupplierId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public Farmer findFarmer(String id) {
        for (Farmer item : farmers) if (item.getFarmerId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public Warehouse findWarehouse(String id) {
        for (Warehouse item : warehouses) if (item.getWarehouseId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public DistributionCenter findDistributionCenter(String id) {
        for (DistributionCenter item : distributionCenters) if (item.getCenterId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public Customer findCustomer(String id) {
        for (Customer item : customers) if (item.getCustomerId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public Order findOrder(String id) {
        for (Order item : orders) if (item.getOrderId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public Shipment findShipment(String trackingCode) {
        for (Shipment item : shipments) if (item.getTrackingCode().equalsIgnoreCase(trackingCode)) return item;
        return null;
    }

    public Inventory findInventory(String productId, String warehouseId) {
        for (Inventory item : inventories) {
            if (item.getProductId().equalsIgnoreCase(productId)
                    && item.getWarehouseId().equalsIgnoreCase(warehouseId)) return item;
        }
        return null;
    }

    public String nextInventoryId() { return String.format("INV-%04d", inventorySequence++); }
    public String nextOrderId() { return String.format("ORD-%04d", orderSequence++); }
    public String nextTrackingCode() { return String.format("TRK-%04d", shipmentSequence++); }
}

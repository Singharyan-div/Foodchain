package com.foodchain.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.foodchain.dsa.graph.BipartiteMatching;
import com.foodchain.dsa.graph.FlowNetwork;
import com.foodchain.model.Customer;
import com.foodchain.model.DistributionCenter;
import com.foodchain.model.Inventory;
import com.foodchain.model.Product;
import com.foodchain.model.Supplier;
import com.foodchain.model.Warehouse;

/** Projects live FoodChain suppliers, stock, warehouses, centers, and markets into a flow graph. */
public final class DistributionNetworkService {
    private final FoodChainData data;

    public DistributionNetworkService(FoodChainData data) { this.data = data; }

    public FlowNetwork.Result analyze(FlowNetwork.Algorithm algorithm) {
        NetworkSnapshot snapshot = createSnapshot();
        return snapshot.network.solve(snapshot.source, snapshot.sink, algorithm);
    }

    public List<Assignment> assignSuppliersToWarehouses() {
        List<Supplier> suppliers = data.getSuppliers();
        List<Warehouse> warehouses = data.getWarehouses();
        List<int[]> pairs = new ArrayList<int[]>();
        for (int left = 0; left < suppliers.size(); left++) {
            for (int right = 0; right < warehouses.size(); right++) {
                long eligibleStock = stockFromSupplierAtWarehouse(
                        suppliers.get(left).getSupplierId(), warehouses.get(right).getWarehouseId());
                if (eligibleStock > 0) pairs.add(new int[] { left, right });
            }
        }
        BipartiteMatching.Result result = BipartiteMatching.solve(
                suppliers.size(), warehouses.size(), pairs);
        int[] assignments = result.getRightByLeft();
        List<Assignment> matches = new ArrayList<Assignment>();
        for (int i = 0; i < assignments.length; i++) {
            if (assignments[i] >= 0) {
                matches.add(new Assignment(suppliers.get(i), warehouses.get(assignments[i])));
            }
        }
        return matches;
    }

    private long stockFromSupplierAtWarehouse(String supplierId, String warehouseId) {
        long total = 0;
        for (Inventory inventory : data.getInventories()) {
            if (!warehouseId.equalsIgnoreCase(inventory.getWarehouseId())) continue;
            Product product = data.findProduct(inventory.getProductId());
            if (product != null && supplierId.equalsIgnoreCase(product.getSupplierId())) {
                total += Math.max(0, inventory.getQuantity());
            }
        }
        return total;
    }

    private NetworkSnapshot createSnapshot() {
        List<String> nodeNames = new ArrayList<String>();
        Map<String, Integer> nodeIds = new LinkedHashMap<String, Integer>();
        int source = addNode("SOURCE", "Supply source", nodeNames, nodeIds);
        int sink = addNode("SINK", "Market sink", nodeNames, nodeIds);
        for (Supplier supplier : data.getSuppliers()) {
            addNode("SUP:" + key(supplier.getSupplierId()), "Supplier " + supplier.getName(), nodeNames, nodeIds);
        }
        for (Warehouse warehouse : data.getWarehouses()) {
            addNode("WH:" + key(warehouse.getWarehouseId()), "Warehouse " + warehouse.getName(), nodeNames, nodeIds);
        }
        for (DistributionCenter center : data.getDistributionCenters()) {
            addNode("DC:" + key(center.getCenterId()), "Center " + center.getName(), nodeNames, nodeIds);
        }
        Map<String, String> marketLabels = new LinkedHashMap<String, String>();
        for (Customer customer : data.getCustomers()) {
            String locationKey = key(customer.getLocation());
            if (!marketLabels.containsKey(locationKey)) marketLabels.put(locationKey, customer.getMarket());
        }
        for (Map.Entry<String, String> market : marketLabels.entrySet()) {
            addNode("MARKET:" + market.getKey(), "Market " + market.getValue(), nodeNames, nodeIds);
        }

        FlowNetwork network = new FlowNetwork(nodeNames.size());
        Map<String, Long> supplierStock = new LinkedHashMap<String, Long>();
        Map<String, Long> supplierWarehouseStock = new LinkedHashMap<String, Long>();
        for (Supplier supplier : data.getSuppliers()) supplierStock.put(key(supplier.getSupplierId()), 0L);
        for (Inventory inventory : data.getInventories()) {
            if (inventory.getQuantity() <= 0) continue;
            Product product = data.findProduct(inventory.getProductId());
            if (product == null) continue;
            String supplier = key(product.getSupplierId());
            String warehouse = key(inventory.getWarehouseId());
            long quantity = inventory.getQuantity();
            supplierStock.put(supplier, supplierStock.containsKey(supplier)
                    ? supplierStock.get(supplier) + quantity : quantity);
            String relation = supplier + "\u0000" + warehouse;
            supplierWarehouseStock.put(relation, supplierWarehouseStock.containsKey(relation)
                    ? supplierWarehouseStock.get(relation) + quantity : quantity);
        }
        for (Supplier supplier : data.getSuppliers()) {
            String supplierKey = key(supplier.getSupplierId());
            long capacity = supplierStock.containsKey(supplierKey) ? supplierStock.get(supplierKey) : 0;
            if (capacity > 0) network.addEdge(source,
                    nodeIds.get("SUP:" + supplierKey), capacity, "Source -> " + supplier.getName());
            for (Warehouse warehouse : data.getWarehouses()) {
                String warehouseKey = key(warehouse.getWarehouseId());
                String relation = supplierKey + "\u0000" + warehouseKey;
                Long stock = supplierWarehouseStock.get(relation);
                if (stock != null && stock.longValue() > 0) {
                    network.addEdge(nodeIds.get("SUP:" + supplierKey),
                            nodeIds.get("WH:" + warehouseKey), stock.longValue(),
                            supplier.getName() + " -> " + warehouse.getName());
                }
            }
        }

        for (Warehouse warehouse : data.getWarehouses()) {
            for (DistributionCenter center : data.getDistributionCenters()) {
                if (!warehouse.getWarehouseId().equalsIgnoreCase(center.getWarehouseId())) continue;
                long capacity = Math.min(warehouse.getCapacityUnits(), center.getCapacityUnits());
                if (capacity > 0) network.addEdge(nodeIds.get("WH:" + key(warehouse.getWarehouseId())),
                        nodeIds.get("DC:" + key(center.getCenterId())), capacity,
                        warehouse.getName() + " -> " + center.getName());
            }
        }

        Map<String, Long> marketCapacity = new LinkedHashMap<String, Long>();
        for (DistributionCenter center : data.getDistributionCenters()) {
            String locationKey = key(center.getLocation());
            if (!marketLabels.containsKey(locationKey)) continue;
            long capacity = Math.max(0, center.getCapacityUnits());
            Integer marketNode = nodeIds.get("MARKET:" + locationKey);
            if (capacity > 0 && marketNode != null) {
                network.addEdge(nodeIds.get("DC:" + key(center.getCenterId())), marketNode.intValue(),
                        capacity, center.getName() + " -> " + marketLabels.get(locationKey));
                marketCapacity.put(locationKey, marketCapacity.containsKey(locationKey)
                        ? marketCapacity.get(locationKey) + capacity : capacity);
            }
        }
        for (Map.Entry<String, Long> market : marketCapacity.entrySet()) {
            network.addEdge(nodeIds.get("MARKET:" + market.getKey()), sink,
                    market.getValue(), marketLabels.get(market.getKey()) + " -> Market sink");
        }
        return new NetworkSnapshot(network, source, sink);
    }

    private static int addNode(String key, String label, List<String> labels, Map<String, Integer> ids) {
        int id = labels.size();
        labels.add(label);
        ids.put(key, id);
        return id;
    }

    private static String key(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private static final class NetworkSnapshot {
        private final FlowNetwork network;
        private final int source;
        private final int sink;

        private NetworkSnapshot(FlowNetwork network, int source, int sink) {
            this.network = network;
            this.source = source;
            this.sink = sink;
        }
    }

    public static final class Assignment {
        private final Supplier supplier;
        private final Warehouse warehouse;

        private Assignment(Supplier supplier, Warehouse warehouse) {
            this.supplier = supplier;
            this.warehouse = warehouse;
        }

        public Supplier getSupplier() { return supplier; }
        public Warehouse getWarehouse() { return warehouse; }
    }
}

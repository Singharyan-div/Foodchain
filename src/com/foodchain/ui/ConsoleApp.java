package com.foodchain.ui;

import java.util.Scanner;

import com.foodchain.dsa.dp.BitmaskAssortmentPlanner;
import com.foodchain.dsa.dp.EditDistance;
import com.foodchain.dsa.dp.MatrixChainOptimizer;
import com.foodchain.dsa.dp.OptimalBinarySearchTree;
import com.foodchain.dsa.graph.FlowNetwork;
import com.foodchain.dsa.np.ThreeSatCliqueReduction;
import com.foodchain.dsa.np.ThreeSatSolver;
import com.foodchain.dsa.np.VertexCoverApproximation;
import com.foodchain.dsa.randomized.MillerRabin;
import com.foodchain.dsa.randomized.RandomizedQuickSort;
import com.foodchain.dsa.randomized.ReservoirSampler;

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
import com.foodchain.parallel.BlellochScan;
import com.foodchain.parallel.BrentTheorem;
import com.foodchain.parallel.ParallelReduce;
import com.foodchain.service.FoodChainData;
import com.foodchain.service.InventoryService;
import com.foodchain.service.DocumentSimilarityService;
import com.foodchain.service.DistributionNetworkService;
import com.foodchain.service.OrderService;
import com.foodchain.service.ProductService;
import com.foodchain.service.ShipmentService;
import com.foodchain.service.StringSearchService;
import com.foodchain.service.SupplierService;
import com.foodchain.service.WarehouseService;

/** Console menus for the collection-backed FoodChain management modules. */
public final class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final FoodChainData data = new FoodChainData();
    private final ProductService productService = new ProductService(data);
    private final SupplierService supplierService = new SupplierService(data);
    private final WarehouseService warehouseService = new WarehouseService(data);
    private final InventoryService inventoryService = new InventoryService(data);
    private final OrderService orderService = new OrderService(data);
    private final ShipmentService shipmentService = new ShipmentService(data, inventoryService);
    private final StringSearchService stringSearchService = new StringSearchService(data);
    private final DocumentSimilarityService documentSimilarityService =
            new DocumentSimilarityService(data);
    private final DistributionNetworkService distributionNetworkService =
            new DistributionNetworkService(data);

    public void run() {
        System.out.println("========================================");
        System.out.println("             FOODCHAIN SYSTEM");
        System.out.println(" Food Supply & Distribution Management");
        System.out.println("========================================");
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = readInt("Select an option: ");
                switch (choice) {
                    case 1: productMenu(); break;
                    case 2: supplierMenu(); break;
                    case 3: warehouseMenu(); break;
                    case 4: inventoryMenu(); break;
                    case 5: orderMenu(); break;
                    case 6: shipmentMenu(); break;
                    case 7: stringSearchMenu(); break;
                    case 8: documentSimilarityMenu(); break;
                    case 9: dynamicProgrammingMenu(); break;
                    case 10: distributionNetworkMenu(); break;
                    case 11: constraintAnalysisMenu(); break;
                    case 12: inventoryAnalyticsMenu(); break;
                    case 13: running = false; break;
                    default: System.out.println("Please enter a number from 1 to 13.");
                }
            }
        } catch (InputClosedException exception) {
            System.out.println("\nConsole input closed. Exiting FoodChain.");
        } finally {
            scanner.close();
        }
        System.out.println("FoodChain closed.");
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1. Product Management");
        System.out.println("2. Supplier Management");
        System.out.println("3. Warehouse Management");
        System.out.println("4. Inventory Management");
        System.out.println("5. Order Management");
        System.out.println("6. Shipment Management");
        System.out.println("7. String Search");
        System.out.println("8. Document Similarity");
        System.out.println("9. DP Optimization");
        System.out.println("10. Distribution Network");
        System.out.println("11. Constraint Analysis");
        System.out.println("12. Inventory Analytics");
        System.out.println("13. Exit");
    }

    private void productMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nPRODUCT MANAGEMENT");
            System.out.println("1. List products");
            System.out.println("2. Add product");
            System.out.println("3. Change product price");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listProducts(); break;
                case 2: addProduct(); break;
                case 3: changeProductPrice(); break;
                case 0: back = true; break;
                default: System.out.println("Choose 0, 1, 2, or 3.");
            }
        }
    }

    private void listProducts() {
        System.out.println("\nPRODUCT CATALOGUE");
        for (Product product : data.getProducts()) {
            Supplier supplier = data.findSupplier(product.getSupplierId());
            String supplierName = supplier == null ? "Unknown supplier" : supplier.getName();
            System.out.printf("%s | %s | %s | INR %.2f/unit | shelf life %d days | refrigerated %s | supplier %s%n",
                    product.getProductId(), product.getName(), product.getCategory(), product.getPricePerUnit(),
                    product.getShelfLifeDays(), product.isRefrigerated() ? "yes" : "no", supplierName);
        }
    }

    private void addProduct() {
        System.out.println("\nADD PRODUCT");
        String id = readRequired("Product ID: ");
        String name = readRequired("Product name: ");
        String category = readRequired("Category: ");
        String supplierId = readRequired("Supplier ID: ");
        String description = readRequired("Description: ");
        double price = readPositiveDouble("Price per unit (INR): ");
        int shelfLife = readPositiveInt("Shelf life in days: ");
        boolean refrigerated = readYesNo("Requires refrigeration? (y/n): ");
        try {
            productService.add(new Product(id, name, category, supplierId, description,
                    price, shelfLife, refrigerated));
            System.out.println("Product added.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception);
        }
    }

    private void changeProductPrice() {
        String id = readRequired("Product ID: ");
        double price = readPositiveDouble("New price per unit (INR): ");
        try {
            productService.updatePrice(id, price);
            System.out.println("Product price updated.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception);
        }
    }

    private void supplierMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nSUPPLIER MANAGEMENT");
            System.out.println("1. List suppliers and farmers");
            System.out.println("2. Add supplier");
            System.out.println("3. Add farmer under a supplier");
            System.out.println("4. Update supplier quality certification");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listSuppliersAndFarmers(); break;
                case 2: addSupplier(); break;
                case 3: addFarmer(); break;
                case 4: updateSupplierCertification(); break;
                case 0: back = true; break;
                default: System.out.println("Choose 0, 1, 2, 3, or 4.");
            }
        }
    }

    private void listSuppliersAndFarmers() {
        System.out.println("\nSUPPLIERS");
        for (Supplier supplier : data.getSuppliers()) {
            System.out.println(supplier.getSupplierId() + " | " + supplier.getName() + " | "
                    + supplier.getRegion() + " | quality certified: " + yesNo(supplier.isQualityCertified()));
        }
        System.out.println("FARMERS");
        for (Farmer farmer : data.getFarmers()) {
            System.out.println(farmer.getFarmerId() + " | " + farmer.getName() + " | supplier "
                    + farmer.getSupplierId() + " | " + farmer.getLocation());
        }
    }

    private void addSupplier() {
        String id = readRequired("Supplier ID: ");
        String name = readRequired("Supplier name: ");
        String region = readRequired("Region: ");
        boolean certified = readYesNo("Quality certified? (y/n): ");
        try {
            supplierService.addSupplier(new Supplier(id, name, region, certified));
            System.out.println("Supplier added.");
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void addFarmer() {
        String id = readRequired("Farmer ID: ");
        String name = readRequired("Farmer name: ");
        String supplierId = readRequired("Supplier ID: ");
        String location = readRequired("Farm location: ");
        try {
            supplierService.addFarmer(new Farmer(id, name, supplierId, location));
            System.out.println("Farmer added.");
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void updateSupplierCertification() {
        String id = readRequired("Supplier ID: ");
        boolean certified = readYesNo("Set quality certification to valid? (y/n): ");
        try {
            supplierService.updateCertification(id, certified);
            System.out.println("Supplier certification updated.");
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void warehouseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nWAREHOUSE MANAGEMENT");
            System.out.println("1. List warehouses and distribution centers");
            System.out.println("2. Add warehouse");
            System.out.println("3. Add distribution center");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listWarehousesAndCenters(); break;
                case 2: addWarehouse(); break;
                case 3: addDistributionCenter(); break;
                case 0: back = true; break;
                default: System.out.println("Choose 0, 1, 2, or 3.");
            }
        }
    }

    private void listWarehousesAndCenters() {
        System.out.println("\nWAREHOUSES");
        for (Warehouse warehouse : data.getWarehouses()) {
            System.out.println(warehouse.getWarehouseId() + " | " + warehouse.getName() + " | "
                    + warehouse.getLocation() + " | capacity " + warehouse.getCapacityUnits());
        }
        System.out.println("DISTRIBUTION CENTERS");
        for (DistributionCenter center : data.getDistributionCenters()) {
            System.out.println(center.getCenterId() + " | " + center.getName() + " | "
                    + center.getLocation() + " | linked warehouse " + center.getWarehouseId()
                    + " | capacity " + center.getCapacityUnits());
        }
    }

    private void addWarehouse() {
        String id = readRequired("Warehouse ID: ");
        String name = readRequired("Warehouse name: ");
        String location = readRequired("Location: ");
        int capacity = readPositiveInt("Capacity in units: ");
        try {
            warehouseService.addWarehouse(new Warehouse(id, name, location, capacity));
            System.out.println("Warehouse added.");
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void addDistributionCenter() {
        String id = readRequired("Center ID: ");
        String name = readRequired("Center name: ");
        String location = readRequired("Location: ");
        String warehouseId = readRequired("Linked warehouse ID: ");
        int capacity = readPositiveInt("Center capacity in units: ");
        try {
            warehouseService.addDistributionCenter(new DistributionCenter(id, name, location,
                    warehouseId, capacity));
            System.out.println("Distribution center added.");
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void inventoryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nINVENTORY MANAGEMENT");
            System.out.println("1. View inventory by warehouse");
            System.out.println("2. Receive stock");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listInventory(); break;
                case 2: receiveInventory(); break;
                case 0: back = true; break;
                default: System.out.println("Choose 0, 1, or 2.");
            }
        }
    }

    private void listInventory() {
        System.out.println("\nINVENTORY");
        for (Inventory inventory : data.getInventories()) {
            Product product = data.findProduct(inventory.getProductId());
            Warehouse warehouse = data.findWarehouse(inventory.getWarehouseId());
            String productName = product == null ? inventory.getProductId() : product.getName();
            String warehouseName = warehouse == null ? inventory.getWarehouseId() : warehouse.getName();
            System.out.println(inventory.getInventoryId() + " | " + productName + " | " + warehouseName
                    + " | quantity " + inventory.getQuantity() + " | updated " + inventory.getLastUpdated());
        }
    }

    private void receiveInventory() {
        String productId = readRequired("Product ID: ");
        String warehouseId = readRequired("Warehouse ID: ");
        int quantity = readPositiveInt("Quantity received: ");
        try {
            inventoryService.receive(productId, warehouseId, quantity);
            System.out.println("Stock received. Current quantity: "
                    + inventoryService.available(productId, warehouseId));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception);
        }
    }

    private void orderMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nORDER MANAGEMENT");
            System.out.println("1. List orders");
            System.out.println("2. Place order");
            System.out.println("3. Mark order Processing or Cancelled");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listOrders(); break;
                case 2: placeOrder(); break;
                case 3: updateOrderStatus(); break;
                case 0: back = true; break;
                default: System.out.println("Choose 0, 1, 2, or 3.");
            }
        }
    }

    private void listOrders() {
        System.out.println("\nCUSTOMER ORDERS");
        if (data.getOrders().isEmpty()) System.out.println("No orders have been placed this session.");
        for (Order order : data.getOrders()) {
            Customer customer = data.findCustomer(order.getCustomerId());
            Product product = data.findProduct(order.getProductId());
            String customerName = customer == null ? order.getCustomerId() : customer.getName();
            String productName = product == null ? order.getProductId() : product.getName();
            System.out.println(order.getOrderId() + " | " + customerName + " | " + productName
                    + " | quantity " + order.getQuantity() + " | " + order.getStatus()
                    + " | " + order.getOrderDate());
        }
    }

    private void placeOrder() {
        listCustomers();
        String customerId = readRequired("Customer ID: ");
        listProducts();
        String productId = readRequired("Product ID: ");
        int quantity = readPositiveInt("Quantity: ");
        try {
            Order order = orderService.placeOrder(customerId, productId, quantity);
            System.out.println("Order placed: " + order.getOrderId() + " (Pending).");
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void listCustomers() {
        System.out.println("CUSTOMERS");
        for (Customer customer : data.getCustomers()) {
            System.out.println(customer.getCustomerId() + " | " + customer.getName() + " | "
                    + customer.getMarket() + " | " + customer.getLocation());
        }
    }

    private void updateOrderStatus() {
        String orderId = readRequired("Order ID: ");
        System.out.println("1. Processing  2. Cancelled");
        int choice = readInt("New status: ");
        String status = choice == 1 ? "Processing" : choice == 2 ? "Cancelled" : "";
        try {
            orderService.updateStatus(orderId, status);
            System.out.println("Order status updated to " + status + ".");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception);
        }
    }

    private void shipmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nSHIPMENT MANAGEMENT");
            System.out.println("1. List shipments");
            System.out.println("2. Create shipment from an order");
            System.out.println("3. Update shipment status");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listShipments(); break;
                case 2: createShipment(); break;
                case 3: updateShipmentStatus(); break;
                case 0: back = true; break;
                default: System.out.println("Choose 0, 1, 2, or 3.");
            }
        }
    }

    private void listShipments() {
        System.out.println("\nSHIPMENTS");
        if (data.getShipments().isEmpty()) System.out.println("No shipments have been created this session.");
        for (Shipment shipment : data.getShipments()) {
            System.out.println(shipment.getTrackingCode() + " | order " + shipment.getOrderId()
                    + " | " + shipment.getQuantity() + " units | warehouse " + shipment.getWarehouseId()
                    + " -> center " + shipment.getDistributionCenterId() + " | " + shipment.getStatus());
        }
    }

    private void createShipment() {
        listOrders();
        String orderId = readRequired("Order ID to ship: ");
        listWarehousesAndCenters();
        String warehouseId = readRequired("Warehouse ID: ");
        String centerId = readRequired("Destination center ID: ");
        try {
            Shipment shipment = shipmentService.createShipment(orderId, warehouseId, centerId);
            System.out.println("Shipment created: " + shipment.getTrackingCode()
                    + ". The ordered stock was deducted from warehouse inventory.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception);
        }
    }

    private void updateShipmentStatus() {
        String trackingCode = readRequired("Tracking code: ");
        System.out.println("1. In Transit  2. Delivered");
        int choice = readInt("New status: ");
        String status = choice == 1 ? "In Transit" : choice == 2 ? "Delivered" : "";
        try {
            shipmentService.updateStatus(trackingCode, status);
            System.out.println("Shipment status updated to " + status + ".");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            showError(exception);
        }
    }

    private void stringSearchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nSTRING SEARCH & QUALITY SCAN");
            System.out.println("1. Search product names/descriptions (KMP)");
            System.out.println("2. Search quality reports (KMP)");
            System.out.println("3. Search product IDs (Rabin-Karp)");
            System.out.println("4. Search shipment tracking codes (Rabin-Karp)");
            System.out.println("5. Scan quality reports for all warning terms (Aho-Corasick)");
            System.out.println("6. Search a phrase in product descriptions (Z-function)");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: searchProducts(); break;
                case 2: searchQualityReports(); break;
                case 3: searchProductIds(); break;
                case 4: searchTrackingCodes(); break;
                case 5: scanQualityWarnings(); break;
                case 6: searchDescriptionPhrase(); break;
                case 0: back = true; break;
                default: System.out.println("Choose a number from 0 to 6.");
            }
        }
    }

    private void searchProducts() {
        String query = readRequired("Product name/description term: ");
        java.util.List<Product> matches = stringSearchService.searchProducts(query);
        if (matches.isEmpty()) {
            System.out.println("No product matched '" + query + "'.");
            return;
        }
        System.out.println("KMP matches (case-insensitive):");
        for (Product product : matches) {
            System.out.println(product.getProductId() + " | " + product.getName()
                    + " | " + product.getDescription());
        }
    }

    private void searchQualityReports() {
        String query = readRequired("Quality-report term: ");
        java.util.List<QualityReport> matches = stringSearchService.searchQualityReports(query);
        if (matches.isEmpty()) {
            System.out.println("No quality report matched '" + query + "'.");
            return;
        }
        System.out.println("KMP report matches (case-insensitive):");
        for (QualityReport report : matches) printQualityReport(report);
    }

    private void searchProductIds() {
        String fragment = readRequired("Product ID fragment (example P-10): ");
        java.util.List<Product> matches = stringSearchService.searchProductIds(fragment);
        if (matches.isEmpty()) {
            System.out.println("No product ID matched '" + fragment + "'.");
            return;
        }
        System.out.println("Rabin-Karp product-ID matches:");
        for (Product product : matches) {
            System.out.println(product.getProductId() + " | " + product.getName());
        }
    }

    private void searchTrackingCodes() {
        String fragment = readRequired("Tracking-code fragment (create a shipment first if needed): ");
        java.util.List<Shipment> matches = stringSearchService.searchTrackingCodes(fragment);
        if (matches.isEmpty()) {
            System.out.println("No shipment tracking code matched '" + fragment + "'.");
            return;
        }
        System.out.println("Rabin-Karp tracking-code matches:");
        for (Shipment shipment : matches) {
            System.out.println(shipment.getTrackingCode() + " | order " + shipment.getOrderId()
                    + " | " + shipment.getStatus());
        }
    }

    private void scanQualityWarnings() {
        java.util.List<StringSearchService.QualityWarning> warnings =
                stringSearchService.scanQualityWarnings();
        if (warnings.isEmpty()) {
            System.out.println("No configured warning terms found in the reports.");
            System.out.println("Terms checked: " + String.join(", ", StringSearchService.getWarningKeywords()));
            return;
        }
        System.out.println("Aho-Corasick warning matches (start positions are zero-based):");
        for (StringSearchService.QualityWarning warning : warnings) {
            QualityReport report = warning.getReport();
            System.out.println(report.getReportId() + " | product " + report.getProductId()
                    + " | " + (report.isPassed() ? "PASSED" : "REVIEW REQUIRED")
                    + " | " + report.getDetails());
            for (com.foodchain.dsa.strings.AhoCorasick.Match match : warning.getMatches()) {
                System.out.println("   keyword '" + match.getKeyword() + "' at character "
                        + match.getStartIndex());
            }
        }
    }

    private void searchDescriptionPhrase() {
        String phrase = readRequired("Description phrase to find: ");
        java.util.List<Product> matches = stringSearchService.searchDescriptionPhrase(phrase);
        if (matches.isEmpty()) {
            System.out.println("No product description contains '" + phrase + "'.");
            return;
        }
        System.out.println("Z-function phrase matches (case-insensitive):");
        for (Product product : matches) {
            System.out.println(product.getProductId() + " | " + product.getName()
                    + " | " + product.getDescription());
        }
    }

    private void printQualityReport(QualityReport report) {
        System.out.println(report.getReportId() + " | product " + report.getProductId()
                + " | " + report.getReportDate() + " | "
                + (report.isPassed() ? "PASSED" : "REVIEW REQUIRED")
                + " | " + report.getDetails());
    }

    private void documentSimilarityMenu() {
        documentSimilarityService.rebuildIndex();
        System.out.println("Indexed " + documentSimilarityService.getIndexedDocumentCount()
                + " product and quality-report documents.");
        boolean back = false;
        while (!back) {
            System.out.println("\nDOCUMENT INDEX & SIMILARITY");
            System.out.println("1. List indexed documents");
            System.out.println("2. Search phrase across documents (Suffix Array)");
            System.out.println("3. Find longest repeated text (Kasai LCP)");
            System.out.println("4. Check phrase existence (Suffix Automaton)");
            System.out.println("5. Rebuild index");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: listIndexedDocuments(); break;
                case 2: searchIndexedDocuments(); break;
                case 3: showLongestRepeatedFragment(); break;
                case 4: checkIndexedPhrase(); break;
                case 5:
                    documentSimilarityService.rebuildIndex();
                    System.out.println("Index rebuilt for "
                            + documentSimilarityService.getIndexedDocumentCount() + " documents.");
                    break;
                case 0: back = true; break;
                default: System.out.println("Choose a number from 0 to 5.");
            }
        }
    }

    private void listIndexedDocuments() {
        System.out.println("INDEXED DOCUMENTS");
        for (DocumentSimilarityService.Document document : documentSimilarityService.getDocuments()) {
            System.out.println(document.getKind() + " | " + document.getTitle()
                    + " | " + document.getText());
        }
    }

    private void searchIndexedDocuments() {
        String phrase = readRequired("Phrase to search: ");
        java.util.List<DocumentSimilarityService.Document> matches =
                documentSimilarityService.searchDocuments(phrase);
        if (matches.isEmpty()) {
            System.out.println("No indexed document contains '" + phrase + "'.");
            return;
        }
        System.out.println("Suffix-array matches (case-insensitive):");
        for (DocumentSimilarityService.Document document : matches) {
            System.out.println(document.getKind() + " | " + document.getTitle()
                    + " | " + document.getText());
        }
    }

    private void showLongestRepeatedFragment() {
        DocumentSimilarityService.RepeatedFragment repeated =
                documentSimilarityService.findLongestRepeatedFragment();
        if (repeated == null) {
            System.out.println("No repeated text fragment was found.");
            return;
        }
        System.out.println("Longest repeated fragment: \"" + repeated.getText() + "\"");
        System.out.println("Appears in: " + repeated.getFirstDocument().getTitle());
        System.out.println("And: " + repeated.getSecondDocument().getTitle());
        System.out.println("LCP length before trimming spaces: " + repeated.getIndexedLength());
    }

    private void checkIndexedPhrase() {
        String phrase = readRequired("Phrase to check: ");
        boolean found = documentSimilarityService.containsPhrase(phrase);
        System.out.println(found ? "Suffix automaton: phrase exists in the index."
                : "Suffix automaton: phrase was not found.");
    }

    private void dynamicProgrammingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nDYNAMIC-PROGRAMMING OPTIMIZATION");
            System.out.println("1. Correct a product/supplier name (Levenshtein)");
            System.out.println("2. Select maximum distinct products under a budget (Bitmask DP)");
            System.out.println("3. Optimize a matrix-processing chain (Matrix-Chain DP)");
            System.out.println("4. Build frequency-optimized product search tree (Optimal BST)");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: suggestNameCorrection(); break;
                case 2: planProductAssortment(); break;
                case 3: optimizeMatrixChain(); break;
                case 4: buildOptimalProductSearchTree(); break;
                case 0: back = true; break;
                default: System.out.println("Choose a number from 0 to 4.");
            }
        }
    }

    private void suggestNameCorrection() {
        String query = readRequired("Possibly misspelled product/supplier name: ");
        java.util.List<NameSuggestion> suggestions = new java.util.ArrayList<NameSuggestion>();
        for (Product product : data.getProducts()) {
            suggestions.add(new NameSuggestion("Product", product.getName(),
                    EditDistance.damerauLevenshtein(query, product.getName())));
        }
        for (Supplier supplier : data.getSuppliers()) {
            suggestions.add(new NameSuggestion("Supplier", supplier.getName(),
                    EditDistance.damerauLevenshtein(query, supplier.getName())));
        }
        java.util.Collections.sort(suggestions, new java.util.Comparator<NameSuggestion>() {
            @Override
            public int compare(NameSuggestion first, NameSuggestion second) {
                return Integer.compare(first.distance, second.distance);
            }
        });
        System.out.println("Closest catalogue names (adjacent transposition is one edit):");
        int count = Math.min(5, suggestions.size());
        for (int i = 0; i < count; i++) {
            NameSuggestion item = suggestions.get(i);
            System.out.println(item.kind + " | " + item.name + " | edit distance " + item.distance);
        }
    }

    private void planProductAssortment() {
        double budget = readPositiveDouble("Budget in INR: ");
        try {
            BitmaskAssortmentPlanner.Result result =
                    BitmaskAssortmentPlanner.plan(data.getProducts(), budget);
            System.out.println("Selected " + result.getSelectedProducts().size()
                    + " distinct product types for INR " + String.format("%.2f", result.getTotalCost()) + ":");
            for (Product product : result.getSelectedProducts()) {
                System.out.println(product.getProductId() + " | " + product.getName()
                        + " | INR " + String.format("%.2f", product.getPricePerUnit()));
            }
            System.out.println("Remaining budget: INR " + String.format("%.2f", result.getRemainingBudget()));
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void optimizeMatrixChain() {
        int matrixCount;
        while (true) {
            matrixCount = readPositiveInt("Number of matrices (maximum 12): ");
            if (matrixCount <= 12) break;
            System.out.println("Use at most 12 matrices for this console demonstration.");
        }
        int[] dimensions = new int[matrixCount + 1];
        System.out.println("Enter dimensions d0..d" + matrixCount
                + "; matrix Ai has size d(i-1) x di.");
        for (int i = 0; i < dimensions.length; i++) {
            dimensions[i] = readPositiveInt("d" + i + ": ");
        }
        try {
            MatrixChainOptimizer.Result result = MatrixChainOptimizer.optimize(dimensions);
            System.out.println("Optimal order: " + result.getParenthesization());
            System.out.println("Scalar multiplications: " + result.getScalarMultiplications());
        } catch (IllegalArgumentException exception) {
            showError(exception);
        }
    }

    private void buildOptimalProductSearchTree() {
        java.util.List<Product> products = new java.util.ArrayList<Product>(data.getProducts());
        java.util.Collections.sort(products, new java.util.Comparator<Product>() {
            @Override
            public int compare(Product first, Product second) {
                return first.getName().compareToIgnoreCase(second.getName());
            }
        });
        java.util.List<String> names = new java.util.ArrayList<String>();
        double[] successful = new double[products.size()];
        double[] unsuccessful = new double[products.size() + 1];
        java.util.Arrays.fill(unsuccessful, 1.0);
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            names.add(product.getName());
            successful[i] = 1.0;
            for (Order order : data.getOrders()) {
                if (product.getProductId().equalsIgnoreCase(order.getProductId())
                        && !"Cancelled".equalsIgnoreCase(order.getStatus())) {
                    successful[i] += order.getQuantity();
                }
            }
        }
        OptimalBinarySearchTree.Result result =
                OptimalBinarySearchTree.build(names, successful, unsuccessful);
        System.out.println("Keys are alphabetically sorted; successful frequencies use placed order units.");
        System.out.println("Optimal BST: " + result.getTree());
        System.out.println("Weighted search cost: " + String.format("%.2f", result.getWeightedSearchCost()));
    }

    private static final class NameSuggestion {
        private final String kind;
        private final String name;
        private final int distance;

        private NameSuggestion(String kind, String name, int distance) {
            this.kind = kind;
            this.name = name;
            this.distance = distance;
        }
    }

    private void distributionNetworkMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nDISTRIBUTION NETWORK");
            System.out.println("1. Max flow with Ford-Fulkerson");
            System.out.println("2. Max flow with Edmonds-Karp");
            System.out.println("3. Max flow with Dinic and show min-cut bottlenecks");
            System.out.println("4. Match suppliers to eligible warehouses");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: showDistributionFlow(FlowNetwork.Algorithm.FORD_FULKERSON); break;
                case 2: showDistributionFlow(FlowNetwork.Algorithm.EDMONDS_KARP); break;
                case 3: showDistributionFlow(FlowNetwork.Algorithm.DINIC); break;
                case 4: showSupplierWarehouseMatching(); break;
                case 0: back = true; break;
                default: System.out.println("Choose a number from 0 to 4.");
            }
        }
    }

    private void showDistributionFlow(FlowNetwork.Algorithm algorithm) {
        try {
            FlowNetwork.Result result = distributionNetworkService.analyze(algorithm);
            System.out.println(algorithm + " maximum deliverable units: " + result.getMaxFlow());
            if (algorithm == FlowNetwork.Algorithm.DINIC) {
                System.out.println("Min-cut bottleneck links:");
                if (result.getMinCutEdges().isEmpty()) System.out.println("No positive-capacity cut edge was identified.");
                for (FlowNetwork.Arc edge : result.getMinCutEdges()) {
                    System.out.println(edge.getLabel() + " | capacity " + edge.getCapacity());
                }
            }
        } catch (ArithmeticException exception) {
            System.out.println("Flow total exceeded the supported numeric range.");
        }
    }

    private void showSupplierWarehouseMatching() {
        java.util.List<DistributionNetworkService.Assignment> assignments =
                distributionNetworkService.assignSuppliersToWarehouses();
        System.out.println("Maximum one-to-one supplier/warehouse assignments: " + assignments.size());
        java.util.Set<String> matchedSuppliers = new java.util.HashSet<String>();
        for (DistributionNetworkService.Assignment assignment : assignments) {
            matchedSuppliers.add(assignment.getSupplier().getSupplierId().toLowerCase());
            System.out.println(assignment.getSupplier().getName() + " -> "
                    + assignment.getWarehouse().getName());
        }
        for (Supplier supplier : data.getSuppliers()) {
            if (!matchedSuppliers.contains(supplier.getSupplierId().toLowerCase())) {
                System.out.println(supplier.getName() + " -> no eligible warehouse assignment");
            }
        }
    }

    private void constraintAnalysisMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nSUPPLY CONSTRAINT ANALYSIS");
            System.out.println("1. Solve sample 3-SAT supply rules");
            System.out.println("2. Reduce 3-SAT to CLIQUE to INDEPENDENT-SET to VERTEX-COVER");
            System.out.println("3. Find critical suppliers with Vertex-Cover 2-approximation");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: solveSupplyConstraints(); break;
                case 2: showReductionChain(); break;
                case 3: approximateSupplierConflictCover(); break;
                case 0: back = true; break;
                default: System.out.println("Choose a number from 0 to 3.");
            }
        }
    }

    private java.util.List<ThreeSatSolver.Clause> sampleSupplyFormula() {
        java.util.List<ThreeSatSolver.Clause> clauses = new java.util.ArrayList<ThreeSatSolver.Clause>();
        // x1 certified supplier, x2 cold-storage slot, x3 transport route, x4 quality approval.
        clauses.add(new ThreeSatSolver.Clause(1, 2, 3));
        clauses.add(new ThreeSatSolver.Clause(-1, 2, 4));
        clauses.add(new ThreeSatSolver.Clause(1, -2, 3));
        clauses.add(new ThreeSatSolver.Clause(-3, 4, 2));
        return clauses;
    }

    private void solveSupplyConstraints() {
        java.util.List<ThreeSatSolver.Clause> clauses = sampleSupplyFormula();
        System.out.println("Variables: x1=certified supplier, x2=cold storage, x3=transport route, x4=quality approval");
        for (ThreeSatSolver.Clause clause : clauses) System.out.println(clause);
        ThreeSatSolver.Result result = ThreeSatSolver.solve(4, clauses);
        if (!result.isSatisfiable()) {
            System.out.println("No assignment satisfies the supply constraints.");
            return;
        }
        System.out.println("Satisfiable assignment:");
        String[] names = { "", "certified supplier", "cold storage", "transport route", "quality approval" };
        for (int variable = 1; variable <= 4; variable++) {
            System.out.println("  " + names[variable] + " = " + result.valueOf(variable));
        }
    }

    private void showReductionChain() {
        ThreeSatCliqueReduction.Result result =
                ThreeSatCliqueReduction.reduce(4, sampleSupplyFormula());
        System.out.println("3-SAT -> CLIQUE: create one vertex per literal; connect compatible literals from different clauses.");
        System.out.println("CLIQUE graph: " + result.getVertices().size() + " vertices, "
                + result.getEdges().size() + " edges; target clique size k=" + result.getTargetSize());
        int[] clique = result.getClique();
        if (clique == null) {
            System.out.println("No size-k clique was found, so the formula is unsatisfiable.");
            return;
        }
        System.out.println("Witness clique: " + labelsAt(result.getVertices(), clique));
        System.out.println("Complement graph: the same witness is an independent set of size "
                + result.getComplementIndependentSet().size() + ".");
        System.out.println("VERTEX-COVER in the complement is all remaining vertices: size "
                + result.getComplementVertexCover().size() + " of " + result.getVertices().size() + ".");
    }

    private String labelsAt(java.util.List<String> labels, int[] indices) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) result.append(", ");
            result.append(labels.get(indices[i]));
        }
        return result.toString();
    }

    private void approximateSupplierConflictCover() {
        java.util.List<Supplier> suppliers = data.getSuppliers();
        java.util.List<String> labels = new java.util.ArrayList<String>();
        for (Supplier supplier : suppliers) labels.add(supplier.getName());
        java.util.List<int[]> conflicts = new java.util.ArrayList<int[]>();
        for (int first = 0; first < suppliers.size(); first++) {
            for (int second = first + 1; second < suppliers.size(); second++) {
                boolean shareWarehouse = false;
                for (Warehouse warehouse : data.getWarehouses()) {
                    long firstStock = stockForSupplierAtWarehouse(
                            suppliers.get(first).getSupplierId(), warehouse.getWarehouseId());
                    long secondStock = stockForSupplierAtWarehouse(
                            suppliers.get(second).getSupplierId(), warehouse.getWarehouseId());
                    if (firstStock > 0 && secondStock > 0) shareWarehouse = true;
                }
                if (shareWarehouse) conflicts.add(new int[] { first, second });
            }
        }
        VertexCoverApproximation.Result result =
                VertexCoverApproximation.approximate(labels, conflicts);
        System.out.println("Shared-warehouse supplier conflicts: " + result.getEdgeCount());
        System.out.println("2-approximate critical supplier set (size "
                + result.getSelectedVertices().size() + "): " + result.getSelectedVertices());
        if (result.getEdgeCount() == 0) System.out.println("No supplier pair currently shares stocked warehouse capacity.");
    }

    private long stockForSupplierAtWarehouse(String supplierId, String warehouseId) {
        long quantity = 0;
        for (Inventory inventory : data.getInventories()) {
            if (!warehouseId.equalsIgnoreCase(inventory.getWarehouseId())) continue;
            Product product = data.findProduct(inventory.getProductId());
            if (product != null && supplierId.equalsIgnoreCase(product.getSupplierId())) {
                quantity += Math.max(0, inventory.getQuantity());
            }
        }
        return quantity;
    }

    private void inventoryAnalyticsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nRANDOMIZED & PARALLEL INVENTORY ANALYTICS");
            System.out.println("1. Rank products by order demand (Randomized QuickSort)");
            System.out.println("2. Sample inventory stream (Reservoir Sampling)");
            System.out.println("3. Cumulative stock totals (Blelloch Scan)");
            System.out.println("4. Parallel inventory statistics (Fork-Join Reduce)");
            System.out.println("5. Check large numeric batch/lot value (Miller-Rabin)");
            System.out.println("6. Estimate parallel processing time (Brent's theorem)");
            System.out.println("0. Back");
            switch (readInt("Choose: ")) {
                case 1: rankProductsByDemand(); break;
                case 2: sampleInventoryStream(); break;
                case 3: showCumulativeInventory(); break;
                case 4: showParallelInventoryStatistics(); break;
                case 5: checkLargeNumericValue(); break;
                case 6: showBrentEstimate(); break;
                case 0: back = true; break;
                default: System.out.println("Choose a number from 0 to 6.");
            }
        }
    }

    private void rankProductsByDemand() {
        java.util.Map<String, Long> demand = new java.util.HashMap<String, Long>();
        for (Product product : data.getProducts()) demand.put(product.getProductId(), 0L);
        for (Order order : data.getOrders()) {
            if ("Cancelled".equalsIgnoreCase(order.getStatus())) continue;
            String productId = order.getProductId();
            if (demand.containsKey(productId)) {
                demand.put(productId, demand.get(productId) + order.getQuantity());
            }
        }
        DemandRow[] rows = new DemandRow[data.getProducts().size()];
        boolean hasDemand = false;
        int index = 0;
        for (Product product : data.getProducts()) {
            long units = demand.get(product.getProductId());
            if (units > 0) hasDemand = true;
            rows[index++] = new DemandRow(product, units);
        }
        RandomizedQuickSort.sort(rows, new java.util.Comparator<DemandRow>() {
            @Override
            public int compare(DemandRow first, DemandRow second) {
                int byDemand = Long.compare(second.units, first.units);
                return byDemand != 0 ? byDemand : first.product.getName().compareToIgnoreCase(second.product.getName());
            }
        }, new java.util.Random());
        if (!hasDemand) System.out.println("No non-cancelled orders yet; all demand totals are currently zero.");
        for (int rank = 0; rank < rows.length; rank++) {
            System.out.println((rank + 1) + ". " + rows[rank].product.getProductId()
                    + " | " + rows[rank].product.getName() + " | ordered units " + rows[rank].units);
        }
    }

    private void sampleInventoryStream() {
        if (data.getInventories().isEmpty()) {
            System.out.println("There are no inventory records to sample.");
            return;
        }
        int requested = readPositiveInt("Reservoir sample size k: ");
        ReservoirSampler<Inventory> sampler =
                new ReservoirSampler<Inventory>(requested, new java.util.Random());
        for (Inventory inventory : data.getInventories()) sampler.accept(inventory);
        System.out.println("Uniform sample of " + sampler.getSample().size() + " from "
                + sampler.getSeenCount() + " current inventory records:");
        for (Inventory inventory : sampler.getSample()) {
            Product product = data.findProduct(inventory.getProductId());
            Warehouse warehouse = data.findWarehouse(inventory.getWarehouseId());
            System.out.println((product == null ? inventory.getProductId() : product.getName())
                    + " | " + (warehouse == null ? inventory.getWarehouseId() : warehouse.getName())
                    + " | " + inventory.getQuantity() + " units");
        }
    }

    private void showCumulativeInventory() {
        System.out.println("Inclusive cumulative inventory totals, grouped by warehouse:");
        for (Warehouse warehouse : data.getWarehouses()) {
            java.util.List<Inventory> records = new java.util.ArrayList<Inventory>();
            for (Inventory inventory : data.getInventories()) {
                if (warehouse.getWarehouseId().equalsIgnoreCase(inventory.getWarehouseId())) records.add(inventory);
            }
            long[] quantities = new long[records.size()];
            for (int i = 0; i < records.size(); i++) quantities[i] = records.get(i).getQuantity();
            try {
                long[] totals = BlellochScan.inclusive(quantities);
                System.out.println(warehouse.getName() + ":");
                for (int i = 0; i < records.size(); i++) {
                    Product product = data.findProduct(records.get(i).getProductId());
                    System.out.println("  through " + (product == null ? records.get(i).getProductId() : product.getName())
                            + " -> " + totals[i] + " units");
                }
            } catch (ArithmeticException exception) {
                System.out.println(warehouse.getName() + ": cumulative stock exceeds the numeric range.");
            }
        }
    }

    private void showParallelInventoryStatistics() {
        long[] quantities = new long[data.getInventories().size()];
        for (int i = 0; i < quantities.length; i++) quantities[i] = data.getInventories().get(i).getQuantity();
        try {
            ParallelReduce.Statistics statistics = ParallelReduce.analyze(quantities);
            System.out.println("Inventory records reduced: " + statistics.getCount());
            System.out.println("Total stock: " + statistics.getTotal() + " units");
            System.out.println("Maximum row quantity: " + statistics.getMaximum() + " units");
            System.out.println("Average row quantity: " + String.format("%.2f", statistics.getAverage()) + " units");
        } catch (ArithmeticException exception) {
            System.out.println("Inventory total exceeded the supported numeric range.");
        }
    }

    private void checkLargeNumericValue() {
        long value = readLong("Non-negative batch/lot number to check: ");
        System.out.println(value + (MillerRabin.isPrime(value)
                ? " is prime under deterministic 64-bit Miller-Rabin."
                : " is not prime."));
    }

    private void showBrentEstimate() {
        int records = data.getInventories().size();
        long work = records * 10L;
        long depth = records == 0 ? 0 : 10L + ceilLog2(records);
        int processors = Math.max(1, Runtime.getRuntime().availableProcessors());
        BrentTheorem.Estimate estimate = BrentTheorem.estimate(work, depth, processors);
        System.out.println("Inventory records: " + records + "; modeled operations: " + work
                + "; processors: " + processors);
        System.out.println("Brent work/depth lower bound: " + estimate.getLowerBound());
        System.out.println("Brent upper estimate ceil(T1/P) + T-infinity: "
                + estimate.getBrentUpperBound());
    }

    private long ceilLog2(int value) {
        long power = 1;
        long levels = 0;
        while (power < value) {
            power *= 2;
            levels++;
        }
        return levels;
    }

    private long readLong(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Long.parseLong(input.trim());
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid 64-bit whole number.");
            }
        }
    }

    private static final class DemandRow {
        private final Product product;
        private final long units;
        private DemandRow(Product product, long units) { this.product = product; this.units = units; }
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException exception) {
                System.out.println("Enter a whole number.");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Enter a number greater than zero.");
        }
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                double value = Double.parseDouble(input.trim());
                if (!Double.isNaN(value) && !Double.isInfinite(value) && value > 0) return value;
            } catch (NumberFormatException exception) {
                // Show the same input hint as for a non-positive value.
            }
            System.out.println("Enter a valid amount greater than zero.");
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if ("y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)) return true;
            if ("n".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value)) return false;
            System.out.println("Enter y or n.");
        }
    }

    private String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if (!value.isEmpty()) return value;
            System.out.println("This value cannot be empty.");
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) throw new InputClosedException();
        return scanner.nextLine();
    }

    private String yesNo(boolean value) { return value ? "yes" : "no"; }

    private void showError(RuntimeException exception) {
        System.out.println("Could not complete operation: " + exception.getMessage());
    }

    private static final class InputClosedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}

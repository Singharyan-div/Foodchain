# FoodChain — execution and presentation guide

This guide follows the project from startup through the main user actions. The line references point to `src/App.jsx`, `src/main.jsx` and `src/algorithms.js` as they are now.

## 1. Start it

1. Open a terminal in `web/frontend`.
2. Run `npm install` once, then `npm run dev`.
3. Open the localhost URL that Vite prints. Keep that terminal open.
4. The first visit shows the sample FoodChain records. Changes are saved in this browser automatically.

The web dashboard is separate from Eclipse. The Java console remains in the project root's `src` folder.

## 2. Startup, line by line

### `src/main.jsx`

- **Line 1** imports React.
- **Line 2** imports React's browser renderer, `createRoot`.
- **Line 3** loads the shared visual styles.
- **Line 4** imports the top-level FoodChain component.
- **Line 6** finds the `<div id="root">` in `index.html`, creates the React root there and renders `<App />`. `StrictMode` helps reveal some development-time React issues.

The browser does not start in an algorithm screen. It starts the application shell, and the active page is initially `overview` in `App.jsx`.

### `src/App.jsx`: static definitions and sample data

- **Lines 6–18** define the left navigation: Overview, Farmers, Suppliers, Products, Warehouses, Inventory, Orders, Shipments, Quality Reports, Distribution and the DSA module map. The `group` field controls where each link appears in the sidebar.
- **Lines 20–71** contain the starter data: partner records, food catalog, warehouse capacity, stock, orders, shipments, quality reports, inventory events and an empty algorithm-run history. IDs connect records across modules; for example, inventory rows refer to product IDs and warehouse IDs.
- **Lines 73–80** define each editable module's fields and table columns. The shared record page uses these definitions so the same add/edit form works for farmers, suppliers, products, warehouses and quality reports.
- **Lines 81–83** hold display helpers, including rupee formatting and the current date.

### Loading and saving local data

- **Lines 86–94 — `initialData()`** first tries to read the browser's `localStorage` entry named `foodchain.modules.v1`. If it exists and parses, that saved data takes precedence. If it is missing or invalid, the app clones the sample data.
- **Lines 96–108** contain CSV parsing helpers. The parser understands quoted cells and escaped quotes so commas inside a record do not split that cell.
- **Line 113 onward — `App()`** creates the application and its React state.
- **Lines 114–129** keep the current data, selected page, search text, open modal, notices, algorithm result and module inputs in state.
- **Line 131** watches the data state. Whenever `db` changes, it serializes all module buckets back to `localStorage`. That is why edits survive a page refresh in the same browser and origin.
- **Line 132** clears temporary success/error notices after a short delay.

## 3. The important execution paths

### A. Any algorithm button

1. A module button calls a handler, for example `productSearch`, `runQualityScan`, `runInventoryAnalytics` or `planDistribution`.
2. Those handlers call **`recordRun` at lines 135–142** with a module name, algorithm ID, input data and a plain-language reason for using it.
3. `recordRun` calls `runAlgorithm(id, input)` from `src/algorithms.js`. That function's `switch` near **line 569** selects the implementation for the ID.
4. The implementation returns a headline, calculated result, step trace and complexity.
5. `recordRun` saves the run in `db.algorithmRuns` (up to the latest 100), sets it as the current result and returns the calculation to the calling module.
6. Near the end of `App.jsx`, the result drawer shows the algorithm name, why the module uses it, the result, trace and complexity.

This means the normal screen remains a business module with its data. The algorithm explanation appears after the user uses that module action.

### B. Product search and demand ranking

- **Lines 183–192 — `productSearch()`** combines product names, descriptions and IDs into searchable catalog text. It runs KMP, Z-Function and Rabin–Karp on the entered term. If there is no match, Levenshtein distance checks which product name is the closest spelling and can show a suggestion.
- In the **Products** table, **`RecordsPage` at lines 311–331** filters visible rows and offers Add/Edit/Delete, smart search and demand ranking.
- Demand ranking runs Randomized QuickSort on the catalog's demand scores. Its sorted values are mapped back to product rows and displayed in that order.
- The product page's related actions run SA-IS and Kasai on actual catalog descriptions, or use Suffix Tree and Suffix Automaton to check sample phrases in those descriptions.
- The catalog data-work action demonstrates Matrix-Chain DP on a small processing-dimension example, then runs Optimal BST with product names sorted as keys and demand scores as successful-search frequencies.

### C. Quality report scan

- The **Quality Reports** module renders the report table and a scan button on each row.
- **Lines 195–197 — `runQualityScan(report)`** sends that report's batch code and inspection notes plus several warning words to Aho–Corasick.
- The result tells you which safety terms occurred in that report. The table keeps the report's status and quality score as the business data.

### D. Inventory intake and analytics

- **`InventoryPage` at lines 333–344** shows stock by warehouse, available quantity after reservations, reorder alerts and recent stock events.
- Clicking **Record intake** increases the chosen warehouse's inventory quantity, increases the catalog stock, creates a `Stock received` event and then saves through React state.
- **Lines 199–204 — `runInventoryAnalytics()`** calculates available quantities from the current inventory rows. Blelloch Scan returns cumulative totals; Parallel Reduce summarizes chunks; Reservoir Sampling takes a small sample of the inventory event stream.
- The additional inventory actions show Miller–Rabin on a large-number example and use Brent's theorem to estimate work from the current inventory size. These two are teaching demonstrations, not production checks on batch IDs or real worker threads.

### E. Order creation: the main end-to-end transaction

1. Click **Create order** from Overview or Orders. The shared modal is implemented by `RecordModal` near **lines 388–400**.
2. The user enters a customer, product, quantity, warehouse and destination.
3. The modal's submit handler calls `createOrder`.
4. **Lines 238–246 — `createOrder(form)`** finds the selected product and matching warehouse inventory row. It refuses the order if the available quantity (`quantity - reserved`) is too small.
5. If stock is sufficient, it creates an order ID, calculates the total and creates a linked shipment ID.
6. One state update adds the order and shipment, subtracts the quantity from inventory and catalog stock, and adds a negative `Order reserved` inventory event.
7. The persistence effect at line 131 writes the new state to browser storage. The Orders, Shipments, Inventory, Overview and CSV export all read the same updated records.

For the assortment planner in **`OrdersPage` at lines 346–354**, check any products you want to consider (or leave all unchecked to use the whole catalog), enter a rupee budget and run Bitmask DP. The algorithm evaluates subsets, maximizes the number of affordable products and breaks ties by using more of the budget.

### F. Shipments and distribution

- **`ShipmentsPage` at lines 356–361** lists shipments linked to orders. Changing a status updates progress and the ETA. A shipment created from an order appears here automatically.
- **`DistributionPage` at lines 363–371** visualizes farm, warehouse and market nodes.
- **Lines 206–219 — `planDistribution()`** constructs the sample route graph. Total supply is derived from available inventory; warehouse-to-hub capacities use free capacity from the warehouse records. It runs Dinic for maximum flow/min-cut and Bipartite Matching for supplier-to-warehouse assignments.
- The related distribution actions run Ford–Fulkerson and Edmonds–Karp on the route model for comparison, plus a small 3-SAT reduction example and Vertex-Cover 2-approximation for route conflicts.
- Some route edges and supplier eligibility rules are intentionally small teaching inputs because this project does not yet store a full road network or formal supplier/warehouse eligibility rules.

### G. Navigation, forms and shared rendering

- **Lines 255–287** derive global search results and render the application shell: fixed sidebar, module links, topbar, CSV controls and the selected page.
- Clicking a sidebar link updates `active`; the conditional rendering around **lines 269–275** selects the correct business module component.
- **Lines 289–291** render shared heading/algorithm callouts and the compact related-algorithms actions.
- **Lines 293–309** render Overview cards, health alerts, demand rows and recent orders.
- **Lines 311–331** render the shared record table and actions for the five editable catalog/partner/report modules.
- **Lines 333–344** render Inventory and stock intake.
- **Lines 346–354** render Orders and the Bitmask DP assortment tool.
- **Lines 356–361** render Shipment tracking.
- **Lines 363–371** render Distribution capacity and matching.
- **Lines 373–385** render the DSA-to-module reference for class presentation.
- **Lines 388 onward** render the add/edit/order modal. On submit it calls the relevant save/update/order callback passed in by `App()`.

## 4. CSV flow

- **Lines 161–167 — `exportCsv()`** starts with a `recordType,id,dataJson` header, adds every record from every data bucket (including inventory events and algorithm runs), escapes each cell, then downloads a dated `.csv` file.
- **Lines 169–181 — `importCsv(file)`** reads a chosen CSV, parses its rows, parses each `dataJson` cell, and merges by record ID. Imported rows replace records with the same ID; other existing records remain.
- The included `public/foodchain-records.csv` is a starter snapshot with 31 sample records. After editing live data, click **Export CSV** to make a current backup. Browser security prevents a web page from silently rewriting a file in the project directory.
- To regenerate the starter snapshot after changing the seed object, run `node scripts/create-seed-csv.mjs` from `web/frontend`.

## 5. DSA mapping for presentation

| FoodChain module | Algorithm and what to say |
|---|---|
| Products | KMP, Z and Rabin–Karp search catalog text; Levenshtein suggests spelling corrections; QuickSort ranks demand; suffix structures index descriptions; Matrix-Chain and Optimal BST demonstrate data/search optimization. |
| Quality Reports | Aho–Corasick finds multiple warning words in one report scan. |
| Orders | Bitmask DP searches product combinations that fit a budget. |
| Inventory | Blelloch Scan produces cumulative stock, Parallel Reduce summarizes quantities and Reservoir Sampling selects a fair event sample. Miller–Rabin and Brent are numeric/parallel-work demonstrations. |
| Suppliers and Warehouses | Bipartite Matching pairs suppliers with eligible warehouses. |
| Distribution | Dinic, Ford–Fulkerson and Edmonds–Karp compare maximum flow; min-cut highlights constrained routes. 3-SAT reductions and Vertex Cover demonstrate supply-conflict modeling. |

For a short live demo: search `mango` in Products → run a quality scan on the spinach report → receive stock in Inventory → create an order → open Shipments → run the Distribution planner → export CSV.

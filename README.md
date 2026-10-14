# FoodChain — Supply & Distribution Management

FoodChain is a browser dashboard for managing food-supply-chain modules and demonstrating the DSA used inside them. It runs locally in the browser and does not require Spring Boot or MySQL.

For a source-by-source execution walkthrough with line references and a presentation demo flow, see [`PROJECT_EXECUTION_GUIDE.md`](web/frontend/PROJECT_EXECUTION_GUIDE.md).

## Run the dashboard

1. Open a terminal in `web/frontend`.
2. Run `npm install` once.
3. Run `npm run dev`.
4. Open the local URL printed by Vite (typically `http://localhost:5173`). Keep the terminal running while using the dashboard.

For this checkout, the project folder is `C:\Users\aryan\OneDrive\Desktop\Documents\ChatGPT\foodchain`. Eclipse is not required to run the web dashboard. The Java console project remains in `src` and can still be opened separately in Eclipse with JDK 17.

## FoodChain modules

- Overview — network status, demand and attention items.
- Farmers and Suppliers — partner records and supplier matching context.
- Products — catalog, smart search, spelling suggestions, demand ranking and description indexing.
- Warehouses — capacity and cold-storage records.
- Inventory — stock by warehouse, receiving, event stream and stock analytics.
- Orders — customer orders and a budget-based product assortment planner.
- Shipments — delivery tracking and status updates.
- Quality Reports — inspection records and food-safety warning scans.
- Distribution — warehouse network capacity, supplier assignments, route bottlenecks and constraint examples.
- DSA in modules — a short reference mapping each course concept to the FoodChain workflow where it is used.

Each algorithm explanation opens from the relevant business module. For example, product search runs KMP, Z-Function and Rabin–Karp on catalog text; a quality scan runs Aho–Corasick on a report; order planning runs Bitmask DP on the product catalog; and distribution planning runs max-flow and bipartite matching on the supply network. Results include a short explanation, trace and complexity.

## Records and CSV

The dashboard starts with realistic sample records. Changes are automatically saved in the browser's local storage. Use **Export CSV** to download every module record, inventory event and algorithm run in a single CSV file. **Import CSV** merges those rows by record ID, so you can restore a backup or load the included starter CSV at `web/frontend/public/foodchain-records.csv`.

Browsers do not allow a web page to silently rewrite a file in the project folder. Export CSV creates the current up-to-date file on demand; local browser storage keeps edits between visits on the same browser and origin. If you change the sample records in `src/App.jsx`, regenerate the starter snapshot from `web/frontend` with `node scripts/create-seed-csv.mjs`.

## Build the static dashboard

From `web/frontend`, run `npm run build`. Vite writes the deployable frontend to `web/frontend/dist`.

## Other project parts

- `src` contains the Java console project and DSA implementations.
- `web/backend` and `database` contain optional earlier Spring Boot and MySQL work; this local dashboard does not call them.

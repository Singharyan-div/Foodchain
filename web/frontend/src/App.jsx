import React, { useEffect, useMemo, useState } from 'react';
import { runAlgorithm } from './algorithms.js';
import './modules.css';

const STORE_KEY = 'foodchain.modules.v1';
const modules = [
  { id: 'overview', label: 'Overview', icon: '⌂', group: 'WORKSPACE' },
  { id: 'farmers', label: 'Farmers', icon: '♧', group: 'PARTNERS' },
  { id: 'suppliers', label: 'Suppliers', icon: '⇄', group: 'PARTNERS' },
  { id: 'products', label: 'Products', icon: '◉', group: 'CATALOG' },
  { id: 'warehouses', label: 'Warehouses', icon: '▤', group: 'CATALOG' },
  { id: 'inventory', label: 'Inventory', icon: '▦', group: 'OPERATIONS' },
  { id: 'orders', label: 'Orders', icon: '▣', group: 'OPERATIONS' },
  { id: 'shipments', label: 'Shipments', icon: '↗', group: 'OPERATIONS' },
  { id: 'qualityReports', label: 'Quality reports', icon: '✓', group: 'QUALITY' },
  { id: 'distribution', label: 'Distribution', icon: '⌘', group: 'PLANNING' },
  { id: 'dsaMap', label: 'DSA in modules', icon: '⌗', group: 'PROJECT GUIDE' },
];

const seed = {
  farmers: [
    { id: 'F-101', name: 'Green Valley Farm', contact: 'Ramesh Patil', phone: '+91 98765 12041', location: 'Nashik, Maharashtra', products: 'Tomato, Onion', status: 'Verified' },
    { id: 'F-102', name: 'Sahyadri Organics', contact: 'Meera Shinde', phone: '+91 98765 12042', location: 'Pune, Maharashtra', products: 'Mango, Spinach', status: 'Verified' },
    { id: 'F-103', name: 'Riverbend Produce', contact: 'Arjun Rao', phone: '+91 98765 12043', location: 'Kolhapur, Maharashtra', products: 'Rice, Potato', status: 'Review' },
  ],
  suppliers: [
    { id: 'S-201', name: 'HarvestLink Co-op', contact: 'Kavita Desai', phone: '+91 98765 22101', location: 'Mumbai, Maharashtra', category: 'Fresh produce', status: 'Active' },
    { id: 'S-202', name: 'Western Grain Traders', contact: 'Nikhil Shah', phone: '+91 98765 22102', location: 'Pune, Maharashtra', category: 'Grains', status: 'Active' },
    { id: 'S-203', name: 'CoolRoute Logistics', contact: 'Sana Khan', phone: '+91 98765 22103', location: 'Thane, Maharashtra', category: 'Cold-chain', status: 'Active' },
  ],
  products: [
    { id: 'P-301', name: 'Alphonso Mango', category: 'Fruit', unit: 'kg', price: 180, stock: 420, demand: 91, description: 'Sweet ripe alphonso mango from the Konkan harvest', supplierId: 'S-201', status: 'In season' },
    { id: 'P-302', name: 'Fresh Tomato', category: 'Vegetable', unit: 'kg', price: 38, stock: 185, demand: 84, description: 'Fresh red tomatoes packed at source', supplierId: 'S-201', status: 'In stock' },
    { id: 'P-303', name: 'Basmati Rice', category: 'Grain', unit: 'kg', price: 92, stock: 670, demand: 78, description: 'Aged aromatic basmati rice grain', supplierId: 'S-202', status: 'In stock' },
    { id: 'P-304', name: 'Baby Spinach', category: 'Vegetable', unit: 'kg', price: 64, stock: 42, demand: 67, description: 'Tender washed baby spinach leaves', supplierId: 'S-201', status: 'Low stock' },
    { id: 'P-305', name: 'Red Onion', category: 'Vegetable', unit: 'kg', price: 32, stock: 310, demand: 62, description: 'Locally grown red onion with dry outer skin', supplierId: 'S-201', status: 'In stock' },
  ],
  warehouses: [
    { id: 'W-401', name: 'Mumbai Central Hub', location: 'Navi Mumbai', capacity: 1600, used: 1030, coldStorage: 'Yes', manager: 'Priya Nair', status: 'Active' },
    { id: 'W-402', name: 'Pune Fresh Depot', location: 'Pimpri-Chinchwad', capacity: 950, used: 620, coldStorage: 'Yes', manager: 'Dev Kulkarni', status: 'Active' },
    { id: 'W-403', name: 'Nashik Produce Center', location: 'Nashik', capacity: 1200, used: 780, coldStorage: 'No', manager: 'Amit Pawar', status: 'Active' },
  ],
  inventory: [
    { id: 'I-501', productId: 'P-301', warehouseId: 'W-401', quantity: 220, reserved: 34, reorderAt: 80, updated: 'Today, 09:42' },
    { id: 'I-502', productId: 'P-302', warehouseId: 'W-401', quantity: 75, reserved: 18, reorderAt: 90, updated: 'Today, 09:36' },
    { id: 'I-503', productId: 'P-303', warehouseId: 'W-402', quantity: 410, reserved: 45, reorderAt: 120, updated: 'Today, 08:58' },
    { id: 'I-504', productId: 'P-304', warehouseId: 'W-402', quantity: 42, reserved: 16, reorderAt: 55, updated: 'Today, 08:21' },
    { id: 'I-505', productId: 'P-305', warehouseId: 'W-403', quantity: 260, reserved: 30, reorderAt: 70, updated: 'Yesterday' },
  ],
  orders: [
    { id: 'O-701', customer: 'Fresh Basket Market', items: 'Alphonso Mango × 12, Fresh Tomato × 20', total: 2920, status: 'Processing', created: 'Today, 10:24', warehouseId: 'W-401' },
    { id: 'O-700', customer: 'City Kitchen Co.', items: 'Basmati Rice × 30', total: 2760, status: 'Packed', created: 'Today, 09:50', warehouseId: 'W-402' },
    { id: 'O-699', customer: 'Green Plate Cafe', items: 'Baby Spinach × 8, Red Onion × 14', total: 960, status: 'Delivered', created: 'Yesterday', warehouseId: 'W-403' },
  ],
  shipments: [
    { id: 'SH-801', orderId: 'O-700', destination: 'Andheri, Mumbai', carrier: 'CoolRoute 07', status: 'In transit', eta: 'Today, 16:30', progress: 66 },
    { id: 'SH-800', orderId: 'O-699', destination: 'Bandra, Mumbai', carrier: 'FreshMove 12', status: 'Delivered', eta: 'Delivered 11:05', progress: 100 },
  ],
  qualityReports: [
    { id: 'Q-901', productId: 'P-301', batch: 'AM-KON-2408', inspector: 'Neha Joshi', date: 'Today, 08:14', score: 96, status: 'Passed', notes: 'Firm fruit, clean packaging, temperature within range.' },
    { id: 'Q-900', productId: 'P-304', batch: 'SP-PUN-117', inspector: 'Neha Joshi', date: 'Today, 07:48', score: 71, status: 'Review', notes: 'Leaf freshness good; condensation noticed on one crate.' },
    { id: 'Q-899', productId: 'P-302', batch: 'TM-NSK-052', inspector: 'Ravi More', date: 'Yesterday', score: 89, status: 'Passed', notes: 'Red ripe tomatoes, no mold, lot accepted.' },
  ],
  inventoryEvents: [
    { id: 'EV-1001', type: 'Stock received', productId: 'P-301', warehouseId: 'W-401', quantity: 40, at: 'Today, 09:42' },
    { id: 'EV-1000', type: 'Order reserved', productId: 'P-302', warehouseId: 'W-401', quantity: -20, at: 'Today, 09:36' },
    { id: 'EV-999', type: 'Quality release', productId: 'P-303', warehouseId: 'W-402', quantity: 65, at: 'Yesterday' },
    { id: 'EV-998', type: 'Stock received', productId: 'P-304', warehouseId: 'W-402', quantity: 18, at: 'Yesterday' },
  ],
  algorithmRuns: [],
};

const definitions = {
  farmers: { title: 'Farmers', subtitle: 'Farmer partners and the produce they grow.', icon: '♧', fields: [['name','Farm / farmer name'],['contact','Contact person'],['phone','Phone'],['location','Location'],['products','Products supplied'],['status','Status','select',['Verified','Review','Pending']]], columns: [['name','Farm / farmer'],['contact','Contact'],['location','Location'],['products','Products'],['status','Status']] },
  suppliers: { title: 'Suppliers', subtitle: 'Supplier network for food, packaging and cold-chain services.', icon: '⇄', fields: [['name','Supplier name'],['contact','Contact person'],['phone','Phone'],['location','Location'],['category','Category'],['status','Status','select',['Active','Review','Inactive']]], columns: [['name','Supplier'],['contact','Contact'],['category','Category'],['location','Location'],['status','Status']] },
  products: { title: 'Products', subtitle: 'Food catalog, stock signals and demand across the network.', icon: '◉', fields: [['name','Product name'],['category','Category'],['unit','Unit'],['price','Price (₹)','number'],['stock','Catalog stock','number'],['demand','Demand score (0–100)','number'],['description','Product description','textarea'],['supplierId','Supplier ID'],['status','Status']], columns: [['name','Product'],['category','Category'],['price','Price'],['stock','Catalog stock'],['demand','Demand'],['status','Status']] },
  warehouses: { title: 'Warehouses', subtitle: 'Storage locations, capacity and cold-chain readiness.', icon: '▤', fields: [['name','Warehouse name'],['location','Location'],['capacity','Capacity (units)','number'],['used','Used capacity','number'],['coldStorage','Cold storage','select',['Yes','No']],['manager','Manager'],['status','Status']], columns: [['name','Warehouse'],['location','Location'],['capacity','Capacity'],['used','Used'],['coldStorage','Cold storage'],['status','Status']] },
  qualityReports: { title: 'Quality reports', subtitle: 'Inspection records, batch decisions and automated warning scans.', icon: '✓', fields: [['productId','Product ID'],['batch','Batch code'],['inspector','Inspector'],['score','Quality score','number'],['status','Status','select',['Passed','Review','Rejected']],['notes','Inspection notes','textarea']], columns: [['batch','Batch'],['productId','Product'],['inspector','Inspector'],['score','Score'],['notes','Inspection notes'],['status','Status']] },
};

const money = value => `₹${Number(value || 0).toLocaleString('en-IN')}`;
const pageDate = new Intl.DateTimeFormat('en-IN', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' }).format(new Date()).toUpperCase();
const newId = prefix => `${prefix}-${Date.now().toString().slice(-6)}`;
const productName = (db, id) => db.products.find(item => item.id === id)?.name || id || '—';
const warehouseName = (db, id) => db.warehouses.find(item => item.id === id)?.name || id || '—';
const initialData = () => {
  try {
    const saved = localStorage.getItem(STORE_KEY);
    if (saved) {
      const parsed = JSON.parse(saved);
      return Object.fromEntries(Object.keys(seed).map(key => [key, Array.isArray(parsed[key]) ? parsed[key] : seed[key]]));
    }
  } catch { /* use starter records if browser storage is unavailable */ }
  return structuredClone(seed);
};

function csvEscape(value) {
  const text = String(value ?? '');
  return `"${text.replaceAll('"', '""')}"`;
}
function parseCsvLine(line) {
  const cells = []; let cell = ''; let quoted = false;
  for (let i = 0; i < line.length; i++) {
    const ch = line[i];
    if (ch === '"' && quoted && line[i + 1] === '"') { cell += '"'; i++; }
    else if (ch === '"') quoted = !quoted;
    else if (ch === ',' && !quoted) { cells.push(cell); cell = ''; }
    else cell += ch;
  }
  cells.push(cell); return cells;
}

function App() {
  const [db, setDb] = useState(initialData);
  const [active, setActive] = useState('overview');
  const [query, setQuery] = useState('');
  const [modal, setModal] = useState(null);
  const [notice, setNotice] = useState('');
  const [algoResult, setAlgoResult] = useState(null);
  const [inventoryProduct, setInventoryProduct] = useState('P-301');
  const [inventoryWarehouse, setInventoryWarehouse] = useState('W-401');
  const [inventoryQty, setInventoryQty] = useState(25);
  const [qualityScan, setQualityScan] = useState(null);
  const [budget, setBudget] = useState(300);
  const [routePlan, setRoutePlan] = useState(null);
  const [demandSorted, setDemandSorted] = useState(false);
  const [demandOrder, setDemandOrder] = useState([]);
  const [globalSearch, setGlobalSearch] = useState('');

  useEffect(() => { localStorage.setItem(STORE_KEY, JSON.stringify(db)); }, [db]);
  useEffect(() => { if (!notice) return undefined; const timer = setTimeout(() => setNotice(''), 3200); return () => clearTimeout(timer); }, [notice]);
  const current = modules.find(item => item.id === active) || modules[0];

  const commit = (message, updater) => { setDb(prev => updater(prev)); if (message) setNotice(message); };
  const recordRun = (moduleId, id, input, explanation) => {
    try {
      const output = runAlgorithm(id, input);
      const run = { id: newId('RUN'), module: moduleId, algorithm: id, explanation, headline: output.headline, complexity: output.complexity, at: new Date().toLocaleString(), trace: output.trace, result: output.result };
      setDb(prev => ({ ...prev, algorithmRuns: [run, ...prev.algorithmRuns].slice(0, 100) }));
      setAlgoResult(run); return output;
    } catch (error) { setNotice(error.message || 'Algorithm could not run.'); return null; }
  };
  const addRecord = (type, values) => {
    const prefixes = { farmers: 'F', suppliers: 'S', products: 'P', warehouses: 'W', qualityReports: 'Q' };
    if (type === 'products' || type === 'warehouses') {
      for (const key of ['price','stock','demand','capacity','used','score']) if (values[key] !== undefined && values[key] !== '') values[key] = Number(values[key]);
    }
    if (type === 'qualityReports') { values.score = Number(values.score); values.date = 'Just now'; }
    const row = { ...values, id: newId(prefixes[type]) };
    const singular = { farmers: 'Farmer', suppliers: 'Supplier', products: 'Product', warehouses: 'Warehouse', qualityReports: 'Quality report' }[type];
    commit(`${singular} added`, prev => ({ ...prev, [type]: [row, ...prev[type]] }));
    setModal(null);
  };
  const updateRecord = (type, row) => {
    commit('Changes saved', prev => ({ ...prev, [type]: prev[type].map(item => item.id === row.id ? row : item) })); setModal(null);
  };
  const deleteRecord = (type, row) => {
    if (!window.confirm(`Delete ${row.name || row.batch || row.id}?`)) return;
    commit('Record deleted', prev => ({ ...prev, [type]: prev[type].filter(item => item.id !== row.id) }));
  };
  const exportCsv = () => {
    const rows = [['recordType','id','dataJson']];
    Object.entries(db).forEach(([type, records]) => records.forEach(record => rows.push([type, record.id || '', JSON.stringify(record)])));
    const csv = rows.map(row => row.map(csvEscape).join(',')).join('\r\n');
    const url = URL.createObjectURL(new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8' }));
    const anchor = document.createElement('a'); anchor.href = url; anchor.download = `foodchain-records-${new Date().toISOString().slice(0,10)}.csv`; anchor.click(); URL.revokeObjectURL(url);
    setNotice('All module records exported to CSV');
  };
  const importCsv = async file => {
    if (!file) return;
    try {
      const lines = (await file.text()).replace(/^\uFEFF/, '').split(/\r?\n/).filter(Boolean);
      const head = parseCsvLine(lines.shift() || '');
      const typeAt = head.indexOf('recordType'), dataAt = head.indexOf('dataJson');
      if (typeAt < 0 || dataAt < 0) throw new Error('CSV needs recordType and dataJson columns.');
      const imported = {};
      lines.forEach(line => { const cells = parseCsvLine(line); const type = cells[typeAt]; if (!Array.isArray(seed[type])) return; (imported[type] ||= []).push(JSON.parse(cells[dataAt])); });
      if (!Object.keys(imported).length) throw new Error('No FoodChain records were found in that CSV.');
      commit('CSV data imported and merged', prev => ({ ...prev, ...Object.fromEntries(Object.entries(imported).map(([type, rows]) => [type, [...rows, ...prev[type].filter(existing => !rows.some(row => row.id === existing.id))]])) }));
    } catch (error) { setNotice(error.message || 'Could not read CSV.'); }
  };

  const productSearch = () => {
    if (!query.trim()) { setNotice('Enter a product name, code or description to search.'); return; }
    const text = db.products.map(p => `${p.name} — ${p.description} — ${p.id}`).join(' | ');
    const exact = recordRun('products', 'kmp', { text: text.toLowerCase(), pattern: query.toLowerCase() }, 'KMP scans names, descriptions and product codes without rechecking matched prefixes.');
    recordRun('products', 'z-function', { text: text.toLowerCase(), pattern: query.toLowerCase() }, 'The Z-function compares the search term with every catalog position in linear time.');
    recordRun('products', 'rabin-karp', { text: text.toLowerCase(), pattern: query.toLowerCase() }, 'Rabin–Karp searches the product catalog with rolling hashes and verifies matching windows.');
    if (query.trim() && exact?.result?.matches?.length === 0) {
      const ranked = db.products.map(p => ({ ...p, distance: runAlgorithm('edit-distance', { first: query.toLowerCase(), second: p.name.toLowerCase() }).result.levenshtein }));
      const best = ranked.sort((a,b) => a.distance - b.distance)[0];
      if (best && best.distance <= Math.max(2, Math.floor(query.length / 2))) setNotice(`No exact match. Did you mean ${best.name}? (Levenshtein distance ${best.distance})`);
    }
  };
  const runQualityScan = report => {
    const output = recordRun('qualityReports', 'aho-corasick', { text: `${report.batch} ${report.notes}`.toLowerCase(), keywords: ['mold','spoiled','contaminated','temperature','damage','condensation'] }, 'Aho–Corasick checks one inspection note against many food-safety warning terms in one pass.');
    if (output) setQualityScan({ reportId: report.id, ...output });
  };
  const runInventoryAnalytics = () => {
    const amounts = db.inventory.map(row => Math.max(0, Number(row.quantity) - Number(row.reserved)));
    const scan = recordRun('inventory', 'blelloch-scan', { values: amounts }, 'Blelloch scan computes running available-stock totals across inventory rows.');
    recordRun('inventory', 'parallel-reduce', { values: amounts, workers: 3 }, 'Parallel reduce combines inventory chunks into total, average and maximum stock.');
    recordRun('inventory', 'reservoir-sampling', { values: db.inventoryEvents.map(e => `${e.type}: ${productName(db,e.productId)} ${e.quantity > 0 ? '+' : ''}${e.quantity}`), k: Math.min(3, db.inventoryEvents.length) }, 'Reservoir sampling selects a fair small sample from the ongoing inventory-event stream.');
    return scan;
  };
  const planDistribution = () => {
    const nodes = ['Farm supply','Nashik Produce Center','Pune Fresh Depot','Mumbai Central Hub','Market'];
    const freeCapacity = id => { const warehouse=db.warehouses.find(w=>w.id===id); return Math.max(1,Number(warehouse?.capacity||500)-Number(warehouse?.used||0)); };
    const totalSupply = db.inventory.reduce((sum,row)=>sum+Math.max(0,row.quantity-row.reserved),0);
    const edges = [
      {from:nodes[0],to:nodes[1],capacity:Math.min(320,totalSupply)},{from:nodes[0],to:nodes[2],capacity:Math.min(250,totalSupply)},
      {from:nodes[1],to:nodes[3],capacity:Math.min(210,freeCapacity('W-403'))},{from:nodes[2],to:nodes[3],capacity:Math.min(170,freeCapacity('W-402'))},
      {from:nodes[1],to:nodes[4],capacity:90},{from:nodes[3],to:nodes[4],capacity:330},
    ];
    const flow = recordRun('distribution','dinic',{nodes,source:nodes[0],sink:nodes[4],edges},'Dinic routes as much produce as possible through capacity-limited hubs; min-cut edges expose bottlenecks.');
    const eligible = db.warehouses.map((_, wi) => db.suppliers.map((__, si) => [0, 1, 2].includes((si + wi) % 3) && !(wi === 2 && si === 1) ? [si, wi] : null).filter(Boolean)).flat();
    const matching = recordRun('distribution','bipartite-matching',{suppliers:db.suppliers.map(s=>s.name),warehouses:db.warehouses.map(w=>w.name),eligible},'Bipartite matching assigns suppliers to eligible warehouses with one-to-one capacity.');
    setRoutePlan({ flow, matching });
  };
  const productDoc = db.products.map(p => `${p.name}. ${p.description}.`).join(' ').toLowerCase();
  const relatedTools = {
    products: [
      { title: 'Index product descriptions', hint: 'SA-IS + Kasai LCP', run: () => { recordRun('products','suffix-array',{text:productDoc},'SA-IS builds a linear-time suffix index for product-description text.'); recordRun('products','kasai-lcp',{text:productDoc},'Kasai LCP scans adjacent suffixes to reveal repeated description phrases.'); } },
      { title: 'Search repeated phrases', hint: 'Suffix Tree + Automaton', run: () => { const terms=[query||'fresh','mango','rice']; recordRun('products','suffix-tree',{text:productDoc,queries:terms},'A compact suffix tree answers repeated substring queries in product descriptions.'); recordRun('products','suffix-automaton',{text:productDoc,queries:terms},'A suffix automaton checks requested terms against the product text.'); } },
      { title: 'Optimize catalog data work', hint: 'Matrix-Chain + Optimal BST', run: () => { recordRun('products','matrix-chain',{dimensions:[10,30,5,60]},'Matrix-chain DP chooses the cheapest order for a multi-stage catalog data pipeline.'); const sorted=[...db.products].sort((a,b)=>a.name.localeCompare(b.name)); recordRun('products','optimal-bst',{keys:sorted.map(p=>p.name),successful:sorted.map(p=>Math.max(1,p.demand)),unsuccessful:Array(sorted.length+1).fill(1)},'Optimal BST uses demand frequencies to keep common product lookups near the root.'); } },
    ],
    inventory: [
      { title: 'Estimate parallel stock processing', hint: 'Brent’s theorem', run: () => recordRun('inventory','brent-theorem',{work:Math.max(100,db.inventory.length*80),criticalPath:Math.max(10,db.inventory.length*12),processors:4},'Brent’s theorem bounds runtime from total stock work, critical path and processor count.') },
      { title: 'Validate a large batch number', hint: 'Miller–Rabin', run: () => recordRun('inventory','miller-rabin',{number:'18446744073709551557'},'Miller–Rabin demonstrates a fast primality check for large numeric batch identifiers.') },
    ],
    distribution: [
      { title: 'Compare augmenting-path flow', hint: 'Ford–Fulkerson', run: () => recordRun('distribution','ford-fulkerson',{nodes:['Farm','Nashik','Pune','Mumbai','Market'],source:'Farm',sink:'Market',edges:[{from:'Farm',to:'Nashik',capacity:320},{from:'Farm',to:'Pune',capacity:250},{from:'Nashik',to:'Mumbai',capacity:210},{from:'Pune',to:'Mumbai',capacity:170},{from:'Nashik',to:'Market',capacity:90},{from:'Mumbai',to:'Market',capacity:330}]},'Ford–Fulkerson augments feasible farm-to-market routes until no capacity remains.') },
      { title: 'Compare BFS capacity flow', hint: 'Edmonds–Karp', run: () => recordRun('distribution','edmonds-karp',{nodes:['Farm','Nashik','Pune','Mumbai','Market'],source:'Farm',sink:'Market',edges:[{from:'Farm',to:'Nashik',capacity:320},{from:'Farm',to:'Pune',capacity:250},{from:'Nashik',to:'Mumbai',capacity:210},{from:'Pune',to:'Mumbai',capacity:170},{from:'Nashik',to:'Market',capacity:90},{from:'Mumbai',to:'Market',capacity:330}]},'Edmonds–Karp uses BFS augmenting paths to calculate delivery capacity.') },
      { title: 'Check supply constraints', hint: '3-SAT reductions', run: () => recordRun('distribution','3sat-reductions',{variables:3,clauses:[[1,-2,3],[-1,2,3],[1,2,-3]]},'A small constraint formula is reduced to CLIQUE, Independent Set and Vertex Cover witnesses.') },
      { title: 'Find critical route hubs', hint: 'Vertex-Cover 2-approximation', run: () => recordRun('distribution','vertex-cover',{locations:['Nashik Hub','Pune Depot','Mumbai Hub','Market Depot'],edges:[[0,1],[1,2],[2,3],[0,3]]},'The 2-approximation selects critical hubs that cover every modeled conflict route.') },
    ],
  };
  const createOrder = form => {
    const product = db.products.find(p => p.id === form.productId); const qty = Number(form.quantity);
    if (!product || qty < 1) { setNotice('Choose a product and enter a quantity.'); return; }
    const row = db.inventory.find(i => i.productId === product.id && i.warehouseId === form.warehouseId);
    if (!row || row.quantity - row.reserved < qty) { setNotice('Not enough available stock at that warehouse.'); return; }
    const order = { id: newId('O'), customer: form.customer, items: `${product.name} × ${qty}`, total: qty * Number(product.price), status: 'Processing', created: 'Just now', warehouseId: form.warehouseId };
    const ship = { id: newId('SH'), orderId: order.id, destination: form.destination, carrier: 'FoodChain Dispatch', status: 'Preparing', eta: 'Awaiting pickup', progress: 15 };
    commit('Order placed and stock reserved', prev => ({ ...prev, products: prev.products.map(p => p.id === product.id ? { ...p, stock: Math.max(0, Number(p.stock || 0) - qty) } : p), orders: [order, ...prev.orders], shipments: [ship, ...prev.shipments], inventory: prev.inventory.map(i => i.id === row.id ? { ...i, quantity: i.quantity - qty, updated: 'Just now' } : i), inventoryEvents: [{ id: newId('EV'), type: 'Order reserved', productId: product.id, warehouseId: form.warehouseId, quantity: -qty, at: 'Just now' }, ...prev.inventoryEvents] }));
    setModal(null);
  };
  const changeShipment = (ship, status) => {
    const progress = { Preparing: 15, 'In transit': 66, Delivered: 100, Delayed: 45 }[status];
    commit('Shipment status updated', prev => ({ ...prev, shipments: prev.shipments.map(s => s.id === ship.id ? { ...s, status, progress, eta: status === 'Delivered' ? 'Delivered just now' : s.eta } : s) }));
  };

  const globalMatches = useMemo(() => {
    const term = globalSearch.trim().toLowerCase(); if (!term) return [];
    return Object.entries(db).flatMap(([type, rows]) => rows.filter(row => JSON.stringify(row).toLowerCase().includes(term)).map(row => ({ type, row }))).slice(0, 8);
  }, [db, globalSearch]);

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark">F<span>.</span></div><div><b>foodchain</b><small>SUPPLY NETWORK</small></div></div>
      <div className="workspace-tag"><i className="live-dot"/>LOCAL WORKSPACE · CSV READY</div>
      {['WORKSPACE','PARTNERS','CATALOG','OPERATIONS','QUALITY','PLANNING','PROJECT GUIDE'].map(group => <div className="nav-group" key={group}><div className="nav-label">{group}</div>{modules.filter(m=>m.group===group).map(item=><button className={`nav-item ${active===item.id?'active':''}`} key={item.id} onClick={()=>{setActive(item.id);setAlgoResult(null);setQuery('')}}><span className="nav-icon">{item.icon}</span><span>{item.label}</span>{item.id==='qualityReports'&&db.qualityReports.filter(r=>r.status==='Review').length>0&&<i className="nav-count">{db.qualityReports.filter(r=>r.status==='Review').length}</i>}</button>)}</div>)}
      <div className="sidebar-bottom"><div className="season-card"><div className="season-icon">☼</div><div><b>Fresh harvest season</b><span>Local demo data · saved here</span></div><span className="season-arrow">↗</span></div><div className="profile"><div className="avatar">FC</div><div><b>FoodChain Admin</b><span>Local workspace</span></div><button className="more-button" title="Export CSV" onClick={exportCsv}>⇩</button></div></div>
    </aside>

    <main className="main-area">
      <header className="topbar"><div className="crumb">FoodChain <b>/</b> <strong>{current.label}</strong></div><div className="top-actions"><div className="global-search"><span>⌕</span><input placeholder="Search all records..." value={globalSearch} onChange={e=>setGlobalSearch(e.target.value)}/>{globalSearch&&<button onClick={()=>setGlobalSearch('')}>×</button>}{globalSearch&&<div className="global-results">{globalMatches.length?globalMatches.map(({type,row})=><button key={`${type}-${row.id}`} onClick={()=>{setActive(type);setGlobalSearch('')}}><b>{row.name||row.batch||row.id}</b><small>{modules.find(m=>m.id===type)?.label||type} · {row.id}</small></button>):<span>No matching records</span>}</div>}</div><span className="connection"><i/>Saved in this browser</span><label className="button button-quiet csv-import">⇧ Import CSV<input type="file" accept=".csv,text/csv" onChange={e=>{importCsv(e.target.files?.[0]);e.target.value=''}}/></label><button className="button button-primary" onClick={exportCsv}><span>⇩</span> Export CSV</button></div></header>
      <section className="page-content">
        {active==='overview'&&<Overview db={db} setActive={setActive} exportCsv={exportCsv} openOrder={()=>setModal({type:'newOrder'})} />}
        {definitions[active]&&<RecordsPage type={active} db={db} query={query} setQuery={setQuery} add={()=>setModal({type:active})} edit={row=>setModal({type:active,row})} remove={row=>deleteRecord(active,row)} productSearch={productSearch} recordRun={recordRun} demandSorted={demandSorted} setDemandSorted={setDemandSorted} demandOrder={demandOrder} setDemandOrder={setDemandOrder} qualityScan={qualityScan} scanQuality={runQualityScan} related={relatedTools[active]||[]} />}
        {active==='inventory'&&<InventoryPage db={db} setDb={setDb} inventoryProduct={inventoryProduct} setInventoryProduct={setInventoryProduct} inventoryWarehouse={inventoryWarehouse} setInventoryWarehouse={setInventoryWarehouse} inventoryQty={inventoryQty} setInventoryQty={setInventoryQty} onAnalytics={runInventoryAnalytics} related={relatedTools.inventory} />}
        {active==='orders'&&<OrdersPage db={db} setBudget={setBudget} budget={budget} recordRun={recordRun} addOrder={()=>setModal({type:'newOrder'})} />}
        {active==='shipments'&&<ShipmentsPage db={db} changeShipment={changeShipment} />}
        {active==='distribution'&&<DistributionPage db={db} plan={planDistribution} routePlan={routePlan} related={relatedTools.distribution} />}
        {active==='dsaMap'&&<DsaMap setActive={setActive} />}
      </section>
      <footer className="footer"><span>FOODCHAIN · FOOD SUPPLY & DISTRIBUTION MANAGEMENT</span><span>Browser storage <b>● ON</b> · Export a CSV backup any time</span></footer>
    </main>
    {modal&&<RecordModal modal={modal} db={db} close={()=>setModal(null)} save={addRecord} update={updateRecord} createOrder={createOrder} />}
    {notice&&<div className="toast"><span>✓</span>{notice}<button onClick={()=>setNotice('')}>×</button></div>}
    {algoResult&&<div className="algo-drawer-backdrop" onClick={()=>setAlgoResult(null)}><aside className="algo-drawer" onClick={e=>e.stopPropagation()}><div className="algo-drawer-head"><div><span className="eyebrow">ALGORITHM USED IN {modules.find(m=>m.id===algoResult.module)?.label?.toUpperCase()}</span><h2>{algorithmTitle(algoResult.algorithm)}</h2></div><button onClick={()=>setAlgoResult(null)}>×</button></div><div className="algorithm-explain"><b>Why this is here</b><p>{algoResult.explanation}</p></div><div className="algo-result-callout"><span>RESULT</span><b>{algoResult.headline}</b><small>{algoResult.complexity}</small></div><div className="panel"><div className="panel-heading"><div><span className="eyebrow">WHAT HAPPENED</span><h3>Execution steps</h3></div><small>{algoResult.at}</small></div><div className="trace-steps">{algoResult.trace.map((step,i)=><div className="trace-step" key={i}><span>{String(i+1).padStart(2,'0')}</span><p>{step}</p></div>)}</div><details className="result-json"><summary>View calculated data <span>⌄</span></summary><pre>{JSON.stringify(algoResult.result,null,2)}</pre></details></div></aside></div>}
  </div>;
}

function algorithmTitle(id) {
  return ({kmp:'KMP product search', 'z-function':'Z-Function description match','rabin-karp':'Rabin–Karp code search','aho-corasick':'Aho–Corasick safety scan','suffix-array':'SA-IS description index','kasai-lcp':'Kasai repeated text','suffix-automaton':'Suffix automaton lookup','suffix-tree':'Suffix tree lookup','edit-distance':'Levenshtein correction','bitmask-dp':'Bitmask DP assortment','matrix-chain':'Matrix-chain data flow','optimal-bst':'Optimal BST product lookup','ford-fulkerson':'Ford–Fulkerson distribution','edmonds-karp':'Edmonds–Karp capacity check',dinic:'Dinic distribution planner','bipartite-matching':'Bipartite warehouse matching','3sat-reductions':'3-SAT constraint reduction','vertex-cover':'Vertex-cover hub analysis','randomized-quicksort':'Randomized demand ranking','reservoir-sampling':'Reservoir event sampling','miller-rabin':'Miller–Rabin numeric check','blelloch-scan':'Blelloch stock scan','parallel-reduce':'Parallel stock summary','brent-theorem':'Brent parallel estimate'})[id]||id;
}

function Heading({eyebrow,title,subtitle,action}) { return <div className="page-heading"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1><p>{subtitle}</p></div>{action&&<div className="heading-action">{action}</div>}</div>; }
function ModuleAlgorithm({title,body,onClick,label='Run module analysis'}) { return <div className="module-algorithm"><div className="module-algo-icon">⌘</div><div className="module-algo-copy"><span className="eyebrow">ALGORITHM IN THIS MODULE</span><b>{title}</b><p>{body}</p></div><button className="button button-quiet" onClick={onClick}>{label} <span>↗</span></button></div>; }
function RelatedAlgorithms({items=[]}) { if (!items.length) return null; return <div className="related-algorithms"><span className="eyebrow">MORE METHODS USED IN THIS MODULE</span><div>{items.map((item,i)=><button key={i} onClick={item.run}><b>{item.title}</b><small>{item.hint}</small></button>)}</div></div>; }

function Overview({db,setActive,exportCsv,openOrder}) {
  const lowStock=db.inventory.filter(i=>i.quantity-i.reserved<=i.reorderAt).length;
  const pending=db.orders.filter(o=>o.status!=='Delivered').length;
  const shipped=db.shipments.filter(s=>s.status==='In transit').length;
  const totalValue=db.products.reduce((sum,p)=>sum+p.price*p.stock,0);
  const topProducts=[...db.products].sort((a,b)=>b.demand-a.demand).slice(0,4);
  return <>
    <div className="page-heading"><div><span className="eyebrow">{pageDate} <span className="heading-spark">✳</span> NETWORK HEALTHY</span><h1>Good morning, FoodChain</h1><p>Here’s what’s moving through your supply network today.</p></div><div className="heading-action"><button className="button button-quiet" onClick={exportCsv}>⇩ Download CSV backup</button><button className="button button-primary" onClick={openOrder}><span>＋</span> Create order</button></div></div>
    <div className="overview-banner"><div className="banner-copy"><span className="banner-kicker">FROM FARM TO TABLE, IN SYNC</span><h2>Fresh food. Clear movement.<br/>Less waste along the way.</h2><p>Monitor growers, stock, quality and delivery from one workspace.</p><button className="button button-light" onClick={()=>setActive('distribution')}>Open distribution planner <span>↗</span></button></div><div className="banner-orbit orbit-one"/><div className="banner-orbit orbit-two"/><div className="produce-art"><span className="produce-leaf">⌁</span><span className="produce-fruit">●</span><span className="produce-fruit small">●</span><span className="produce-stem">⌁</span></div><div className="banner-side"><span className="mini-pill"><i/> LIVE NETWORK</span><b>{db.farmers.length} growing partners</b><span>across {new Set(db.farmers.map(f=>f.location.split(',')[0])).size} farm regions</span><div className="banner-line"/><b>{db.warehouses.length} active hubs</b><span>coordinating fresh deliveries</span></div></div>
    <div className="stats-grid"><Stat label="Active products" value={db.products.length} detail="Across the food catalog" icon="◉" hue="green"/><Stat label="Available inventory" value={`${db.inventory.reduce((s,i)=>s+Math.max(0,i.quantity-i.reserved),0).toLocaleString()} kg`} detail={`${lowStock} stock alerts need a look`} icon="▦" hue={lowStock?'orange':'blue'}/><Stat label="Orders in motion" value={pending} detail={`${shipped} shipments currently moving`} icon="↗" hue="blue"/><Stat label="Catalog value" value={money(totalValue)} detail="Estimated value of listed stock" icon="₹" hue="orange"/></div>
    <div className="dashboard-grid overview-module-grid"><section className="panel"><div className="panel-heading"><div><span className="eyebrow">WHAT NEEDS ATTENTION</span><h3>Supply pulse</h3></div><button className="text-action" onClick={()=>setActive('inventory')}>Open inventory <span>→</span></button></div><div className="pulse-list">{db.inventory.filter(i=>i.quantity-i.reserved<=i.reorderAt).slice(0,3).map(row=><div className="pulse-row" key={row.id}><span className="pulse-icon amber">!</span><div><b>{productName(db,row.productId)} is running low</b><small>{warehouseName(db,row.warehouseId)} · {row.quantity-row.reserved} available · reorder at {row.reorderAt}</small></div><button className="text-action" onClick={()=>setActive('inventory')}>Review</button></div>)}{db.qualityReports.filter(r=>r.status==='Review').slice(0,2).map(row=><div className="pulse-row" key={row.id}><span className="pulse-icon mint">✓</span><div><b>Quality report needs review</b><small>{productName(db,row.productId)} · batch {row.batch} · score {row.score}/100</small></div><button className="text-action" onClick={()=>setActive('qualityReports')}>Inspect</button></div>)}{!lowStock&&!db.qualityReports.some(r=>r.status==='Review')&&<div className="empty-state compact"><span>✓</span><b>All clear</b><p>No stock or quality alerts right now.</p></div>}</div><div className="panel-foot"><span><i className="legend-dot"/> Inventory and quality signals</span><span>Updated from local records</span></div></section>
    <section className="panel"><div className="panel-heading"><div><span className="eyebrow">DEMAND, THIS WEEK</span><h3>Products customers want</h3></div><button className="round-link" title="Products" onClick={()=>setActive('products')}>↗</button></div><div className="demand-chart">{topProducts.map((p,i)=><div className="demand-row" key={p.id}><div className="demand-name"><span className={`rank rank-${i}`}>0{i+1}</span><b>{p.name}</b><small>{p.category} · {p.stock} kg in catalog</small></div><div className="demand-track"><span style={{width:`${p.demand}%`}}/></div><div className="demand-value">{p.demand}<small> / 100</small></div></div>)}</div><div className="panel-foot"><span>Ranked by current demand score</span><span>{db.products.length} products</span></div></section></div>
    <div className="overview-bottom"><div className="supply-note"><div className="note-icon">⌗</div><div><b>Algorithms live inside the work</b><p>Search products, review quality, plan bundles or model routes. The module shows the algorithm it used and its result.</p></div><button className="button button-quiet" onClick={()=>setActive('dsaMap')}>See module map <span>↗</span></button></div><section className="panel orders-panel"><div className="panel-heading"><div><span className="eyebrow">RECENT ACTIVITY</span><h3>Latest orders</h3></div><button className="text-action" onClick={()=>setActive('orders')}>All orders <span>→</span></button></div><div className="recent-list">{db.orders.slice(0,3).map((o,i)=><div className="recent-order" key={o.id}><div className={`order-avatar hue-${i}`}>{o.customer.split(' ').map(x=>x[0]).slice(0,2).join('')}</div><div className="recent-info"><b>{o.customer}</b><span>{o.id} · {o.items}</span></div><div className="recent-meta"><Status value={o.status}/><small>{money(o.total)}</small></div></div>)}</div></section></div>
  </>;
}
function Stat({label,value,detail,icon,hue}) { return <div className="stat-card"><div className="stat-top">{label}<span className={`stat-icon ${hue}`}>{icon}</span></div><strong>{value}</strong><small><i className={`tiny-dot ${hue}`}/>{detail}</small></div>; }
function Status({value}) { const cls=String(value||'').toLowerCase().replaceAll(' ','-'); return <span className={`status-pill status-${cls}`}>{value}</span>; }

function RecordsPage({type,db,query,setQuery,add,edit,remove,productSearch,recordRun,demandSorted,setDemandSorted,demandOrder,setDemandOrder,qualityScan,scanQuality,related}) {
  const def=definitions[type]; let rows=[...db[type]]; const columns=def.columns;
  if(type==='products'&&demandSorted) rows.sort((a,b)=>demandOrder.indexOf(a.id)-demandOrder.indexOf(b.id));
  const shown=rows.filter(row=>JSON.stringify(row).toLowerCase().includes(query.toLowerCase()));
  let action;
  if(type==='products') action=<><div className="module-search"><span>⌕</span><input value={query} placeholder="Search products, codes, descriptions" onChange={e=>setQuery(e.target.value)} onKeyDown={e=>e.key==='Enter'&&productSearch()}/><button onClick={productSearch}>Smart search</button></div><button className="button button-primary" onClick={add}><span>＋</span> Add product</button></>;
  else action=<button className="button button-primary" onClick={add}><span>＋</span> Add {type==='qualityReports'?'report':type.slice(0,-1)}</button>;
  return <>
    <Heading eyebrow={`FOODCHAIN · ${def.title.toUpperCase()}`} title={def.title} subtitle={def.subtitle} action={action}/>
    {type==='products'&&<ModuleAlgorithm title="KMP search + Levenshtein name correction" body="The search scans product names, IDs and descriptions. If spelling is off, dynamic programming suggests the closest product name." onClick={productSearch} label="Run smart search"/>}
    {type==='qualityReports'&&<ModuleAlgorithm title="Aho–Corasick warning-keyword scan" body="Check one report against food-safety terms such as mold, spoiled, contamination and temperature in a single multi-pattern pass." onClick={()=>db.qualityReports[0]&&scanQuality(db.qualityReports[0])} label="Scan latest report"/>}
    {type==='farmers'&&<ModuleAlgorithm title="Levenshtein name correction" body="When a farmer or farm name is entered with a typo, edit-distance dynamic programming can recommend the closest existing partner before adding duplicates." onClick={()=>recordRun('farmers','edit-distance',{first:'Green Vally Farm',second:'Green Valley Farm'},'Levenshtein dynamic programming estimates likely name correction to reduce duplicate partner records.')} label="See name correction"/>}
    {type==='suppliers'&&<ModuleAlgorithm title="Bipartite matching in warehouse assignment" body="Supplier eligibility is paired with warehouse capacity in the Distribution module to produce one-to-one supplier assignments." onClick={()=>recordRun('suppliers','bipartite-matching',{suppliers:db.suppliers.map(s=>s.name),warehouses:db.warehouses.map(w=>w.name),eligible:db.suppliers.flatMap((_,si)=>db.warehouses.map((__,wi)=>si%2===wi%2?[si,wi]:null).filter(Boolean))},'Supplier and warehouse entities form the two sides of a bipartite graph; matching assigns compatible pairs.')} label="Preview matching"/>}
    {type==='warehouses'&&<ModuleAlgorithm title="Flow network and min-cut capacity" body="Warehouses become intermediate nodes in Distribution; max-flow estimates how much produce can reach markets and identifies saturated routes." onClick={()=>recordRun('warehouses','edmonds-karp',{nodes:['Farm','Warehouse A','Warehouse B','Market'],source:'Farm',sink:'Market',edges:[{from:'Farm',to:'Warehouse A',capacity:20},{from:'Farm',to:'Warehouse B',capacity:15},{from:'Warehouse A',to:'Market',capacity:12},{from:'Warehouse B',to:'Market',capacity:13}]},'Edmonds–Karp uses BFS augmenting paths to calculate the capacity through warehouse hubs.')} label="Preview hub capacity"/>}
    {type==='qualityReports'&&qualityScan&&<div className="inline-result"><b>Latest scan:</b> {qualityScan.headline}<span>{qualityScan.result.matches?.length?qualityScan.result.matches.map(m=>m.keyword).join(', '):'No warning terms found'}</span></div>}
    <RelatedAlgorithms items={related}/>
    <div className="panel records-panel"><div className="toolbar"><div><span className="table-count">{shown.length} {def.title.toLowerCase()}</span><small>· Saved automatically in this browser</small></div><div className="table-search"><span>⌕</span><input value={query} onChange={e=>setQuery(e.target.value)} placeholder={`Filter ${def.title.toLowerCase()}...`}/></div>{type==='products'&&<button className="button button-quiet" onClick={()=>{if(demandSorted){setDemandSorted(false);return;}const output=recordRun('products','randomized-quicksort',{values:db.products.map(p=>p.demand)},'Randomized QuickSort ranks demand scores before stock planning.');const order=[...(output?.result?.sorted||[])].reverse().map(value=>db.products.find(p=>p.demand===value)?.id);setDemandOrder(order);setDemandSorted(true);}}>↕ {demandSorted?'Default order':'Rank by demand'}</button>}</div>
      <div className="table-wrap"><table><thead><tr>{columns.map(([key,label])=><th key={key}>{label}</th>)}<th className="action-col">Actions</th></tr></thead><tbody>{shown.map(row=><tr key={row.id}>{columns.map(([key])=><td key={key}>{type==='products'&&key==='name'?<div className="product-cell"><span className="product-swatch">{row.name.slice(0,1)}</span><span><b>{row.name}</b><small>{row.id} · {row.unit}</small></span></div>:key==='status'?<Status value={row[key]}/>:key==='demand'?<span className="demand-badge">{row[key]} / 100</span>:key==='stock'?<b>{row[key]} {row.unit}</b>:key==='price'?money(row[key]):key==='score'?<b className={row.score<75?'score-warning':''}>{row[key]} / 100</b>:key==='products'&&type==='farmers'?<span className="soft-tag">{row[key]}</span>:row[key]}</td>)}<td className="action-col"><div className="row-actions">{type==='qualityReports'&&<button title="Scan report" onClick={()=>scanQuality(row)}>⌕</button>}<button title="Edit" onClick={()=>edit(row)}>✎</button><button title="Delete" className="danger-text" onClick={()=>remove(row)}>×</button></div></td></tr>)}</tbody></table>{!shown.length&&<div className="empty-table"><span>⌕</span><b>No records match</b><small>Try another filter or add a new record.</small></div>}</div>
      <div className="panel-foot"><span>Showing {shown.length} of {db[type].length} records</span><span>CSV includes each record’s full fields</span></div></div>
  </>;
}

function InventoryPage({db,setDb,inventoryProduct,setInventoryProduct,inventoryWarehouse,setInventoryWarehouse,inventoryQty,setInventoryQty,onAnalytics,related}) {
  const available=db.inventory.reduce((s,i)=>s+Math.max(0,i.quantity-i.reserved),0); const low=db.inventory.filter(i=>i.quantity-i.reserved<=i.reorderAt);
  const receive=()=>{const qty=Number(inventoryQty);if(!qty||qty<1)return;setDb(prev=>{const found=prev.inventory.find(i=>i.productId===inventoryProduct&&i.warehouseId===inventoryWarehouse);const next=found?prev.inventory.map(i=>i.id===found.id?{...i,quantity:i.quantity+qty,updated:'Just now'}:i):[{id:newId('I'),productId:inventoryProduct,warehouseId:inventoryWarehouse,quantity:qty,reserved:0,reorderAt:40,updated:'Just now'},...prev.inventory];return {...prev,products:prev.products.map(p=>p.id===inventoryProduct?{...p,stock:Number(p.stock||0)+qty}:p),inventory:next,inventoryEvents:[{id:newId('EV'),type:'Stock received',productId:inventoryProduct,warehouseId:inventoryWarehouse,quantity:qty,at:'Just now'},...prev.inventoryEvents]};});};
  return <><Heading eyebrow="OPERATIONS · LIVE STOCK" title="Inventory" subtitle="On-hand, reserved and reorder levels across each storage location." action={<button className="button button-primary" onClick={onAnalytics}>▦ Run inventory analytics</button>}/>
    <div className="inventory-summary"><div className="summary-tile"><span className="summary-icon mint">▦</span><small>Available units</small><b>{available.toLocaleString()} <i>kg</i></b><span>After reservations</span></div><div className="summary-tile"><span className="summary-icon amber">!</span><small>Reorder alerts</small><b>{low.length} <i>items</i></b><span>At or below reorder point</span></div><div className="summary-tile"><span className="summary-icon blue">↻</span><small>Events captured</small><b>{db.inventoryEvents.length} <i>events</i></b><span>Receipts and order reservations</span></div></div>
    <div className="dashboard-grid inventory-layout"><section className="panel"><div className="panel-heading"><div><span className="eyebrow">STOCK BY LOCATION</span><h3>Inventory records</h3></div><span className="soft-tag">{db.inventory.length} SKUs</span></div><div className="table-wrap"><table><thead><tr><th>Product</th><th>Warehouse</th><th>On hand</th><th>Reserved</th><th>Available</th><th>Reorder point</th></tr></thead><tbody>{db.inventory.map(i=>{const p=db.products.find(x=>x.id===i.productId);return <tr key={i.id}><td><div className="product-cell"><span className="product-swatch">{p?.name?.[0]||'?'}</span><span><b>{productName(db,i.productId)}</b><small>{i.updated}</small></span></div></td><td>{warehouseName(db,i.warehouseId)}</td><td>{i.quantity} kg</td><td>{i.reserved} kg</td><td><b className={i.quantity-i.reserved<=i.reorderAt?'score-warning':''}>{i.quantity-i.reserved} kg</b></td><td>{i.reorderAt} kg</td></tr>})}</tbody></table></div></section>
    <div className="inventory-side"><section className="panel receive-panel"><div className="panel-heading"><div><span className="eyebrow">STOCK INTAKE</span><h3>Receive produce</h3></div><span className="receive-badge"><i/> LIVE</span></div><p>Record an intake and add an event to the inventory stream.</p><div className="receive-form"><label>Product<select value={inventoryProduct} onChange={e=>setInventoryProduct(e.target.value)}>{db.products.map(p=><option value={p.id} key={p.id}>{p.name}</option>)}</select></label><label>Warehouse<select value={inventoryWarehouse} onChange={e=>setInventoryWarehouse(e.target.value)}>{db.warehouses.map(w=><option value={w.id} key={w.id}>{w.name}</option>)}</select></label><label>Quantity (kg)<input type="number" min="1" value={inventoryQty} onChange={e=>setInventoryQty(e.target.value)}/></label><button className="button button-primary" onClick={receive}>＋ Record intake</button></div></section>
    <section className="panel"><div className="panel-heading"><div><span className="eyebrow">RECENT EVENTS</span><h3>Inventory stream</h3></div><span className="soft-tag">{db.inventoryEvents.length} total</span></div><div className="event-list">{db.inventoryEvents.slice(0,5).map(e=><div className="event-row" key={e.id}><span className={e.quantity>0?'event-plus':'event-minus'}>{e.quantity>0?'+':'−'}</span><div><b>{e.type}</b><small>{productName(db,e.productId)} · {warehouseName(db,e.warehouseId)}</small></div><strong>{Math.abs(e.quantity)} kg</strong></div>)}</div></section></div></div>
    <ModuleAlgorithm title="Blelloch scan · parallel reduce · reservoir sampling" body="Stock rows become available quantities, then scan produces cumulative totals, reduce reports totals and peaks, and reservoir sampling fairly samples the live event stream." onClick={onAnalytics}/>
    <RelatedAlgorithms items={related}/>
  </>;
}

function OrdersPage({db,setBudget,budget,recordRun,addOrder}) {
  const [picked,setPicked]=useState([]);
  const assortment=()=>{const products=(picked.length?db.products.filter(p=>picked.includes(p.id)):db.products).map(p=>({name:p.name,cost:p.price}));return recordRun('orders','bitmask-dp',{budget:Number(budget),products},'Bitmask DP evaluates affordable product combinations and returns the largest bundle under the selected budget.');};
  return <><Heading eyebrow="OPERATIONS · CUSTOMER DEMAND" title="Orders" subtitle="Create customer orders, reserve inventory and review the product mix." action={<button className="button button-primary" onClick={addOrder}><span>＋</span> Create order</button>}/>
    <div className="dashboard-grid orders-layout"><section className="panel"><div className="panel-heading"><div><span className="eyebrow">ORDER BOOK</span><h3>Customer orders</h3></div><span className="soft-tag">{db.orders.length} records</span></div><div className="table-wrap"><table><thead><tr><th>Order</th><th>Customer</th><th>Items</th><th>Total</th><th>Created</th><th>Status</th></tr></thead><tbody>{db.orders.map(o=><tr key={o.id}><td><b>{o.id}</b></td><td>{o.customer}</td><td>{o.items}</td><td><b>{money(o.total)}</b></td><td>{o.created}</td><td><Status value={o.status}/></td></tr>)}</tbody></table></div></section>
    <section className="panel assortment-panel"><div className="panel-heading"><div><span className="eyebrow">SMART ASSORTMENT</span><h3>Fit more value in budget</h3></div><span className="stat-icon green">⌘</span></div><p>Pick a budget and let the subset planner find a useful combination from your catalog.</p><label className="budget-label">Budget in ₹<input type="number" min="1" value={budget} onChange={e=>setBudget(e.target.value)}/></label><button className="button button-primary full-button" onClick={assortment}>Run assortment DP <span>↗</span></button><div className="assortment-products">{db.products.map(p=><label key={p.id}><input type="checkbox" checked={picked.includes(p.id)} onChange={e=>setPicked(e.target.checked?[...picked,p.id]:picked.filter(id=>id!==p.id))}/><span>{p.name}</span><b>{money(p.price)}</b></label>)}</div><small className="helper-copy">When products are checked, DP plans from that subset; otherwise it uses the full catalog.</small></section></div>
    <ModuleAlgorithm title="Bitmask Dynamic Programming assortment planner" body="Each bit represents whether a product is selected. DP checks combinations against the budget, then returns the highest-value affordable bundle." onClick={assortment}/>
  </>;
}

function ShipmentsPage({db,changeShipment}) {
  return <><Heading eyebrow="OPERATIONS · COLD CHAIN" title="Shipments" subtitle="Track dispatch progress, delivery windows and carrier updates." action={<span className="soft-tag">{db.shipments.filter(s=>s.status==='In transit').length} moving now</span>}/>
    <div className="shipment-grid">{db.shipments.map(s=><article className="panel shipment-card" key={s.id}><div className="shipment-card-head"><div><span className="eyebrow">{s.id} · ORDER {s.orderId}</span><h3>{s.destination}</h3></div><Status value={s.status}/></div><div className="shipment-visual"><div className="shipment-line"><span style={{width:`${s.progress}%`}}/></div><div className="shipment-stops"><span className="stop-done">Farm pickup</span><span className={s.progress>40?'stop-done':''}>Distribution hub</span><span className={s.progress===100?'stop-done':''}>Customer</span></div></div><div className="shipment-facts"><div><small>Carrier</small><b>{s.carrier}</b></div><div><small>Estimated arrival</small><b>{s.eta}</b></div><div><small>Order items</small><b>{db.orders.find(o=>o.id===s.orderId)?.items||'—'}</b></div></div><div className="shipment-actions"><label>Update status<select value={s.status} onChange={e=>changeShipment(s,e.target.value)}>{['Preparing','In transit','Delayed','Delivered'].map(x=><option key={x}>{x}</option>)}</select></label><span>{s.progress}% complete</span></div></article>)}{!db.shipments.length&&<div className="empty-state"><span>↗</span><b>No shipments yet</b><p>Creating a customer order also creates its shipment record.</p></div>}</div>
    <div className="panel shipment-note"><span className="summary-icon blue">↗</span><div><b>Order-to-shipment workflow</b><p>When you place an order, available stock is reduced, an inventory event is added and a linked shipment is created automatically.</p></div><div className="shipment-flow"><div><span>1</span><b>Order placed</b><small>Stock reserved</small></div><i>→</i><div><span>2</span><b>Dispatch ready</b><small>Carrier assigned</small></div><i>→</i><div><span>3</span><b>Delivered</b><small>Status tracked</small></div></div></div>
  </>;
}

function DistributionPage({db,plan,routePlan,related}) {
  const flow=routePlan?.flow?.result; const matching=routePlan?.matching?.result;
  return <><Heading eyebrow="PLANNING · NETWORK CAPACITY" title="Distribution planner" subtitle="Model food movement through hubs and see where capacity is tight." action={<button className="button button-primary" onClick={plan}><span>⌘</span> Optimize network</button>}/>
    <div className="planner-layout"><section className="panel network-panel"><div className="panel-heading"><div><span className="eyebrow">SUPPLY NETWORK</span><h3>Farm → warehouse → market</h3></div><span className="soft-tag">Capacity in kg / day</span></div><div className="distribution-network"><div className="network-node farm-node"><span>♧</span><b>Farm supply</b><small>570 kg available</small></div><div className="network-branches"><div><i>→</i><span>320 kg</span><div className="network-node"><span>▤</span><b>Nashik Center</b><small>{db.warehouses[2]?.used||780} / {db.warehouses[2]?.capacity||1200} kg</small></div></div><div><i>→</i><span>250 kg</span><div className="network-node"><span>▤</span><b>Pune Depot</b><small>{db.warehouses[1]?.used||620} / {db.warehouses[1]?.capacity||950} kg</small></div></div></div><div className="network-node hub-node"><span>▤</span><b>Mumbai Hub</b><small>{db.warehouses[0]?.used||1030} / {db.warehouses[0]?.capacity||1600} kg</small></div><div className="network-node market-node"><span>⌂</span><b>Markets</b><small>Delivery demand</small></div></div><div className="network-legend"><span><i className="green-dot"/> Active route</span><span><i className="amber-dot"/> Capacity may bind</span><span>Flow values are demo capacities and can be edited in Warehouses</span></div></section>
    <section className="panel planner-side"><div className="panel-heading"><div><span className="eyebrow">PLANNER OUTPUT</span><h3>Capacity and assignments</h3></div><span className="stat-icon green">⌘</span></div>{routePlan?<><div className="flow-output"><small>MAX FLOW TO MARKET</small><b>{flow?.maxFlow??flow?.flow??'—'} <i>kg/day</i></b><span>{routePlan.flow.headline}</span></div><div className="matching-output"><small>SUPPLIER ↔ WAREHOUSE MATCHES</small>{(matching?.matches||matching?.assignments||[]).length? (matching.matches||matching.assignments).map((m,i)=><div key={i}><b>{typeof m==='object'?(m.supplier||m.left||m[0]||`Supplier ${i+1}`):m}</b><span>→</span><b>{typeof m==='object'?(m.warehouse||m.right||m[1]||`Warehouse ${i+1}`):''}</b></div>):<p>{routePlan.matching.headline}</p>}</div><div className="cut-list"><b>Route bottlenecks</b><p>Inspect the saturated edges in the result details to find the minimum cut.</p></div></>:<div className="planner-empty"><div>⌘</div><b>Run your first network plan</b><p>Dinic computes max flow and Bipartite Matching assigns suppliers to compatible warehouses.</p><button className="button button-primary" onClick={plan}>Optimize network <span>↗</span></button></div>}</section></div>
    <div className="module-algorithm planner-algo"><div className="module-algo-icon">⌘</div><div className="module-algo-copy"><span className="eyebrow">ALGORITHMS USED IN THIS MODULE</span><b>Dinic max-flow + Bipartite Matching + min-cut analysis</b><p>Dinic sends the largest possible volume through capacity-constrained routes. Matching assigns suppliers to eligible hubs; a min-cut identifies the bottleneck links.</p></div><button className="button button-primary" onClick={plan}>Run planner <span>↗</span></button></div>
    <RelatedAlgorithms items={related}/>
  </>;
}

function DsaMap({setActive}) {
  const rows=[
    ['Products','Search product names, IDs and descriptions','KMP · Z-Function · Rabin–Karp · Levenshtein','Product search checks several fields; edit-distance can suggest the closest spelling.','products'],
    ['Products / Quality','Index long descriptions and find repeated phrases','SA-IS · Kasai LCP · Suffix Tree / Automaton','Text indexes support repeated-description checks and many substring searches.','products'],
    ['Quality reports','Flag food-safety language in inspection notes','Aho–Corasick','One multi-pattern scan finds warning terms such as mold, contamination and temperature.','qualityReports'],
    ['Orders','Find a useful product bundle within a budget','Bitmask DP','Each subset is a state; the result shows an affordable assortment.','orders'],
    ['Inventory','Compute stock totals and inspect the event stream','Blelloch Scan · Parallel Reduce · Reservoir Sampling','Prefix totals, chunk summaries and fair stream samples help demonstrate analytics.','inventory'],
    ['Distribution','Send the most produce through the warehouse network','Dinic · Ford–Fulkerson · Edmonds–Karp · Max-Flow Min-Cut','Max-flow estimates capacity; min-cut points to constrained edges.','distribution'],
    ['Suppliers / Warehouses','Assign suppliers to eligible storage locations','Bipartite Matching','An augmenting-path match creates one-to-one compatible assignments.','distribution'],
    ['Planning constraints','Explain conflicts between routes and resource choices','3-SAT → CLIQUE → Independent Set → Vertex Cover','Small sample constraints show reduction witnesses and a 2-approximate critical hub cover.','distribution'],
    ['Products / Analytics','Rank demand and estimate scalable processing','Randomized QuickSort · Matrix-Chain · Optimal BST · Brent · Miller–Rabin','Use demand order, data-operation order, lookup frequencies and parallel runtime estimates.','products'],
  ];
  return <><Heading eyebrow="PROJECT GUIDE · WHERE THE DSA LIVES" title="Algorithms in FoodChain" subtitle="The modules hold the business records. These are the algorithms used behind the module actions." action={<span className="soft-tag">24 concepts · module-linked</span>}/><div className="dsa-intro"><span>⌗</span><div><b>Start with the module, then inspect its algorithm.</b><p>Open a module and run its search, scan or planning action. The result drawer explains the algorithm, why it fits and its execution trace.</p></div></div><div className="dsa-map-list">{rows.map(([module,purpose,algo,why,target],i)=><article className="dsa-map-row" key={module+purpose}><div className="dsa-index">{String(i+1).padStart(2,'0')}</div><div className="dsa-module"><span className="eyebrow">MODULE</span><b>{module}</b><small>{purpose}</small></div><div className="dsa-algorithms"><span className="eyebrow">ALGORITHM USED</span><b>{algo}</b><p>{why}</p></div><button className="button button-quiet" onClick={()=>setActive(target)}>Open module <span>↗</span></button></article>)}</div><div className="dsa-footnote"><b>What actually runs</b><p>Algorithm actions run against the FoodChain sample records or a small capacity model. New business data is saved in browser storage and included in CSV export/import.</p></div></>;
}

function RecordModal({modal,db,close,save,update,createOrder}) {
  const isOrder=modal.type==='newOrder'; const def=definitions[modal.type];
  const typeLabel={farmers:'farmer',suppliers:'supplier',products:'product',warehouses:'warehouse',qualityReports:'quality report'}[modal.type];
  const [values,setValues]=useState(()=>modal.row?{...modal.row}:isOrder?{customer:'',productId:db.products[0]?.id||'',quantity:1,warehouseId:db.warehouses[0]?.id||'',destination:''}:Object.fromEntries(def.fields.map(([key,,kind,options])=>[key,kind==='select'?options[0]:key==='score'?90:''])));
  const set=(key,value)=>setValues(prev=>({...prev,[key]:value}));
  const submit=e=>{e.preventDefault();if(isOrder)createOrder(values);else if(modal.row)update(modal.type,{...modal.row,...values});else save(modal.type,values);};
  const fields=isOrder?[]:def.fields;
  return <div className="modal-backdrop" onMouseDown={e=>e.target===e.currentTarget&&close()}><form className="modal-card" onSubmit={submit}><div className="modal-heading"><div><span className="eyebrow">FOODCHAIN · {isOrder?'OPERATIONS':def.title.toUpperCase()}</span><h2>{isOrder?'Create customer order':`${modal.row?'Edit':'Add'} ${typeLabel}`}</h2></div><button type="button" onClick={close}>×</button></div><div className="modal-fields">{isOrder?<><Field label="Customer name"><input required value={values.customer} onChange={e=>set('customer',e.target.value)} placeholder="e.g. Green Plate Cafe"/></Field><Field label="Product"><select value={values.productId} onChange={e=>set('productId',e.target.value)}>{db.products.map(p=><option value={p.id} key={p.id}>{p.name} · {money(p.price)}/{p.unit}</option>)}</select></Field><Field label="Quantity"><input required type="number" min="1" value={values.quantity} onChange={e=>set('quantity',e.target.value)}/></Field><Field label="Fulfillment warehouse"><select value={values.warehouseId} onChange={e=>set('warehouseId',e.target.value)}>{db.warehouses.map(w=><option value={w.id} key={w.id}>{w.name}</option>)}</select></Field><Field label="Delivery destination" wide><input required value={values.destination} onChange={e=>set('destination',e.target.value)} placeholder="Area, city"/></Field></>:fields.map(([key,label,kind,options])=><Field label={label} wide={kind==='textarea'} key={key}>{kind==='select'?<select value={values[key]||options[0]} onChange={e=>set(key,e.target.value)}>{options.map(o=><option key={o}>{o}</option>)}</select>:kind==='textarea'?<textarea rows="3" required value={values[key]||''} onChange={e=>set(key,e.target.value)}/>:<input required type={kind==='number'?'number':'text'} step={kind==='number'?'any':undefined} value={values[key]??''} onChange={e=>set(key,e.target.value)}/>}</Field>)}</div><div className="modal-actions"><button type="button" className="button button-quiet" onClick={close}>Cancel</button><button className="button button-primary" type="submit">{isOrder?'Place order':modal.row?'Save changes':'Add record'}</button></div></form></div>;
}
function Field({label,wide,children}) { return <label className={wide?'span-two':''}>{label}{children}</label>; }

export default App;

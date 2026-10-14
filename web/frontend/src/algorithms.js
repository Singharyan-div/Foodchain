const groups = [
  { id: 'strings', title: 'String algorithms', course: 'M1 · CO1', icon: 'Aa' },
  { id: 'suffix', title: 'Suffix structures', course: 'M2 · CO2', icon: '▤' },
  { id: 'dp', title: 'Dynamic programming', course: 'M3 · CO3', icon: '▦' },
  { id: 'flow', title: 'Network flow', course: 'M4 · CO4', icon: '⌁' },
  { id: 'np', title: 'NP-completeness', course: 'M5 · CO5', icon: '◇' },
  { id: 'random', title: 'Randomized & parallel', course: 'M6 · CO6', icon: '⤨' },
];

const algorithms = [
  { id: 'kmp', group: 'strings', name: 'KMP Search', tag: 'Pattern matching', complexity: 'O(n + m)', about: 'Find every occurrence of a quality phrase without rechecking matched characters.', use: 'Search product descriptions and food-quality terms.', sample: { text: 'Fresh red apples from Nashik orchards; apples are sorted daily.', pattern: 'apples' } },
  { id: 'z-function', group: 'strings', name: 'Z-Function', tag: 'Pattern matching', complexity: 'O(n + m)', about: 'Build a Z-array and use it to find all positions where a pattern starts.', use: 'Find repeated description fragments in product records.', sample: { text: 'fresh apple fresh mango fresh apple', pattern: 'fresh' } },
  { id: 'rabin-karp', group: 'strings', name: 'Rabin–Karp', tag: 'Rolling hash', complexity: 'Expected O(n + m)', about: 'Compare rolling hashes of windows, then verify matching windows character by character.', use: 'Search product SKUs and shipment tracking codes.', sample: { text: 'FC-AP-204 | FC-MG-118 | FC-AP-204', pattern: 'FC-AP-204' } },
  { id: 'aho-corasick', group: 'strings', name: 'Aho–Corasick', tag: 'Multiple patterns', complexity: 'O(text + matches)', about: 'Scan one report with a trie and failure links to find many warning terms at once.', use: 'Detect temperature, damage, mold, and contamination warnings.', sample: { text: 'Temperature high; damaged packaging. Inspect for mold.', keywords: ['temperature', 'damaged', 'mold', 'spoiled', 'contaminated'] } },
  { id: 'suffix-array', group: 'suffix', name: 'SA-IS Suffix Array', tag: 'Document indexing', complexity: 'O(n)', about: 'Sort every text suffix with induced sorting, then show the longest repeated phrase.', use: 'Index long product descriptions and quality documents.', sample: { text: 'fresh apple fresh mango fresh apple' } },
  { id: 'kasai-lcp', group: 'suffix', name: 'Kasai LCP', tag: 'Repeated text', complexity: 'O(n)', about: 'Compute adjacent suffix longest-common-prefix values using the suffix array.', use: 'Identify repeated product-description fragments.', sample: { text: 'banana bandana banana' } },
  { id: 'suffix-tree', group: 'suffix', name: 'Compact Suffix Tree', tag: 'Substring queries', complexity: 'O(n²) teaching build', about: 'Build a compacted suffix trie and trace substring queries along labeled edges.', use: 'Answer repeated substring checks inside a short product or quality document.', sample: { text: 'banana bandana', queries: ['ana', 'band', 'mango'] } },
  { id: 'suffix-automaton', group: 'suffix', name: 'Suffix Automaton', tag: 'Substring queries', complexity: 'O(n) build', about: 'Build a compact state machine that answers many substring-existence queries.', use: 'Check whether requested terms occur in a stored report.', sample: { text: 'nashik red apples are fresh', queries: ['apples', 'fresh', 'mango'] } },
  { id: 'edit-distance', group: 'dp', name: 'Levenshtein & Damerau', tag: 'Dynamic programming', complexity: 'O(m × n)', about: 'Compare names with insertion, deletion, replacement, and adjacent-transposition costs.', use: 'Suggest corrections for misspelled supplier and product names.', sample: { first: 'Tomato', second: 'Tomtao' } },
  { id: 'bitmask-dp', group: 'dp', name: 'Bitmask Assortment DP', tag: 'Subset planning', complexity: 'O(2ⁿ)', about: 'Enumerate product subsets and find the largest assortment that fits a budget.', use: 'Choose a product bundle when funds or truck space are limited.', sample: { budget: 300, products: [{ name: 'Apples', cost: 120 }, { name: 'Tomatoes', cost: 45 }, { name: 'Rice', cost: 72 }, { name: 'Mangoes', cost: 160 }] } },
  { id: 'matrix-chain', group: 'dp', name: 'Matrix-Chain Order', tag: 'Dynamic programming', complexity: 'O(n³)', about: 'Choose the cheapest parenthesization for a sequence of matrix operations.', use: 'Reduce the cost of multi-stage supply-data processing.', sample: { dimensions: [10, 30, 5, 60] } },
  { id: 'optimal-bst', group: 'dp', name: 'Optimal Binary Search Tree', tag: 'Search optimization', complexity: 'O(n³)', about: 'Place frequently searched keys near the root using successful and miss frequencies.', use: 'Optimize lookup order for frequently searched products.', sample: { keys: ['Apples', 'Rice', 'Tomatoes'], successful: [34, 8, 50], unsuccessful: [5, 4, 3, 2] } },
  { id: 'ford-fulkerson', group: 'flow', name: 'Ford–Fulkerson', tag: 'Maximum flow', complexity: 'O(E × max-flow)', about: 'Repeatedly find an augmenting path and push as much supply as its bottleneck allows.', use: 'Model food moving from farms to markets through capacity-limited routes.', sample: { nodes: ['Farm', 'Hub A', 'Hub B', 'Market'], source: 'Farm', sink: 'Market', edges: [{ from: 'Farm', to: 'Hub A', capacity: 8 }, { from: 'Farm', to: 'Hub B', capacity: 5 }, { from: 'Hub A', to: 'Market', capacity: 6 }, { from: 'Hub B', to: 'Market', capacity: 7 }, { from: 'Hub A', to: 'Hub B', capacity: 3 }] } },
  { id: 'edmonds-karp', group: 'flow', name: 'Edmonds–Karp', tag: 'Maximum flow', complexity: 'O(V × E²)', about: 'Use BFS to choose the shortest augmenting path in each max-flow iteration.', use: 'Analyze distribution capacity with predictable polynomial runtime.', sample: { nodes: ['Farm', 'Hub A', 'Hub B', 'Market'], source: 'Farm', sink: 'Market', edges: [{ from: 'Farm', to: 'Hub A', capacity: 8 }, { from: 'Farm', to: 'Hub B', capacity: 5 }, { from: 'Hub A', to: 'Market', capacity: 6 }, { from: 'Hub B', to: 'Market', capacity: 7 }, { from: 'Hub A', to: 'Hub B', capacity: 3 }] } },
  { id: 'dinic', group: 'flow', name: 'Dinic', tag: 'Maximum flow', complexity: 'O(V² × E)', about: 'Build level graphs and send blocking flows to move supply through larger networks.', use: 'Plan high-volume distribution and locate min-cut bottlenecks.', sample: { nodes: ['Farm', 'Hub A', 'Hub B', 'Market'], source: 'Farm', sink: 'Market', edges: [{ from: 'Farm', to: 'Hub A', capacity: 12 }, { from: 'Farm', to: 'Hub B', capacity: 8 }, { from: 'Hub A', to: 'Hub B', capacity: 5 }, { from: 'Hub A', to: 'Market', capacity: 9 }, { from: 'Hub B', to: 'Market', capacity: 10 }] } },
  { id: 'bipartite-matching', group: 'flow', name: 'Bipartite Matching', tag: 'Resource assignment', complexity: 'O(V × E)', about: 'Use augmenting paths to match each supplier to at most one eligible warehouse.', use: 'Assign suppliers to warehouses that can handle their products.', sample: { suppliers: ['Farm A', 'Farm B', 'Farm C'], warehouses: ['Mumbai', 'Pune'], eligible: [[0, 0], [0, 1], [1, 0], [2, 1]] } },
  { id: '3sat-reductions', group: 'np', name: '3-SAT → CLIQUE → IS → VC', tag: 'Constraint reduction', complexity: 'Exponential search', about: 'Solve a small 3-CNF instance and construct the standard graph-reduction witness chain.', use: 'Demonstrate how supply constraints relate to graph problems.', sample: { variables: 3, clauses: [[1, -2, 3], [-1, 2, 3], [1, 2, -3]] } },
  { id: 'vertex-cover', group: 'np', name: 'Vertex-Cover 2-Approximation', tag: 'Approximation', complexity: 'O(V + E)', about: 'Pick both endpoints of each uncovered edge; the resulting cover is at most twice optimum.', use: 'Find a small set of critical hubs covering all conflict routes.', sample: { locations: ['North hub', 'City depot', 'Cold store', 'Market'], edges: [[0, 1], [1, 2], [2, 3], [0, 3]] } },
  { id: 'randomized-quicksort', group: 'random', name: 'Randomized QuickSort', tag: 'Randomized sorting', complexity: 'Expected O(n log n)', about: 'Choose random pivots and partition demand values into less-than, equal, and greater groups.', use: 'Rank products by demand before distribution.', sample: { values: [85, 120, 65, 180, 120, 45] } },
  { id: 'reservoir-sampling', group: 'random', name: 'Reservoir Sampling', tag: 'Streaming data', complexity: 'O(stream)', about: 'Keep a uniform sample of k records while reading a stream of unknown length.', use: 'Inspect a fair sample of a continuous inventory-event stream.', sample: { values: ['Stock receipt #101', 'Order #204', 'Quality check #87', 'Shipment #55', 'Stock correction #19', 'Order #205', 'Shipment #56'], k: 3 } },
  { id: 'miller-rabin', group: 'random', name: 'Miller–Rabin', tag: 'Primality test', complexity: 'O(k log³ n)', about: 'Use modular exponentiation witnesses to test whether a large integer is probably prime.', use: 'Demonstrate fast large-number checks used in numerical processing.', sample: { number: '18446744073709551557' } },
  { id: 'blelloch-scan', group: 'random', name: 'Blelloch Scan', tag: 'Prefix totals', complexity: 'O(n) work', about: 'Run an up-sweep and down-sweep tree to compute every prefix inventory total.', use: 'Calculate cumulative inventory levels efficiently.', sample: { values: [12, 8, 15, 5, 9] } },
  { id: 'parallel-reduce', group: 'random', name: 'Parallel Reduce', tag: 'Supply statistics', complexity: 'O(n) work', about: 'Reduce chunks into partial summaries, then combine totals, maximum, and average.', use: 'Summarize large sets of inventory quantities.', sample: { values: [12, 8, 15, 5, 9, 21, 4, 11, 7], workers: 3 } },
  { id: 'brent-theorem', group: 'random', name: 'Brent’s Theorem', tag: 'Parallel runtime', complexity: 'O(1)', about: 'Estimate parallel time bounds from total work, critical path, and processor count.', use: 'Reason about parallel supply-chain processing time.', sample: { work: 1000, criticalPath: 120, processors: 8 } },
];

function requireArray(value, label) {
  if (!Array.isArray(value)) throw new Error(label + ' must be a JSON array.');
  return value;
}
function requireText(value, label) {
  if (typeof value !== 'string' || !value.length) throw new Error(label + ' is required.');
  return value;
}
function prefixTable(pattern) {
  const lps = Array(pattern.length).fill(0);
  for (let i = 1, length = 0; i < pattern.length;) {
    if (pattern[i] === pattern[length]) lps[i++] = ++length;
    else if (length) length = lps[length - 1];
    else lps[i++] = 0;
  }
  return lps;
}
function kmp(text, pattern) {
  requireText(text, 'Text'); requireText(pattern, 'Pattern');
  const lps = prefixTable(pattern), matches = [], trace = ['Build the longest-prefix/suffix table: ' + JSON.stringify(lps) + '.'];
  let i = 0, j = 0, comparisons = 0;
  while (i < text.length) {
    comparisons++;
    if (text[i] === pattern[j]) {
      i++; j++;
      if (j === pattern.length) {
        matches.push(i - j);
        trace.push('Match at index ' + (i - j) + '; use LPS[' + (j - 1) + '] to continue without restarting.');
        j = lps[j - 1];
      }
    } else if (j > 0) {
      trace.push('Mismatch at text[' + i + ']; shift pattern using LPS from ' + j + ' to ' + lps[j - 1] + '.');
      j = lps[j - 1];
    } else i++;
  }
  if (!matches.length) trace.push('The scan finished without a match.');
  return pack('Found ' + matches.length + ' match' + (matches.length === 1 ? '' : 'es') + '.', { matches, prefixTable: lps, comparisons }, trace, 'O(n + m) time · O(m) space');
}
function zArray(sequence) {
  const z = Array(sequence.length).fill(0);
  let left = 0, right = 0;
  for (let i = 1; i < sequence.length; i++) {
    if (i <= right) z[i] = Math.min(right - i + 1, z[i - left]);
    while (i + z[i] < sequence.length && sequence[z[i]] === sequence[i + z[i]]) z[i]++;
    if (i + z[i] - 1 > right) { left = i; right = i + z[i] - 1; }
  }
  return z;
}
function zSearch(text, pattern) {
  requireText(text, 'Text'); requireText(pattern, 'Pattern');
  const patternChars = Array.from(pattern), joined = patternChars.concat([-1], Array.from(text)), z = zArray(joined);
  const matches = [];
  for (let i = patternChars.length + 1; i < joined.length; i++) if (z[i] >= patternChars.length) matches.push(i - patternChars.length - 1);
  return pack('Found ' + matches.length + ' match' + (matches.length === 1 ? '' : 'es') + '.', { matches, zArray: z }, ['Join pattern, a unique separator, and text; the separator prevents matches from crossing the boundary.', 'Z[i] is the common-prefix length at each position. Values at least ' + pattern.length + ' mark pattern occurrences.'], 'O(n + m) time · O(n + m) space');
}
function rabinKarp(text, pattern) {
  requireText(text, 'Text'); requireText(pattern, 'Pattern');
  const base = 256, mod = 1000000007, m = pattern.length, matches = [], windows = [];
  if (m > text.length) return pack('No window can fit the pattern.', { matches, checkedWindows: 0 }, ['Pattern is longer than the text.'], 'O(n + m) expected · O(1) extra space');
  let power = 1, pHash = 0, wHash = 0;
  for (let i = 1; i < m; i++) power = (power * base) % mod;
  for (let i = 0; i < m; i++) { pHash = (pHash * base + pattern.charCodeAt(i)) % mod; wHash = (wHash * base + text.charCodeAt(i)) % mod; }
  for (let start = 0; start <= text.length - m; start++) {
    const hashMatch = pHash === wHash;
    const exactMatch = hashMatch && text.slice(start, start + m) === pattern;
    windows.push({ start, hashMatch, exactMatch });
    if (exactMatch) matches.push(start);
    if (start < text.length - m) {
      wHash = ((wHash - text.charCodeAt(start) * power) % mod + mod) % mod;
      wHash = (wHash * base + text.charCodeAt(start + m)) % mod;
    }
  }
  return pack('Found ' + matches.length + ' exact match' + (matches.length === 1 ? '' : 'es') + '.', { matches, hashMatches: windows.filter(x => x.hashMatch).length, windows }, ['Hash the pattern and first text window, then slide the rolling hash one character at a time.', 'A matching hash is verified against the characters so collisions do not create false results.'], 'O(n + m) expected · O(n) trace');
}
function ahoCorasick(text, keywords) {
  requireText(text, 'Text'); requireArray(keywords, 'Keywords');
  const clean = [...new Set(keywords.filter(x => typeof x === 'string' && x.trim()).map(x => x.toLowerCase()))];
  if (!clean.length) throw new Error('Add at least one keyword.');
  const nodes = [{ next: new Map(), fail: 0, out: [] }];
  clean.forEach(word => {
    let at = 0;
    for (const ch of word) {
      if (!nodes[at].next.has(ch)) { nodes[at].next.set(ch, nodes.length); nodes.push({ next: new Map(), fail: 0, out: [] }); }
      at = nodes[at].next.get(ch);
    }
    nodes[at].out.push(word);
  });
  const queue = [];
  for (const child of nodes[0].next.values()) queue.push(child);
  for (let head = 0; head < queue.length; head++) {
    const parent = queue[head];
    for (const [ch, child] of nodes[parent].next) {
      let fallback = nodes[parent].fail;
      while (fallback && !nodes[fallback].next.has(ch)) fallback = nodes[fallback].fail;
      nodes[child].fail = nodes[fallback].next.get(ch) ?? 0;
      nodes[child].out.push(...nodes[nodes[child].fail].out);
      queue.push(child);
    }
  }
  let state = 0; const matches = [];
  for (let i = 0; i < text.length; i++) {
    const ch = text[i].toLowerCase();
    while (state && !nodes[state].next.has(ch)) state = nodes[state].fail;
    state = nodes[state].next.get(ch) ?? 0;
    for (const word of nodes[state].out) matches.push({ keyword: word, start: i - word.length + 1 });
  }
  return pack(matches.length + ' warning hit' + (matches.length === 1 ? '' : 's') + ' across ' + clean.length + ' keywords.', { matches, trieStates: nodes.length }, ['Insert all warning words into one trie, then build failure links with breadth-first search.', 'Scan the report once; failure links reuse the longest suffix that is also a trie prefix.'], 'O(text + total keywords + matches)');
}

function saIs(s, upper) {
  const n = s.length;
  if (!n) return [];
  if (n === 1) return [0];
  if (n === 2) return s[0] < s[1] ? [0, 1] : [1, 0];
  if (n < 24) return Array.from({ length: n }, (_, i) => i).sort((a, b) => {
    let x = a, y = b;
    while (x < n && y < n && s[x] === s[y]) { x++; y++; }
    return x === n || y === n ? (n - a) - (n - b) : s[x] - s[y];
  });
  const sa = Array(n).fill(-1), isS = Array(n).fill(false);
  for (let i = n - 2; i >= 0; i--) isS[i] = s[i] === s[i + 1] ? isS[i + 1] : s[i] < s[i + 1];
  const sumL = Array(upper + 1).fill(0), sumS = Array(upper + 1).fill(0);
  for (let i = 0; i < n; i++) { if (!isS[i]) sumS[s[i]]++; else sumL[s[i] + 1]++; }
  for (let i = 0; i <= upper; i++) { sumS[i] += sumL[i]; if (i < upper) sumL[i + 1] += sumS[i]; }
  const induce = orderedLms => {
    sa.fill(-1);
    let buffer = sumS.slice();
    for (const d of orderedLms) if (d < n) sa[buffer[s[d]]++] = d;
    buffer = sumL.slice();
    sa[buffer[s[n - 1]]++] = n - 1;
    for (let i = 0; i < n; i++) { const v = sa[i]; if (v >= 1 && !isS[v - 1]) sa[buffer[s[v - 1]]++] = v - 1; }
    buffer = sumL.slice();
    for (let i = n - 1; i >= 0; i--) { const v = sa[i]; if (v >= 1 && isS[v - 1]) sa[--buffer[s[v - 1] + 1]] = v - 1; }
  };
  const lms = [];
  for (let i = 1; i < n; i++) if (!isS[i - 1] && isS[i]) lms.push(i);
  induce(lms);
  if (lms.length) {
    const lmsMap = Array(n).fill(-1);
    lms.forEach((position, i) => { lmsMap[position] = i; });
    const sortedLms = sa.filter(position => position >= 0 && lmsMap[position] !== -1);
    const reduced = Array(lms.length).fill(0); let reducedUpper = 0;
    reduced[lmsMap[sortedLms[0]]] = 0;
    for (let i = 1; i < lms.length; i++) {
      let previous = sortedLms[i - 1], current = sortedLms[i];
      const previousEnd = lmsMap[previous] + 1 < lms.length ? lms[lmsMap[previous] + 1] : n;
      const currentEnd = lmsMap[current] + 1 < lms.length ? lms[lmsMap[current] + 1] : n;
      let same = previousEnd - previous === currentEnd - current;
      if (same) {
        while (previous < previousEnd) {
          if (s[previous] !== s[current] || isS[previous] !== isS[current]) { same = false; break; }
          previous++; current++;
        }
        if (same && previousEnd < n && currentEnd < n) same = s[previousEnd] === s[currentEnd] && isS[previousEnd] === isS[currentEnd];
        else if (same && previousEnd !== currentEnd) same = false;
      }
      if (!same) reducedUpper++;
      reduced[lmsMap[sortedLms[i]]] = reducedUpper;
    }
    const reducedSa = saIs(reduced, reducedUpper);
    induce(reducedSa.map(i => lms[i]));
  }
  return sa;
}
function makeSuffixArray(text) {
  const chars = Array.from(text);
  const alphabet = [...new Set(chars)].sort((a, b) => a.codePointAt(0) - b.codePointAt(0));
  const rank = new Map(alphabet.map((ch, i) => [ch, i]));
  return { chars, suffixArray: saIs(chars.map(ch => rank.get(ch)), Math.max(0, alphabet.length - 1)) };
}
function kasai(chars, sa) {
  const n = chars.length, rank = Array(n).fill(0), lcp = Array(n).fill(0);
  sa.forEach((pos, i) => { rank[pos] = i; });
  let height = 0;
  for (let i = 0; i < n; i++) {
    const order = rank[i];
    if (order === 0) continue;
    const j = sa[order - 1];
    while (i + height < n && j + height < n && chars[i + height] === chars[j + height]) height++;
    lcp[order] = height;
    if (height) height--;
  }
  return lcp;
}
function suffixArrayRun(text) {
  requireText(text, 'Text');
  if (Array.from(text).length > 4000) throw new Error('Keep this visual demo to 4,000 characters or fewer.');
  const { chars, suffixArray } = makeSuffixArray(text), lcp = kasai(chars, suffixArray);
  const longest = Math.max(0, ...lcp), at = lcp.indexOf(longest);
  const phrase = longest ? chars.slice(suffixArray[at], suffixArray[at] + longest).join('') : '';
  const rows = suffixArray.slice(0, 200).map((position, order) => ({
    order, start: position,
    suffixPreview: chars.slice(position, position + 60).join('') + (chars.length - position > 60 ? '…' : ''),
    lcp: lcp[order],
  }));
  return pack(longest ? 'Longest repeated fragment: “' + phrase + '” (' + longest + ' characters).' : 'No repeated non-empty fragment was found.', { suffixes: rows, shownSuffixes: rows.length, totalSuffixes: chars.length, longestRepeated: phrase, longestLcp: longest }, ['SA-IS classifies suffix positions as S-type or L-type and identifies LMS positions.', 'Induced sorting orders LMS substrings, recursively names them, then induces the full suffix order.', 'Kasai compares adjacent suffixes to find the longest repeated prefix.', 'The result preview shows at most the first 200 suffixes.'], 'O(n) time · O(n) space');
}
function suffixAutomaton(text, queries) {
  requireText(text, 'Text'); requireArray(queries, 'Queries');
  const states = [{ len: 0, link: -1, next: new Map() }]; let last = 0;
  for (const ch of Array.from(text)) {
    const cur = states.length; states.push({ len: states[last].len + 1, link: 0, next: new Map() });
    let p = last;
    while (p !== -1 && !states[p].next.has(ch)) { states[p].next.set(ch, cur); p = states[p].link; }
    if (p === -1) states[cur].link = 0;
    else {
      const q = states[p].next.get(ch);
      if (states[p].len + 1 === states[q].len) states[cur].link = q;
      else {
        const clone = states.length; states.push({ len: states[p].len + 1, link: states[q].link, next: new Map(states[q].next) });
        while (p !== -1 && states[p].next.get(ch) === q) { states[p].next.set(ch, clone); p = states[p].link; }
        states[q].link = states[cur].link = clone;
      }
    }
    last = cur;
  }
  const checks = queries.map(query => {
    let state = 0;
    for (const ch of String(query)) { if (!states[state].next.has(ch)) return { query, found: false }; state = states[state].next.get(ch); }
    return { query, found: true };
  });
  return pack(checks.filter(x => x.found).length + ' of ' + checks.length + ' queried substrings found.', { checks, states: states.length }, ['Extend the automaton once for each source character; clones preserve distinct substring transitions.', 'Each query follows transitions from the start state and stops at the first missing edge.'], 'O(n + total query length) time · O(n) states');
}
function suffixTree(text, queries) {
  requireText(text, 'Text'); requireArray(queries, 'Queries');
  const chars = Array.from(text);
  if (chars.length > 100) throw new Error('The teaching suffix-tree builder is intended for short inputs (100 characters max).');
  const root = { terminal: false, children: new Map() };
  for (let start = 0; start < chars.length; start++) {
    let node = root;
    for (let i = start; i < chars.length; i++) {
      if (!node.children.has(chars[i])) node.children.set(chars[i], { terminal: false, children: new Map() });
      node = node.children.get(chars[i]);
    }
    node.terminal = true;
  }
  let nodes = 1;
  const compress = node => {
    const edges = [];
    for (const [first, child] of node.children) {
      let label = first, cursor = child;
      while (!cursor.terminal && cursor.children.size === 1) {
        const next = cursor.children.entries().next().value;
        label += next[0]; cursor = next[1];
      }
      nodes++;
      edges.push({ label, terminal: cursor.terminal, children: compress(cursor) });
    }
    return edges;
  };
  const tree = compress(root);
  const contains = query => {
    const remaining = Array.from(String(query));
    if (!remaining.length) return true;
    let edges = tree;
    while (remaining.length) {
      const edge = edges.find(candidate => candidate.label[0] === remaining[0]);
      if (!edge) return false;
      const label = Array.from(edge.label), common = Math.min(label.length, remaining.length);
      for (let i = 0; i < common; i++) if (label[i] !== remaining[i]) return false;
      if (remaining.length <= label.length) return true;
      remaining.splice(0, label.length);
      edges = edge.children;
    }
    return true;
  };
  const checks = queries.map(query => ({ query, found: contains(query) }));
  return pack(checks.filter(x => x.found).length + ' of ' + checks.length + ' substrings found in the document.', { checks, tree, compactNodes: nodes }, ['Insert each suffix into a character trie, marking where each suffix ends.', 'Compress every non-terminal node with one child into a single labeled edge.', 'Walk edge labels to answer each substring query; a query may finish in the middle of an edge.'], 'O(n²) teaching build · O(query length) lookup');
}

function editDistance(first, second) {
  requireText(first, 'First name'); requireText(second, 'Second name');
  const a = first.toLowerCase(), b = second.toLowerCase(), dp = Array.from({ length: a.length + 1 }, () => Array(b.length + 1).fill(0));
  for (let i = 0; i <= a.length; i++) dp[i][0] = i;
  for (let j = 0; j <= b.length; j++) dp[0][j] = j;
  for (let i = 1; i <= a.length; i++) for (let j = 1; j <= b.length; j++) {
    dp[i][j] = Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + (a[i - 1] === b[j - 1] ? 0 : 1));
    if (i > 1 && j > 1 && a[i - 1] === b[j - 2] && a[i - 2] === b[j - 1]) dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
  }
  return pack('Edit distance is ' + dp[a.length][b.length] + ' operation' + (dp[a.length][b.length] === 1 ? '' : 's') + '.', { levenshtein: levenshteinOnly(a, b), damerauLevenshtein: dp[a.length][b.length], matrix: dp }, ['Fill a DP grid: insert, delete, or replace each character at minimum cost.', 'Damerau distance also checks whether two neighboring characters can be transposed in one operation.'], 'O(m × n) time · O(m × n) space');
}
function levenshteinOnly(a, b) {
  const dp = Array.from({ length: a.length + 1 }, () => Array(b.length + 1).fill(0));
  for (let i = 0; i <= a.length; i++) dp[i][0] = i;
  for (let j = 0; j <= b.length; j++) dp[0][j] = j;
  for (let i = 1; i <= a.length; i++) for (let j = 1; j <= b.length; j++) dp[i][j] = Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + (a[i - 1] === b[j - 1] ? 0 : 1));
  return dp[a.length][b.length];
}
function bitmask(input) {
  const products = requireArray(input.products, 'Products'), budget = Number(input.budget);
  if (!Number.isFinite(budget) || budget < 0) throw new Error('Budget must be a non-negative number.');
  if (products.length > 20) throw new Error('Bitmask DP supports up to 20 products in this demo.');
  const cents = products.map((p, i) => {
    const cost = Number(p.cost);
    if (!p.name || !Number.isFinite(cost) || cost < 0) throw new Error('Product ' + (i + 1) + ' needs a name and a non-negative cost.');
    return Math.round(cost * 100);
  });
  const limit = Math.round(budget * 100), states = 1 << products.length, costs = Array(states).fill(0), feasible = Array(states).fill(false); feasible[0] = true;
  let bestMask = 0, bestCount = 0, bestCost = 0;
  for (let mask = 1; mask < states; mask++) {
    const bit = 31 - Math.clz32(mask & -mask), previous = mask & (mask - 1);
    feasible[mask] = feasible[previous] && cents[bit] <= limit - costs[previous];
    if (feasible[mask]) costs[mask] = costs[previous] + cents[bit];
    const count = popcount(mask);
    if (feasible[mask] && (count > bestCount || (count === bestCount && costs[mask] > bestCost))) { bestMask = mask; bestCount = count; bestCost = costs[mask]; }
  }
  const selected = products.filter((_, i) => bestMask & (1 << i));
  const formattedCost = bestCost / 100;
  return pack('Selected ' + selected.length + ' products for ₹' + formattedCost.toFixed(2) + ' of ₹' + budget.toFixed(2) + '.', { selected, totalCost: formattedCost, remainingBudget: Number((budget - formattedCost).toFixed(2)), feasibleSubsets: feasible.filter(Boolean).length, subsetsChecked: states }, ['Represent each product subset as a bitmask; derive its cost by removing the lowest set bit.', 'Keep the feasible subset with the greatest number of products; break ties by spending more of the budget.'], 'O(2ⁿ) time · O(2ⁿ) space');
}
function popcount(x) { let count = 0; while (x) { x &= x - 1; count++; } return count; }
function matrixChain(dimensions) {
  const d = requireArray(dimensions, 'Dimensions').map(Number);
  if (d.length < 2 || d.some(x => !Number.isInteger(x) || x <= 0)) throw new Error('Enter at least two positive integer dimensions.');
  const n = d.length - 1;
  if (n > 30) throw new Error('Use at most 30 matrices for this cubic-time demonstration.');
  const cost = Array.from({ length: n }, () => Array(n).fill(0)), split = Array.from({ length: n }, () => Array(n).fill(0));
  for (let length = 2; length <= n; length++) for (let i = 0; i + length <= n; i++) {
    const j = i + length - 1; cost[i][j] = Infinity;
    for (let k = i; k < j; k++) { const candidate = cost[i][k] + cost[k + 1][j] + d[i] * d[k + 1] * d[j + 1]; if (candidate < cost[i][j]) { cost[i][j] = candidate; split[i][j] = k; } }
  }
  const order = (i, j) => i === j ? 'A' + (i + 1) : '(' + order(i, split[i][j]) + ' × ' + order(split[i][j] + 1, j) + ')';
  return pack('Minimum scalar multiplications: ' + cost[0][n - 1].toLocaleString() + '.', { scalarMultiplications: cost[0][n - 1], parenthesization: order(0, n - 1), costTable: cost }, ['Try each possible final split of the matrix chain.', 'Store the cheapest result for every subchain, then reconstruct its parenthesization.'], 'O(n³) time · O(n²) space');
}
function optimalBst(input) {
  const keys = requireArray(input.keys, 'Keys'), p = requireArray(input.successful, 'Successful frequencies').map(Number), q = requireArray(input.unsuccessful, 'Unsuccessful frequencies').map(Number), n = keys.length;
  if (n > 30) throw new Error('Use at most 30 keys for this cubic-time demonstration.');
  if (p.length !== n || q.length !== n + 1 || p.concat(q).some(x => !Number.isFinite(x) || x < 0)) throw new Error('Provide n successful and n+1 non-negative unsuccessful frequencies.');
  if (keys.some((x, i) => typeof x !== 'string' || (i && x < keys[i - 1]))) throw new Error('Keys must be strings in sorted order.');
  const e = Array.from({ length: n + 2 }, () => Array(n + 1).fill(0)), w = Array.from({ length: n + 2 }, () => Array(n + 1).fill(0)), root = Array.from({ length: n + 2 }, () => Array(n + 1).fill(0));
  for (let i = 1; i <= n + 1; i++) e[i][i - 1] = w[i][i - 1] = q[i - 1];
  for (let len = 1; len <= n; len++) for (let i = 1; i + len - 1 <= n; i++) {
    const j = i + len - 1; e[i][j] = Infinity; w[i][j] = w[i][j - 1] + p[j - 1] + q[j];
    for (let r = i; r <= j; r++) { const candidate = e[i][r - 1] + e[r + 1][j] + w[i][j]; if (candidate < e[i][j]) { e[i][j] = candidate; root[i][j] = r; } }
  }
  const tree = (i, j) => i > j ? '∅' : '(' + tree(i, root[i][j] - 1) + ' ← ' + keys[root[i][j] - 1] + ' → ' + tree(root[i][j] + 1, j) + ')';
  return pack('Expected weighted search cost: ' + (n ? e[1][n] : q[0]).toFixed(2) + '.', { weightedSearchCost: n ? e[1][n] : q[0], tree: n ? tree(1, n) : '(empty index)', rootTable: root }, ['For each key interval, try every key as its root.', 'Combine left and right costs with the interval frequency weight; store the cheapest root.'], 'O(n³) time · O(n²) space');
}

function maximumFlow(input, method) {
  const nodes = requireArray(input.nodes, 'Nodes'), edges = requireArray(input.edges, 'Edges');
  const source = nodes.indexOf(input.source), sink = nodes.indexOf(input.sink);
  if (nodes.length < 2 || source < 0 || sink < 0 || source === sink) throw new Error('Choose different source and sink nodes from the node list.');
  const graph = nodes.map(() => []), originals = [];
  const add = (u, v, cap, label) => {
    const forward = { to: v, rev: graph[v].length, cap, original: cap }, reverse = { to: u, rev: graph[u].length, cap: 0, original: 0 };
    graph[u].push(forward); graph[v].push(reverse);
  };
  for (const edge of edges) {
    const u = nodes.indexOf(edge.from), v = nodes.indexOf(edge.to), cap = Number(edge.capacity);
    if (u < 0 || v < 0 || u === v || !Number.isFinite(cap) || cap < 0) throw new Error('Each route needs valid different node names and non-negative capacity.');
    if (!cap) continue;
    add(u, v, cap, edge.from + ' → ' + edge.to); originals.push({ u, v, from: edge.from, to: edge.to, capacity: cap });
  }
  const trace = [], augment = path => {
    const amount = Math.min(...path.map(([u, ei]) => graph[u][ei].cap));
    for (const [u, ei] of path) { const e = graph[u][ei]; e.cap -= amount; graph[e.to][e.rev].cap += amount; }
    return amount;
  };
  let flow = 0;
  if (method === 'edmonds-karp') {
    while (true) {
      const parent = Array(nodes.length).fill(null), queue = [source]; parent[source] = [-1, -1];
      for (let head = 0; head < queue.length && parent[sink] === null; head++) {
        const u = queue[head];
        graph[u].forEach((e, ei) => { if (e.cap > 0 && parent[e.to] === null) { parent[e.to] = [u, ei]; queue.push(e.to); } });
      }
      if (parent[sink] === null) break;
      const path = []; for (let v = sink; v !== source;) { const pair = parent[v]; path.unshift(pair); v = pair[0]; }
      const amount = augment(path); flow += amount;
      trace.push('BFS path ' + [source, ...path.map(([u, ei]) => graph[u][ei].to)].map(i => nodes[i]).join(' → ') + ' carries ' + amount + '.');
    }
  } else if (method === 'ford-fulkerson') {
    while (true) {
      const visited = Array(nodes.length).fill(false);
      const search = (u, path) => {
        if (u === sink) return path;
        visited[u] = true;
        for (let ei = 0; ei < graph[u].length; ei++) { const e = graph[u][ei]; if (e.cap > 0 && !visited[e.to]) { const found = search(e.to, path.concat([[u, ei]])); if (found) return found; } }
        return null;
      };
      const path = search(source, []);
      if (!path) break;
      const amount = augment(path); flow += amount;
      trace.push('DFS augmenting path carries ' + amount + '.');
    }
  } else {
    while (true) {
      const level = Array(nodes.length).fill(-1), queue = [source]; level[source] = 0;
      for (let head = 0; head < queue.length; head++) { const u = queue[head]; for (const e of graph[u]) if (e.cap > 0 && level[e.to] < 0) { level[e.to] = level[u] + 1; queue.push(e.to); } }
      if (level[sink] < 0) break;
      const next = Array(nodes.length).fill(0);
      const send = (u, limit) => {
        if (u === sink) return limit;
        for (; next[u] < graph[u].length; next[u]++) { const e = graph[u][next[u]]; if (e.cap > 0 && level[e.to] === level[u] + 1) { const got = send(e.to, Math.min(limit, e.cap)); if (got) { e.cap -= got; graph[e.to][e.rev].cap += got; return got; } } }
        return 0;
      };
      let pushed;
      while ((pushed = send(source, Number.MAX_SAFE_INTEGER)) > 0) { flow += pushed; trace.push('Level graph sends a blocking-flow amount of ' + pushed + '.'); }
    }
  }
  const reachable = Array(nodes.length).fill(false), queue = [source]; reachable[source] = true;
  for (let head = 0; head < queue.length; head++) { const u = queue[head]; for (const e of graph[u]) if (e.cap > 0 && !reachable[e.to]) { reachable[e.to] = true; queue.push(e.to); } }
  const cut = originals.filter(e => reachable[e.u] && !reachable[e.v]).map(e => ({ route: e.from + ' → ' + e.to, capacity: e.capacity }));
  if (!trace.length) trace.push('No augmenting path exists; the maximum flow is zero.');
  return pack('Maximum delivery flow is ' + flow + ' units per time window.', { algorithm: method, maxFlow: flow, sourceSide: nodes.filter((_, i) => reachable[i]), minCut: cut, routes: originals }, trace, method === 'edmonds-karp' ? 'O(V × E²)' : method === 'dinic' ? 'O(V² × E) general graph' : 'O(E × max-flow) for integer capacities');
}
function bipartite(input) {
  const left = requireArray(input.suppliers, 'Suppliers'), right = requireArray(input.warehouses, 'Warehouses'), pairs = requireArray(input.eligible, 'Eligible pairs');
  const adjacency = left.map(() => []);
  for (const pair of pairs) { if (!Array.isArray(pair) || pair.length !== 2 || !Number.isInteger(pair[0]) || !Number.isInteger(pair[1]) || !left[pair[0]] || !right[pair[1]]) throw new Error('Eligible pairs must be valid [supplierIndex, warehouseIndex] values.'); adjacency[pair[0]].push(pair[1]); }
  const match = Array(right.length).fill(-1); let count = 0; const trace = [];
  const dfs = (u, seen) => { for (const v of adjacency[u]) { if (seen[v]) continue; seen[v] = true; if (match[v] < 0 || dfs(match[v], seen)) { match[v] = u; return true; } } return false; };
  for (let u = 0; u < left.length; u++) { if (dfs(u, Array(right.length).fill(false))) { count++; trace.push('Found an augmenting path for ' + left[u] + '.'); } else trace.push('No free warehouse can be reached for ' + left[u] + '.'); }
  const assignments = match.map((u, v) => u >= 0 ? { supplier: left[u], warehouse: right[v] } : null).filter(Boolean);
  return pack(count + ' of ' + left.length + ' suppliers assigned.', { assignments, unmatchedSuppliers: left.filter((_, i) => !assignments.some(a => a.supplier === left[i])) }, trace, 'O(V × E)');
}

function threeSat(input) {
  const variableCount = Number(input.variables), clauses = requireArray(input.clauses, 'Clauses');
  if (!Number.isInteger(variableCount) || variableCount < 1 || variableCount > 20) throw new Error('Use between 1 and 20 variables for this exhaustive demo.');
  if (clauses.length > 40) throw new Error('Use at most 40 clauses for this exhaustive demo.');
  clauses.forEach((c, i) => { if (!Array.isArray(c) || c.length !== 3 || c.some(x => !Number.isInteger(x) || x === 0 || Math.abs(x) > variableCount)) throw new Error('Clause ' + (i + 1) + ' must contain three literals in the range ±1..±' + variableCount + '.'); });
  let assignment = null, attempts = 0;
  const limit = 1 << variableCount;
  for (let mask = 0; mask < limit; mask++) {
    attempts++;
    if (clauses.every(c => c.some(lit => ((mask >> (Math.abs(lit) - 1)) & 1) === (lit > 0 ? 1 : 0)))) { assignment = mask; break; }
  }
  const vertices = clauses.flatMap((c, ci) => c.map((literal, slot) => ({ id: verticesLabel(ci, slot), clause: ci, literal })));
  const edges = [];
  for (let i = 0; i < vertices.length; i++) for (let j = i + 1; j < vertices.length; j++) if (vertices[i].clause !== vertices[j].clause && vertices[i].literal !== -vertices[j].literal) edges.push([i, j]);
  const clique = [];
  if (assignment !== null) clauses.forEach((c, ci) => {
    const slot = c.findIndex(lit => ((assignment >> (Math.abs(lit) - 1)) & 1) === (lit > 0 ? 1 : 0));
    clique.push(ci * 3 + slot);
  });
  const inClique = new Set(clique), independent = clique.map(i => vertices[i].id), coverOfComplement = vertices.map(v => v.id).filter((_, i) => !inClique.has(i));
  const values = assignment === null ? null : Array.from({ length: variableCount }, (_, i) => ({ variable: 'x' + (i + 1), value: Boolean((assignment >> i) & 1) }));
  return pack(assignment === null ? 'This 3-SAT formula is unsatisfiable.' : 'Satisfiable: witness clique size ' + clique.length + ' equals the number of clauses.', { satisfiable: assignment !== null, assignment: values, assignmentsChecked: attempts, graph: { vertices: vertices.map(v => v.id), edges }, clique: clique.map(i => vertices[i].id), independentSetInComplement: independent, vertexCoverInComplement: coverOfComplement }, ['Try truth assignments until every 3-literal clause is satisfied (small teaching instance).', 'Create one graph vertex per literal occurrence; connect vertices from different clauses unless they contradict each other.', 'A satisfying choice gives a clique of size equal to the clause count.', 'That clique is an independent set in the complement graph; its complement is a vertex cover there.'], '3-SAT search is exponential · reduction graph has 3m vertices');
}
function verticesLabel(clause, slot) { return 'C' + (clause + 1) + ':literal' + (slot + 1); }
function vertexCover(input) {
  const vertices = requireArray(input.locations, 'Locations'), edges = requireArray(input.edges, 'Edges'), selected = new Set(), pickedEdges = [];
  edges.forEach((edge, i) => { if (!Array.isArray(edge) || edge.length !== 2 || !Number.isInteger(edge[0]) || !Number.isInteger(edge[1]) || !vertices[edge[0]] || !vertices[edge[1]] || edge[0] === edge[1]) throw new Error('Edge ' + (i + 1) + ' must be [locationIndex, locationIndex] with different valid endpoints.'); });
  edges.forEach(([u, v]) => { if (!selected.has(u) && !selected.has(v)) { selected.add(u); selected.add(v); pickedEdges.push([vertices[u], vertices[v]]); } });
  const cover = [...selected].map(i => vertices[i]);
  return pack('Selected ' + cover.length + ' locations to cover ' + edges.length + ' conflict routes.', { cover, pickedMatchingEdges: pickedEdges, edgeCount: edges.length, approximationBound: 'Cover size ≤ 2 × optimal cover size' }, ['Scan conflict routes; when an uncovered edge appears, select both endpoints.', 'Every selected edge is disjoint from later selected edges, so the selected cover is at most twice optimal.'], 'O(V + E)');
}

function quicksort(values) {
  const arr = requireArray(values, 'Values').slice(), trace = [];
  if (arr.length > 300) throw new Error('Use at most 300 values to keep the trace readable.');
  const sort = (lo, hi) => {
    if (lo >= hi) return;
    const pivot = arr[lo + Math.floor(Math.random() * (hi - lo + 1))];
    let less = lo, current = lo, greater = hi;
    while (current <= greater) {
      if (arr[current] < pivot) { [arr[less], arr[current]] = [arr[current], arr[less]]; less++; current++; }
      else if (arr[current] > pivot) { [arr[current], arr[greater]] = [arr[greater], arr[current]]; greater--; }
      else current++;
    }
    if (trace.length < 40) trace.push('Random pivot ' + String(pivot) + ' splits into ' + less + ' smaller, ' + (greater - less + 1) + ' equal, and ' + (hi - greater) + ' larger values.');
    sort(lo, less - 1); sort(greater + 1, hi);
  };
  if (arr.some(x => typeof x !== 'number' || !Number.isFinite(x))) throw new Error('Demand values must be finite numbers.');
  sort(0, arr.length - 1);
  return pack('Demand values sorted from lowest to highest.', { sorted: arr, pivotsShown: trace.length }, trace.length ? trace : ['The list is already a single value or empty.'], 'Expected O(n log n) · worst O(n²)');
}
function reservoir(input) {
  const values = requireArray(input.values, 'Stream values'), k = Number(input.k);
  if (!Number.isInteger(k) || k < 0 || k > 500) throw new Error('Sample size k must be an integer from 0 to 500.');
  const sample = [], trace = [];
  values.forEach((value, index) => {
    if (sample.length < k) { sample.push(value); trace.push('Stream item ' + (index + 1) + ' fills an empty sample slot.'); }
    else if (k > 0) {
      const slot = Math.floor(Math.random() * (index + 1));
      if (slot < k) { sample[slot] = value; trace.push('Stream item ' + (index + 1) + ' replaces sample slot ' + (slot + 1) + '.'); }
      else trace.push('Stream item ' + (index + 1) + ' is not selected.');
    } else trace.push('k is 0, so no stream item is retained.');
  });
  return pack('Kept ' + sample.length + ' sample items from ' + values.length + ' stream records.', { sample, seen: values.length, requestedSampleSize: k }, trace.slice(0, 80), 'O(n) time · O(k) space');
}
function isPrime(value) {
  let n = BigInt(value);
  if (n < 2n) return false;
  for (const p of [2n, 3n, 5n, 7n, 11n, 13n, 17n, 19n, 23n, 29n, 31n, 37n]) { if (n === p) return true; if (n % p === 0n) return false; }
  let d = n - 1n, s = 0; while ((d & 1n) === 0n) { d >>= 1n; s++; }
  const modPow = (base, exp) => { let result = 1n; base %= n; while (exp) { if (exp & 1n) result = result * base % n; base = base * base % n; exp >>= 1n; } return result; };
  for (const raw of [2n, 325n, 9375n, 28178n, 450775n, 9780504n, 1795265022n]) {
    const a = raw % n; if (a === 0n) continue;
    let x = modPow(a, d); if (x === 1n || x === n - 1n) continue;
    let pass = false;
    for (let r = 1; r < s; r++) { x = x * x % n; if (x === n - 1n) { pass = true; break; } if (x === 1n) return false; }
    if (!pass) return false;
  }
  return true;
}
function millerRabin(value) {
  const raw = String(value).trim();
  if (!/^\d+$/.test(raw)) throw new Error('Enter a non-negative whole number using digits only.');
  const n = BigInt(raw); if (n > 18446744073709551615n) throw new Error('Use a number no larger than 2⁶⁴ − 1.');
  const prime = isPrime(n);
  return pack(raw + (prime ? ' passes the deterministic 64-bit Miller–Rabin test.' : ' is composite.'), { number: raw, prime, range: 'Deterministic for unsigned integers below 2⁶⁴', bases: [2, 325, 9375, 28178, 450775, 9780504, 1795265022] }, ['Write n − 1 = d × 2ˢ with d odd.', 'Test the fixed 64-bit witness bases using modular exponentiation.', prime ? 'No witness proves compositeness for this input.' : 'A witness proves the input is composite.'], 'O(k log³ n) with safe modular arithmetic');
}
function blelloch(values) {
  const input = requireArray(values, 'Values').map(Number);
  if (input.some(x => !Number.isFinite(x))) throw new Error('Scan values must be finite numbers.');
  const size = Math.max(1, 2 ** Math.ceil(Math.log2(Math.max(1, input.length)))), tree = Array(size).fill(0); input.forEach((x, i) => { tree[i] = x; });
  const trace = ['Pad to ' + size + ' leaves with zeros so the reduction tree is balanced.'];
  for (let stride = 1; stride < size; stride *= 2) { for (let i = 0; i < size; i += stride * 2) { const end = i + stride * 2 - 1; tree[end] += tree[end - stride]; } trace.push('Up-sweep stride ' + stride + ': ' + JSON.stringify(tree)); }
  tree[size - 1] = 0;
  for (let stride = size / 2; stride >= 1; stride /= 2) { for (let i = 0; i < size; i += stride * 2) { const end = i + stride * 2 - 1, left = tree[end - stride]; tree[end - stride] = tree[end]; tree[end] += left; } trace.push('Down-sweep stride ' + stride + ': ' + JSON.stringify(tree)); }
  const inclusive = input.map((x, i) => tree[i] + x);
  return pack('Cumulative inventory totals: ' + inclusive.join(', ') + '.', { input, exclusivePrefix: tree.slice(0, input.length), inclusivePrefix: inclusive }, trace, 'O(n) work · O(n) space · O(log n) depth');
}
function parallelReduce(input) {
  const values = requireArray(input.values, 'Values').map(Number), workers = Number(input.workers);
  if (!Number.isInteger(workers) || workers < 1 || workers > 64 || values.some(x => !Number.isFinite(x))) throw new Error('Use finite values and 1–64 workers.');
  const count = Math.min(workers, Math.max(1, values.length)), size = Math.ceil(values.length / count), chunks = [];
  for (let i = 0; i < values.length; i += size) { const part = values.slice(i, i + size); chunks.push({ startIndex: i, endIndex: i + part.length - 1, count: part.length, sum: part.reduce((a, b) => a + b, 0), max: part.length ? part.reduce((highest, value) => Math.max(highest, value), -Infinity) : null }); }
  const total = chunks.reduce((a, c) => a + c.sum, 0), max = values.length ? values.reduce((highest, value) => Math.max(highest, value), -Infinity) : null;
  return pack('Reduced ' + values.length + ' quantities across ' + chunks.length + ' chunks.', { total, maximum: max, count: values.length, average: values.length ? total / values.length : 0, chunks }, ['Divide the stream into worker-sized chunks.', 'Each worker computes a partial sum and maximum; combine the partial summaries.', 'This browser demo simulates fork-join chunks and their reduction.'], 'O(n) work · O(log p) combine depth');
}
function brent(input) {
  const work = Number(input.work), criticalPath = Number(input.criticalPath), processors = Number(input.processors);
  if (![work, criticalPath, processors].every(Number.isFinite) || !Number.isInteger(processors) || work < 0 || criticalPath < 0 || criticalPath > work || processors < 1) throw new Error('Require work ≥ critical path ≥ 0 and at least one whole processor.');
  const averageWork = Math.ceil(work / processors), lower = Math.max(averageWork, criticalPath), upper = averageWork + criticalPath;
  return pack('Parallel time is bounded between ' + lower + ' and ' + upper + ' work units.', { work, criticalPath, processors, lowerBound: lower, brentUpperBound: upper, calculation: 'max(work / processors, critical path) ≤ time ≤ work / processors + critical path' }, ['Split total work across ' + processors + ' processors: ceil(' + work + ' / ' + processors + ') = ' + averageWork + '.', 'The critical path sets a lower bound; Brent gives the stated upper bound.'], 'O(1) arithmetic');
}
function pack(headline, result, trace, complexity) { return { headline, result, trace, complexity }; }

function runAlgorithm(id, input) {
  switch (id) {
    case 'kmp': return kmp(input.text, input.pattern);
    case 'z-function': return zSearch(input.text, input.pattern);
    case 'rabin-karp': return rabinKarp(input.text, input.pattern);
    case 'aho-corasick': return ahoCorasick(input.text, input.keywords);
    case 'suffix-array': return suffixArrayRun(input.text);
    case 'kasai-lcp': {
      const { chars, suffixArray } = makeSuffixArray(requireText(input.text, 'Text')), lcp = kasai(chars, suffixArray), longest = Math.max(0, ...lcp), order = lcp.indexOf(longest), phrase = longest ? chars.slice(suffixArray[order], suffixArray[order] + longest).join('') : '';
      return pack(longest ? 'Longest common prefix is “' + phrase + '” (' + longest + ' characters).' : 'No repeated non-empty prefix exists.', { suffixArray, lcp, longestRepeated: phrase }, ['Build the suffix array once.', 'Kasai carries the previous LCP forward and decreases it by at most one per step.'], 'O(n) time after suffix-array construction');
    }
    case 'suffix-automaton': return suffixAutomaton(input.text, input.queries);
    case 'suffix-tree': return suffixTree(input.text, input.queries);
    case 'edit-distance': return editDistance(input.first, input.second);
    case 'bitmask-dp': return bitmask(input);
    case 'matrix-chain': return matrixChain(input.dimensions);
    case 'optimal-bst': return optimalBst(input);
    case 'ford-fulkerson': case 'edmonds-karp': case 'dinic': return maximumFlow(input, id);
    case 'bipartite-matching': return bipartite(input);
    case '3sat-reductions': return threeSat(input);
    case 'vertex-cover': return vertexCover(input);
    case 'randomized-quicksort': return quicksort(input.values);
    case 'reservoir-sampling': return reservoir(input);
    case 'miller-rabin': return millerRabin(input.number);
    case 'blelloch-scan': return blelloch(input.values);
    case 'parallel-reduce': return parallelReduce(input);
    case 'brent-theorem': return brent(input);
    default: throw new Error('Choose an algorithm from the toolkit.');
  }
}

export { algorithms, groups, runAlgorithm };

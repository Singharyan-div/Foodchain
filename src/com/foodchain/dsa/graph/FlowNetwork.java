package com.foodchain.dsa.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/** Directed capacitated graph with Ford-Fulkerson, Edmonds-Karp, and Dinic solvers. */
public final class FlowNetwork {
    public enum Algorithm { FORD_FULKERSON, EDMONDS_KARP, DINIC }

    private final int nodeCount;
    private final List<Arc> arcs = new ArrayList<Arc>();

    public FlowNetwork(int nodeCount) {
        if (nodeCount < 2) throw new IllegalArgumentException("A flow network needs at least two nodes.");
        this.nodeCount = nodeCount;
    }

    public void addEdge(int from, int to, long capacity, String label) {
        if (from < 0 || from >= nodeCount || to < 0 || to >= nodeCount || from == to) {
            throw new IllegalArgumentException("Invalid flow-network edge endpoints.");
        }
        if (capacity < 0) throw new IllegalArgumentException("Edge capacity cannot be negative.");
        if (capacity > 0) arcs.add(new Arc(from, to, capacity, label));
    }

    public Result solve(int source, int sink, Algorithm algorithm) {
        if (source < 0 || source >= nodeCount || sink < 0 || sink >= nodeCount || source == sink) {
            throw new IllegalArgumentException("Source and sink must be different valid nodes.");
        }
        if (algorithm == null) throw new IllegalArgumentException("Flow algorithm is required.");
        List<List<ResidualEdge>> graph = createResidualGraph();
        long flow;
        if (algorithm == Algorithm.FORD_FULKERSON) {
            flow = fordFulkerson(graph, source, sink);
        } else if (algorithm == Algorithm.EDMONDS_KARP) {
            flow = edmondsKarp(graph, source, sink);
        } else {
            flow = dinic(graph, source, sink);
        }
        boolean[] reachable = residualReachable(graph, source);
        List<Arc> cut = new ArrayList<Arc>();
        for (Arc arc : arcs) if (reachable[arc.from] && !reachable[arc.to]) cut.add(arc);
        List<Integer> sourceSide = new ArrayList<Integer>();
        for (int node = 0; node < nodeCount; node++) if (reachable[node]) sourceSide.add(node);
        return new Result(flow, algorithm, sourceSide, cut);
    }

    private List<List<ResidualEdge>> createResidualGraph() {
        List<List<ResidualEdge>> graph = new ArrayList<List<ResidualEdge>>(nodeCount);
        for (int i = 0; i < nodeCount; i++) graph.add(new ArrayList<ResidualEdge>());
        for (Arc arc : arcs) {
            ResidualEdge forward = new ResidualEdge(arc.to, graph.get(arc.to).size(), arc.capacity);
            ResidualEdge reverse = new ResidualEdge(arc.from, graph.get(arc.from).size(), 0);
            graph.get(arc.from).add(forward);
            graph.get(arc.to).add(reverse);
        }
        return graph;
    }

    private long fordFulkerson(List<List<ResidualEdge>> graph, int source, int sink) {
        long flow = 0;
        while (true) {
            boolean[] visited = new boolean[nodeCount];
            long pushed = depthFirstAugment(graph, source, sink, Long.MAX_VALUE, visited);
            if (pushed == 0) return flow;
            flow = Math.addExact(flow, pushed);
        }
    }

    private long depthFirstAugment(List<List<ResidualEdge>> graph, int node, int sink,
            long limit, boolean[] visited) {
        if (node == sink) return limit;
        visited[node] = true;
        List<ResidualEdge> edges = graph.get(node);
        for (int i = 0; i < edges.size(); i++) {
            ResidualEdge edge = edges.get(i);
            if (edge.capacity <= 0 || visited[edge.to]) continue;
            long pushed = depthFirstAugment(graph, edge.to, sink, Math.min(limit, edge.capacity), visited);
            if (pushed > 0) {
                edge.capacity -= pushed;
                graph.get(edge.to).get(edge.reverseIndex).capacity += pushed;
                return pushed;
            }
        }
        return 0;
    }

    private long edmondsKarp(List<List<ResidualEdge>> graph, int source, int sink) {
        long flow = 0;
        int[] parentNode = new int[nodeCount];
        int[] parentEdge = new int[nodeCount];
        while (true) {
            Arrays.fill(parentNode, -1);
            parentNode[source] = source;
            Queue<Integer> queue = new ArrayDeque<Integer>();
            queue.add(source);
            while (!queue.isEmpty() && parentNode[sink] == -1) {
                int node = queue.remove();
                for (int i = 0; i < graph.get(node).size(); i++) {
                    ResidualEdge edge = graph.get(node).get(i);
                    if (edge.capacity > 0 && parentNode[edge.to] == -1) {
                        parentNode[edge.to] = node;
                        parentEdge[edge.to] = i;
                        queue.add(edge.to);
                    }
                }
            }
            if (parentNode[sink] == -1) return flow;
            long pushed = Long.MAX_VALUE;
            for (int node = sink; node != source; node = parentNode[node]) {
                pushed = Math.min(pushed,
                        graph.get(parentNode[node]).get(parentEdge[node]).capacity);
            }
            for (int node = sink; node != source; node = parentNode[node]) {
                ResidualEdge edge = graph.get(parentNode[node]).get(parentEdge[node]);
                edge.capacity -= pushed;
                graph.get(node).get(edge.reverseIndex).capacity += pushed;
            }
            flow = Math.addExact(flow, pushed);
        }
    }

    private long dinic(List<List<ResidualEdge>> graph, int source, int sink) {
        long flow = 0;
        int[] level = new int[nodeCount];
        int[] nextEdge = new int[nodeCount];
        while (buildLevels(graph, source, sink, level)) {
            Arrays.fill(nextEdge, 0);
            long pushed;
            while ((pushed = sendBlockingFlow(graph, source, sink, Long.MAX_VALUE, level, nextEdge)) > 0) {
                flow = Math.addExact(flow, pushed);
            }
        }
        return flow;
    }

    private boolean buildLevels(List<List<ResidualEdge>> graph, int source, int sink, int[] level) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new ArrayDeque<Integer>();
        queue.add(source);
        while (!queue.isEmpty()) {
            int node = queue.remove();
            for (ResidualEdge edge : graph.get(node)) {
                if (edge.capacity > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[node] + 1;
                    queue.add(edge.to);
                }
            }
        }
        return level[sink] != -1;
    }

    private long sendBlockingFlow(List<List<ResidualEdge>> graph, int node, int sink,
            long limit, int[] level, int[] nextEdge) {
        if (node == sink) return limit;
        List<ResidualEdge> edges = graph.get(node);
        for (; nextEdge[node] < edges.size(); nextEdge[node]++) {
            ResidualEdge edge = edges.get(nextEdge[node]);
            if (edge.capacity <= 0 || level[edge.to] != level[node] + 1) continue;
            long pushed = sendBlockingFlow(graph, edge.to, sink,
                    Math.min(limit, edge.capacity), level, nextEdge);
            if (pushed > 0) {
                edge.capacity -= pushed;
                graph.get(edge.to).get(edge.reverseIndex).capacity += pushed;
                return pushed;
            }
        }
        return 0;
    }

    private boolean[] residualReachable(List<List<ResidualEdge>> graph, int source) {
        boolean[] visited = new boolean[nodeCount];
        Queue<Integer> queue = new ArrayDeque<Integer>();
        visited[source] = true;
        queue.add(source);
        while (!queue.isEmpty()) {
            int node = queue.remove();
            for (ResidualEdge edge : graph.get(node)) {
                if (edge.capacity > 0 && !visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }
        return visited;
    }

    private static final class ResidualEdge {
        private final int to;
        private final int reverseIndex;
        private long capacity;

        private ResidualEdge(int to, int reverseIndex, long capacity) {
            this.to = to;
            this.reverseIndex = reverseIndex;
            this.capacity = capacity;
        }
    }

    public static final class Arc {
        private final int from;
        private final int to;
        private final long capacity;
        private final String label;

        private Arc(int from, int to, long capacity, String label) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.label = label == null ? "" : label;
        }

        public int getFrom() { return from; }
        public int getTo() { return to; }
        public long getCapacity() { return capacity; }
        public String getLabel() { return label; }
    }

    public static final class Result {
        private final long maxFlow;
        private final Algorithm algorithm;
        private final List<Integer> sourceSide;
        private final List<Arc> minCutEdges;

        private Result(long maxFlow, Algorithm algorithm, List<Integer> sourceSide, List<Arc> minCutEdges) {
            this.maxFlow = maxFlow;
            this.algorithm = algorithm;
            this.sourceSide = Collections.unmodifiableList(sourceSide);
            this.minCutEdges = Collections.unmodifiableList(minCutEdges);
        }

        public long getMaxFlow() { return maxFlow; }
        public Algorithm getAlgorithm() { return algorithm; }
        public List<Integer> getSourceSide() { return sourceSide; }
        public List<Arc> getMinCutEdges() { return minCutEdges; }
    }
}

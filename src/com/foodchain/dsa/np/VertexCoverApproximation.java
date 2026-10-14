package com.foodchain.dsa.np;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Standard endpoint-pair 2-approximation for the minimum vertex-cover problem. */
public final class VertexCoverApproximation {
    private VertexCoverApproximation() { }

    public static Result approximate(List<String> vertices, List<int[]> edges) {
        if (vertices == null || edges == null) throw new IllegalArgumentException("Graph is required.");
        boolean[] selected = new boolean[vertices.size()];
        int[] normalizedFirst = new int[edges.size()];
        int[] normalizedSecond = new int[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            int[] edge = edges.get(i);
            if (edge == null || edge.length != 2 || edge[0] < 0 || edge[0] >= vertices.size()
                    || edge[1] < 0 || edge[1] >= vertices.size() || edge[0] == edge[1]) {
                throw new IllegalArgumentException("An edge has an invalid endpoint.");
            }
            normalizedFirst[i] = edge[0];
            normalizedSecond[i] = edge[1];
        }
        for (int i = 0; i < edges.size(); i++) {
            int first = normalizedFirst[i];
            int second = normalizedSecond[i];
            if (!selected[first] && !selected[second]) {
                selected[first] = true;
                selected[second] = true;
            }
        }
        List<String> cover = new ArrayList<String>();
        for (int i = 0; i < selected.length; i++) if (selected[i]) cover.add(vertices.get(i));
        return new Result(cover, edges.size());
    }

    public static final class Result {
        private final List<String> selectedVertices;
        private final int edgeCount;
        private Result(List<String> selectedVertices, int edgeCount) {
            this.selectedVertices = Collections.unmodifiableList(selectedVertices);
            this.edgeCount = edgeCount;
        }
        public List<String> getSelectedVertices() { return selectedVertices; }
        public int getEdgeCount() { return edgeCount; }
    }
}

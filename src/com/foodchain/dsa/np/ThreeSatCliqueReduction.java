package com.foodchain.dsa.np;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Constructs the standard 3-SAT-to-CLIQUE graph and searches for a clause-sized clique. */
public final class ThreeSatCliqueReduction {
    private ThreeSatCliqueReduction() { }

    public static Result reduce(int variableCount, List<ThreeSatSolver.Clause> clauses) {
        List<Vertex> vertices = new ArrayList<Vertex>();
        for (int clauseIndex = 0; clauseIndex < clauses.size(); clauseIndex++) {
            int[] literals = clauses.get(clauseIndex).getLiterals();
            for (int literal : literals) vertices.add(new Vertex(clauseIndex, literal));
        }
        List<int[]> edges = new ArrayList<int[]>();
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Vertex first = vertices.get(i);
                Vertex second = vertices.get(j);
                if (first.clause != second.clause && first.literal != -second.literal) {
                    edges.add(new int[] { i, j });
                }
            }
        }
        int[] clique = findClauseClique(vertices, clauses.size(), variableCount);
        List<Integer> complementIndependentSet = new ArrayList<Integer>();
        if (clique != null) for (int vertex : clique) complementIndependentSet.add(vertex);
        List<Integer> complementVertexCover = new ArrayList<Integer>();
        if (clique != null) {
            boolean[] inIndependentSet = new boolean[vertices.size()];
            for (int vertex : clique) inIndependentSet[vertex] = true;
            for (int vertex = 0; vertex < vertices.size(); vertex++) {
                if (!inIndependentSet[vertex]) complementVertexCover.add(vertex);
            }
        }
        return new Result(vertices, edges, clique, complementIndependentSet, complementVertexCover,
                clauses.size());
    }

    private static int[] findClauseClique(List<Vertex> vertices, int clauseCount, int variableCount) {
        int[] chosen = new int[clauseCount];
        byte[] assignments = new byte[variableCount + 1];
        return chooseForClause(0, vertices, clauseCount, assignments, chosen) ? chosen : null;
    }

    private static boolean chooseForClause(int clause, List<Vertex> vertices, int clauseCount,
            byte[] assignments, int[] chosen) {
        if (clause == clauseCount) return true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            if (vertex.clause != clause) continue;
            int variable = Math.abs(vertex.literal);
            byte wanted = (byte) (vertex.literal > 0 ? 1 : -1);
            if (assignments[variable] != 0 && assignments[variable] != wanted) continue;
            byte previous = assignments[variable];
            assignments[variable] = wanted;
            chosen[clause] = i;
            if (chooseForClause(clause + 1, vertices, clauseCount, assignments, chosen)) return true;
            assignments[variable] = previous;
        }
        return false;
    }

    private static final class Vertex {
        private final int clause;
        private final int literal;
        private Vertex(int clause, int literal) { this.clause = clause; this.literal = literal; }
    }

    public static final class Result {
        private final List<String> vertices;
        private final List<int[]> edges;
        private final int[] clique;
        private final List<Integer> independentSet;
        private final List<Integer> vertexCover;
        private final int targetSize;

        private Result(List<Vertex> internalVertices, List<int[]> edges, int[] clique,
                List<Integer> independentSet, List<Integer> vertexCover, int targetSize) {
            List<String> labels = new ArrayList<String>();
            for (Vertex vertex : internalVertices) {
                labels.add("C" + (vertex.clause + 1) + ":" + literalName(vertex.literal));
            }
            this.vertices = Collections.unmodifiableList(labels);
            List<int[]> edgeCopy = new ArrayList<int[]>();
            for (int[] edge : edges) edgeCopy.add(Arrays.copyOf(edge, edge.length));
            this.edges = Collections.unmodifiableList(edgeCopy);
            this.clique = clique == null ? null : Arrays.copyOf(clique, clique.length);
            this.independentSet = Collections.unmodifiableList(independentSet);
            this.vertexCover = Collections.unmodifiableList(vertexCover);
            this.targetSize = targetSize;
        }

        private static String literalName(int literal) {
            return literal < 0 ? "NOT x" + -literal : "x" + literal;
        }
        public List<String> getVertices() { return vertices; }
        public List<int[]> getEdges() {
            List<int[]> copy = new ArrayList<int[]>();
            for (int[] edge : edges) copy.add(Arrays.copyOf(edge, edge.length));
            return copy;
        }
        public int[] getClique() { return clique == null ? null : Arrays.copyOf(clique, clique.length); }
        public List<Integer> getComplementIndependentSet() { return independentSet; }
        public List<Integer> getComplementVertexCover() { return vertexCover; }
        public int getTargetSize() { return targetSize; }
    }
}

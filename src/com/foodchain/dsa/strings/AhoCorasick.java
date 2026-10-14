package com.foodchain.dsa.strings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

/** Trie with failure links for finding many quality-warning terms in one report scan. */
public final class AhoCorasick {
    private final Node root = new Node();

    public AhoCorasick(List<String> keywords) {
        for (String keyword : keywords) addKeyword(keyword);
        buildFailureLinks();
    }

    public List<Match> search(String text) {
        List<Match> matches = new ArrayList<Match>();
        if (text == null || text.length() == 0) return matches;
        String normalizedText = text.toLowerCase(Locale.ROOT);
        Node state = root;
        for (int i = 0; i < normalizedText.length(); i++) {
            char current = normalizedText.charAt(i);
            while (state != root && !state.children.containsKey(current)) {
                state = state.failure;
            }
            Node next = state.children.get(current);
            state = next == null ? root : next;
            for (String keyword : state.outputs) {
                matches.add(new Match(keyword, i - keyword.length() + 1));
            }
        }
        return matches;
    }

    private void addKeyword(String keyword) {
        if (keyword == null || keyword.trim().length() == 0) return;
        String normalized = keyword.toLowerCase(Locale.ROOT);
        Node node = root;
        for (int i = 0; i < normalized.length(); i++) {
            char character = normalized.charAt(i);
            Node child = node.children.get(character);
            if (child == null) {
                child = new Node();
                node.children.put(character, child);
            }
            node = child;
        }
        node.outputs.add(normalized);
    }

    private void buildFailureLinks() {
        Queue<Node> queue = new ArrayDeque<Node>();
        root.failure = root;
        for (Node child : root.children.values()) {
            child.failure = root;
            queue.add(child);
        }

        while (!queue.isEmpty()) {
            Node parent = queue.remove();
            for (Map.Entry<Character, Node> edge : parent.children.entrySet()) {
                char character = edge.getKey();
                Node child = edge.getValue();
                Node fallback = parent.failure;
                while (fallback != root && !fallback.children.containsKey(character)) {
                    fallback = fallback.failure;
                }
                Node fallbackChild = fallback.children.get(character);
                child.failure = fallbackChild == null || fallbackChild == child ? root : fallbackChild;
                child.outputs.addAll(child.failure.outputs);
                queue.add(child);
            }
        }
    }

    private static final class Node {
        private final Map<Character, Node> children = new HashMap<Character, Node>();
        private final List<String> outputs = new ArrayList<String>();
        private Node failure;
    }

    public static final class Match {
        private final String keyword;
        private final int startIndex;

        private Match(String keyword, int startIndex) {
            this.keyword = keyword;
            this.startIndex = startIndex;
        }

        public String getKeyword() { return keyword; }
        public int getStartIndex() { return startIndex; }
    }
}

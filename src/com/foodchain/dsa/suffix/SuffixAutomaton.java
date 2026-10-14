package com.foodchain.dsa.suffix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Suffix automaton for linear-time substring-existence queries after building. */
public final class SuffixAutomaton {
    private final List<State> states = new ArrayList<State>();
    private int last;

    /** Builds the automaton in O(n) expected time and O(n) states. */
    public SuffixAutomaton(String text) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null.");
        states.add(new State());
        last = 0;
        for (int i = 0; i < text.length(); i++) extend(text.charAt(i));
    }

    /** Returns whether the indexed text contains the given non-empty substring. */
    public boolean contains(String pattern) {
        if (pattern == null || pattern.length() == 0) return false;
        int state = 0;
        for (int i = 0; i < pattern.length(); i++) {
            Integer next = states.get(state).transitions.get(pattern.charAt(i));
            if (next == null) return false;
            state = next.intValue();
        }
        return true;
    }

    private void extend(char character) {
        int current = states.size();
        State currentState = new State();
        currentState.length = states.get(last).length + 1;
        states.add(currentState);

        int p = last;
        while (p != -1 && !states.get(p).transitions.containsKey(character)) {
            states.get(p).transitions.put(character, current);
            p = states.get(p).link;
        }
        if (p == -1) {
            currentState.link = 0;
        } else {
            int q = states.get(p).transitions.get(character).intValue();
            if (states.get(p).length + 1 == states.get(q).length) {
                currentState.link = q;
            } else {
                State clone = new State();
                clone.length = states.get(p).length + 1;
                clone.link = states.get(q).link;
                clone.transitions.putAll(states.get(q).transitions);
                int cloneIndex = states.size();
                states.add(clone);
                while (p != -1) {
                    Integer target = states.get(p).transitions.get(character);
                    if (target == null || target.intValue() != q) break;
                    states.get(p).transitions.put(character, cloneIndex);
                    p = states.get(p).link;
                }
                states.get(q).link = cloneIndex;
                currentState.link = cloneIndex;
            }
        }
        last = current;
    }

    private static final class State {
        private int length;
        private int link = -1;
        private final Map<Character, Integer> transitions = new HashMap<Character, Integer>();
    }
}

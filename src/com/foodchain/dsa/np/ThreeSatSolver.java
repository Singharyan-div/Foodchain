package com.foodchain.dsa.np;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Small-instance DPLL-style solver for 3-CNF supply constraints. */
public final class ThreeSatSolver {
    private ThreeSatSolver() { }

    public static Result solve(int variableCount, List<Clause> clauses) {
        if (variableCount < 0 || variableCount > 40 || clauses == null) {
            throw new IllegalArgumentException("Use a formula with 0..40 variables and a clause list.");
        }
        for (Clause clause : clauses) {
            for (int literal : clause.literals) {
                if (literal == 0 || Math.abs(literal) > variableCount) {
                    throw new IllegalArgumentException("Clause literal is outside the declared variable range.");
                }
            }
        }
        byte[] assignment = new byte[variableCount + 1];
        boolean satisfiable = search(assignment, clauses);
        if (!satisfiable) return new Result(false, new boolean[variableCount + 1]);
        boolean[] values = new boolean[variableCount + 1];
        for (int i = 1; i < values.length; i++) values[i] = assignment[i] > 0;
        return new Result(true, values);
    }

    private static boolean search(byte[] assignment, List<Clause> clauses) {
        int variable = chooseVariable(assignment, clauses);
        if (variable == -1) return true;
        assignment[variable] = 1;
        if (canStillSatisfy(assignment, clauses) && search(assignment, clauses)) return true;
        assignment[variable] = -1;
        if (canStillSatisfy(assignment, clauses) && search(assignment, clauses)) return true;
        assignment[variable] = 0;
        return false;
    }

    private static int chooseVariable(byte[] assignment, List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (isSatisfied(clause, assignment)) continue;
            for (int literal : clause.literals) {
                int variable = Math.abs(literal);
                if (assignment[variable] == 0) return variable;
            }
        }
        return -1;
    }

    private static boolean canStillSatisfy(byte[] assignment, List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (isSatisfied(clause, assignment)) continue;
            boolean hasUnassigned = false;
            for (int literal : clause.literals) {
                if (assignment[Math.abs(literal)] == 0) {
                    hasUnassigned = true;
                    break;
                }
            }
            if (!hasUnassigned) return false;
        }
        return true;
    }

    private static boolean isSatisfied(Clause clause, byte[] assignment) {
        for (int literal : clause.literals) {
            byte value = assignment[Math.abs(literal)];
            if ((literal > 0 && value > 0) || (literal < 0 && value < 0)) return true;
        }
        return false;
    }

    public static final class Clause {
        private final int[] literals;

        public Clause(int first, int second, int third) {
            literals = new int[] { first, second, third };
        }

        public int[] getLiterals() { return Arrays.copyOf(literals, literals.length); }
        public String toString() { return "(" + literalName(literals[0]) + " OR "
                + literalName(literals[1]) + " OR " + literalName(literals[2]) + ")"; }

        private static String literalName(int literal) {
            return literal < 0 ? "NOT x" + -literal : "x" + literal;
        }
    }

    public static final class Result {
        private final boolean satisfiable;
        private final boolean[] values;

        private Result(boolean satisfiable, boolean[] values) {
            this.satisfiable = satisfiable;
            this.values = Arrays.copyOf(values, values.length);
        }

        public boolean isSatisfiable() { return satisfiable; }
        public boolean valueOf(int variable) {
            if (variable <= 0 || variable >= values.length) {
                throw new IllegalArgumentException("Variable index is outside the assignment.");
            }
            return values[variable];
        }
        public List<Boolean> getValues() {
            List<Boolean> copy = new ArrayList<Boolean>();
            for (int i = 1; i < values.length; i++) copy.add(values[i]);
            return Collections.unmodifiableList(copy);
        }
    }
}

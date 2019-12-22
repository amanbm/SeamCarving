package com.example.seamcarving;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class AStarSolver<Vertex> {

    private AStarGraph<Vertex> input;
    private List<Vertex> solution;
    private Vertex end;
    private ExtrinsicMinPQ<Vertex> explore;
    private Map<Vertex, Vertex> toParent;
    private Map<Vertex, Double> toWeight;
    private int numStatesExplored;
    private Set<Vertex> exists;

    /**
     * Immediately solves and stores the result of running memory optimized A*
     * search, computing everything necessary for all other methods to return
     * their results in constant time. The timeout is given in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.input = input;
        this.solution = new ArrayList<>();
        this.toParent = new HashMap<>();
        this.toWeight = new HashMap<>();
        this.explore = new ArrayHeapMinPQ<>();
        this.end = end;
        this.exists = new HashSet<>();

        toParent.put(start, start);
        toWeight.put(start, 0.0);

        solve(start);
    }

    private void solve(Vertex curr) {

        explore.add(curr, 0);

        while (!explore.isEmpty() && !curr.equals(end)) {
            curr = explore.removeSmallest();
            numStatesExplored++;
            for (WeightedEdge<Vertex> currEdge : input.neighbors(curr)) {
                Vertex next = currEdge.to();
                double weight = toWeight.get(curr) + currEdge.weight();
                double heuristic = input.estimatedDistanceToGoal(next, end);

                if (toWeight.containsKey(next)) {
                    if (toWeight.get(next) > weight) {
                        explore.changePriority(next, weight + heuristic);
                        toWeight.put(next, weight);
                        toParent.put(next, curr);
                    }
                } else {
                    toParent.put(next, curr);
                    toWeight.put(next, weight);
                }
                if (!explore.contains(next) && !exists.contains(next)) {
                    explore.add(next, weight + heuristic);
                }
            }
            exists.add(curr);
        }

        //create solution
        if (curr.equals(end)) {
            createSolution(curr);
        }
    }

    /**
     * Returns one of SOLVED, TIMEOUT, or UNSOLVABLE.
     */
    public boolean solved() {
        if (solution == null || solution.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * A list of vertices corresponding to the solution, from start to end.
     * Returns an empty list if problem was unsolvable or solving timed out.
     */
    public List<Vertex> solution() {
        return solution;
    }

    /**
     * The total weight of the solution, taking into account edge weights.
     * Returns Double.POSITIVE_INFINITY if problem was unsolvable or solving timed out.
     */
    public double solutionWeight() {
        if (solved()) {
            return toWeight.get(end);
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * The total number of priority queue removeSmallest operations.
     */
    public int numStatesExplored() {
        return numStatesExplored;
    }

    private void createSolution(Vertex curr) {
        toWeight.put(end, toWeight.get(curr));
        while (!toParent.get(curr).equals(curr)) {
            solution.add(0, curr);
            curr = toParent.get(curr);
        }
        solution.add(0, curr);
    }
}

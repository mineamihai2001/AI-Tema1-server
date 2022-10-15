package heuristics;

import core.State;

/**
 * Is an estimate that says how further away we are from our target...the closer we are,the lower the cost given
 * A minimum cost has to be associated with the final state
 * Should be at least an admissible heuristic(doesn't overestimate the distance from a state and the goal state)
 */
public interface Heuristic {
    double evaluate( State state);
    double getMaximumValue();
    double getMinimumValue();
}

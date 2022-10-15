package strategy;

/**
 * Interface that describes what functions should a "strategy" contain(at the bare minimum)
 */
public interface Strategy {
    /**
     * Start function to start/run the strategy
     */
    void start();

    /**
     * Print the solution found ,or an appropriate message if there is no solution found
     */
    void printSolution();
}

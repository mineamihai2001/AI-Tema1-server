package heuristics;

import core.State;

/**
 * An admissible heuristic for distance...
 * Although it isn't so good since for most states it gives the same value
 */
public class DistanceHeuristic1 implements Heuristic {
    private final double maxValue=1e9;
    private final double minValue=0;
    /**
     * Idea(we note target value as k and n,m as the max capacities):
     * If k is equal to the current capacities,then the distance is 0
     * If k is equal to the max capacities,then the distance is 1(do the FILL operation)
     * Else,the distance should be at least 2
     * Thus,this heuristic should be admissible
     * @param state the current state
     * @return 0,1, or 2
     */
    @Override
    public double evaluate( State state ) {
        long c0=state.getCapacity1(),c1=state.getCapacity1(),n=state.getN(),m=state.getM(),k=state.getK();
        if(k==c0||k==c1){
            return 0;
        }

        if(k==0||k==n||k==m){
            return 1;
        }

        return 2;
    }

    @Override
    public double getMaximumValue() {
        return maxValue;
    }

    @Override
    public double getMinimumValue() {
        return minValue;
    }
}

package heuristics;

import core.State;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * So far,it seems to be useless :(
 */
public class Heuristic2 implements Heuristic {
    private final double maxValue=1e9;
    private final double minValue=0;
    @Override
    public double evaluate( State state ) {
        return min(abs(state.getCapacity0()-state.getK()),abs(state.getCapacity1()-state.getK()));
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

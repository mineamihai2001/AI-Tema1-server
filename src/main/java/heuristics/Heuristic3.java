package heuristics;

import core.State;

import static java.lang.Math.abs;

public class Heuristic3 implements Heuristic{
    private final double maxValue=1e9;
    private final double minValue=0;
    @Override
    public double evaluate( State state ) {
        return abs(state.getCapacity0()+state.getCapacity1()-state.getK());
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

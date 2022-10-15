package strategy;

import core.Move;
import core.MoveType;
import core.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements the Backtracking Strategy without using a set to remember the already visited states
 */
public class BacktrackingV2Strategy extends AbstractStrategy {

    private boolean foundSolution;

    //Moves used to get from the initial state to the final state
    private List<Move> listMoves;

    private final State initialState;

    private final int maxNrMoves;

    public BacktrackingV2Strategy(State initialState, int maxNrMoves) {
        this.initialState = initialState;
        this.maxNrMoves = maxNrMoves;
        strategyName = "BacktrackingV2";
    }


    private void bkt(State state, int movePosition) {
        //if the state is final
        if (state.isFinal()) {
            foundSolution = true;

            //remove moves saved after final state
            while (listMoves.size() - 1 >= movePosition) {
                listMoves.remove(listMoves.size() - 1);
            }
        } else if (movePosition < maxNrMoves && !foundSolution) {
            //select the next moves from this state that are valid and add them to the list of moves
            for (var move : listPossibleMoves) {
                if (State.moveIsValid(state, move)) {

                    //set the current move
                    listMoves.set(movePosition, move);

                    bkt(State.executeMove(state, move), movePosition + 1);
                }

                if (foundSolution) {
                    break;
                }
            }
        }
    }

    /**
     * Start the backtracking by initializing the variables
     */
    @Override
    public void start() {

        setHasSolution(initialState);

        if (hasSolution) {

            listMoves = new ArrayList<>();
            for (int i = 0; i < maxNrMoves; ++i) {
                listMoves.add(null);
            }

            foundSolution = false;

            bkt(initialState, 0);
        }
    }

    @Override
    public void printSolution() {

        System.out.println("\n\n" + strategyName + "\nStarted from: " + initialState);
        if (hasSolution) {
            if (foundSolution) {

                var state = initialState.copy();
                for (var move : listMoves) {
                    state = State.executeMove(state, move);
                    System.out.println("\nAfter " + move + " we get the state:\n" + state);
                }
                System.out.println("Is this state final? " + state.isFinal());
            } else {
                System.out.println("BacktrackingV2 strategy was not able to find a solution after generating all states using " + maxNrMoves + " transitions/moves from the initial state");
            }
        } else {
            System.out.println("The initial state from which we started doesn't have a solution");
        }
    }

    @Override
    public List<Map<Move, State>> getSolution() {
        List<Map<Move, State>> actions = new ArrayList<>();

        System.out.println("\n\n" + strategyName + "\nStarted from: " + initialState);
        Map<Move, State> initial = new HashMap<>();
        initial.put(new Move(MoveType.EMPTY, 0 ), initialState);
        actions.add(initial);

        if (hasSolution) {
            if (foundSolution) {

                var state = initialState.copy();
                for (var move : listMoves) {
                    Map<Move, State> result = new HashMap<>();
                    state = State.executeMove(state, move);
                    result.put(move, state);
                    actions.add(result);
                }
                System.out.println("Is this state final? " + state.isFinal());
            } else {
                System.out.println("BacktrackingV2 strategy was not able to find a solution after generating all states using " + maxNrMoves + " transitions/moves from the initial state");
            }
        } else {
            System.out.println("The initial state from which we started doesn't have a solution");
        }
        return actions;
    }
}
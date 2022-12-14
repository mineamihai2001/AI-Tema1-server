package com.server;

import core.Move;
import core.MoveType;
import core.State;
import heuristics.*;
import strategy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        testStateHasSolution();

//        executeBacktrackingV1Strategy( State.getInitialState( 5,3,4 ) );
//        executeBacktrackingV1Strategy( State.getInitialState( 5,3,0 ) );
//        executeBacktrackingV1Strategy( State.getInitialState( 6,8,3 ) );

//        executeBacktrackingV2Strategy( State.getInitialState( 5,3,4 ),6 );
//        executeBacktrackingV2Strategy( State.getInitialState( 5,3,1 ),8 );
//        executeBacktrackingV2Strategy( State.getInitialState( 5,3,4 ),5 );
//        executeBacktrackingV2Strategy( State.getInitialState( 6,8,3 ),20 );


//        executeBFSV2Strategy( State.getInitialState( 5,3,4 ) );
//        executeBFSV2Strategy( State.getInitialState( 5,3,1 ) );
//        executeBFSV2Strategy( State.getInitialState( 5,3,0 ) );
//        executeBFSV2Strategy( State.getInitialState( 6,2,3 ) );
//        executeBFSV2Strategy( State.getInitialState( 5,3,1 ) );
//        executeBFSV2Strategy( State.getInitialState( 5,3,2 ) );
//        executeBFSV2Strategy( State.getInitialState( 5,3,4 ) );

//        executeGreedyHillClimbingStrategy( State.getInitialState( 5,3,4 ),new Heuristic1(),1000,2,20 );
//        executeGreedyHillClimbingStrategy( State.getInitialState( 5,3,4 ),new Heuristic2(),1000,2,20 );

//        executeStochasticHillClimbingStrategy(State.getInitialState(4, 3, 1), new Heuristic2() , 400, 20);

//        executeBFSV2Strategy( State.getInitialState( 5,3,4 ) );
//        executeAStarV2Strategy( State.getInitialState( 5,3,4 ),new DistanceHeuristic1(),50000 );
//
//        executeBFSV2Strategy( State.getInitialState( 5,3,1 ) );
//        executeAStarV2Strategy( State.getInitialState( 5,3,1 ),new DistanceHeuristic1(),50000 );
//
//        executeBFSV2Strategy( State.getInitialState( 5,3,2 ) );
//        executeAStarV2Strategy( State.getInitialState( 5,3,2 ),new DistanceHeuristic1(),50000 );
//
//        executeBFSV2Strategy( State.getInitialState( 6,2,3 ) );
//        executeAStarV2Strategy( State.getInitialState( 6,2,3 ),new DistanceHeuristic1(),50000 );
    }

    public static void testStateHasSolution() {
        State state;
        state = State.getInitialState(5, 3, 4);
        System.out.println("Does " + state + " have a solution? " + state.hasSolution());

        state = State.getInitialState(3, 6, 2);
        System.out.println("Does " + state + " have a solution? " + state.hasSolution());

        state = State.getInitialState(5, 10, 11);
        System.out.println("Does " + state + " have a solution? " + state.hasSolution());

        state = State.getInitialState(2, 3, 3);
        System.out.println("Does " + state + " have a solution? " + state.hasSolution());
    }

    public static List<Map<Move, State>> executeBFSV2Strategy(State initialState) {
        Strategy strategy = new BFSV2Strategy(initialState);
        strategy.start();
        strategy.printSolution();
        return strategy.getSolution();
    }

    public static List<Map<Move, State>> executeBacktrackingV2Strategy(State initialState, int maxNrMoves) {
        Strategy strategy = new BacktrackingV2Strategy(initialState, maxNrMoves);
        strategy.start();
//        strategy.printSolution();
        return strategy.getSolution();
    }

    public static void executeBacktrackingV1Strategy(State initialState) {
        Strategy strategy = new BacktrackingV1Strategy(initialState);
        strategy.start();
        strategy.printSolution();
    }

    public static List<Map<Move, State>> executeGreedyHillClimbingStrategy(State initialState, Heuristic heuristic, int nrIterations, int maxDepth, int maxNrImprovements) {
        Strategy strategy = new GreedyHillClimbingStrategy(initialState, heuristic, nrIterations, maxDepth, maxNrImprovements);
        strategy.start();
        strategy.printSolution();
        return strategy.getSolution();
    }

    public static List<Map<Move, State>> executeStochasticHillClimbingStrategy(State initialState, Heuristic heuristic, int nrIterations, int maxNrImprovements) {
        Strategy strategy = new StochasticHillClimbingStrategy(initialState, heuristic, nrIterations, maxNrImprovements);
        strategy.start();
        strategy.printSolution();
        return strategy.getSolution();
    }

    public static List<Map<Move, State>> executeAStarV2Strategy(State initialState,Heuristic heuristic,int maxNrVisitedStates){
        Strategy strategy=new AStarV2Strategy( initialState,heuristic,maxNrVisitedStates );
        strategy.start();
        strategy.printSolution();
        return strategy.getSolution();
    }

    public static void test() {
        List<Move> listMoves = new ArrayList<>();
        listMoves.add(new Move(MoveType.FILL, 0));
        listMoves.add(new Move(MoveType.POUR, 0));
        listMoves.add(new Move(MoveType.EMPTY, 1));
        listMoves.add(new Move(MoveType.POUR, 0));
        listMoves.add(new Move(MoveType.FILL, 0));
        listMoves.add(new Move(MoveType.POUR, 0));

        applyOperations(5, 3, 4, listMoves);
    }

    /**
     * Computes the state starting from the initial state doing the following operations
     *
     * @param n         the max capacity of vase 0
     * @param m         the max capacity of vase 1
     * @param k         the target capacity in either vase
     * @param listMoves the list of moves that are going to be applied
     */
    public static void applyOperations(long n, long m, long k, List<Move> listMoves) {
        var state = State.getInitialState(n, m, k);
        System.out.println("\nOur initial state: " + state);

        for (Move move : listMoves) {
            state = State.executeMove(state, move);

            if (move.getMoveType() == MoveType.EMPTY || move.getMoveType() == MoveType.FILL) {
                System.out.println("\nAfter the operation " + move.getMoveType().name() + " for vase " + move.getVaseNumber() + " we have:\n" + state);
            } else {
                System.out.println("\nAfter the operation " + move.getMoveType().name() + " from vase " + move.getVaseNumber() + " to vase " + (1 - move.getVaseNumber()) + " we have:\n" + state);
            }

            if (state.isFinal()) {
                System.out.println("This state is final!");
            }
        }
    }
}

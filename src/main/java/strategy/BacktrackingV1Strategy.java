package strategy;

import core.Move;
import core.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements the Backtracking Strategy using a set for remembering visited states
 */
public class BacktrackingV1Strategy extends AbstractStrategy {

    //TODO:Better way to make bkt work without memorizing visited states?
    private Set<State> setVisitedStates;
    private boolean foundSolution;
    private boolean stop;

    private final static long maxNrVisitedStates=50000;

    //Moves used to get from the initial state to the final state
    private List<Move> listMoves;

    private final State initialState;

    public BacktrackingV1Strategy( State startState){
        initialState=startState;
        strategyName="BacktrackingV1";
    }

    /**
     * The backtracking function that takes a given state and goes to a new state based on the moves possible
     * @param state the state
     */
    private void bkt( State state){
        if(state.isFinal()){
            stop=true;
            foundSolution=true;
        }
        else if(setVisitedStates.size()>maxNrVisitedStates){
            stop=true;
        }
        else if(!stop){
            //we visited this state
            setVisitedStates.add( state );

            //for each possible move,see where it goes
            for(var move: listPossibleMoves ){
                if(State.moveIsValid( state,move )) {

                    var newState = State.executeMove( state , move );

                    //if it's an unvisited state,go to it
                    if (!setVisitedStates.contains( newState )) {
                        setVisitedStates.add( newState );

                        listMoves.add( move );

                        bkt( newState );

                        //if we found a solution,stop
                        if (stop) {
                            break;
                        }

                        listMoves.remove( listMoves.size() - 1 );
                    }
                }
            }
        }
    }

    /**
     * Start the backtracking by initializing the variables
     */
    @Override
    public void start() {

        setHasSolution( initialState );

        if(hasSolution){
            setVisitedStates=new HashSet<>();
            listMoves=new ArrayList<>();
            foundSolution=stop=false;

            bkt(initialState);
        }
    }

    @Override
    public void printSolution() {

        System.out.println( "\n\n"+strategyName+"\nStarted from: " + initialState );
        if(hasSolution) {
            if (foundSolution) {

                var state = initialState.copy();
                for ( var move : listMoves ) {
                    state = State.executeMove( state , move );
                    System.out.println( "\nAfter " + move + " we get the state:\n" + state );
                }
                System.out.println( "Is this state final? " + state.isFinal() );
            } else {
                System.out.println( "BacktrackingV1 was not able to find a solution after visiting more than " + maxNrVisitedStates + " states " );
            }
        }
        else{
            System.out.println("The initial state from which we started doesn't have a solution");
        }
    }
}

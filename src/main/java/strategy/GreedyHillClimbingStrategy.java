package strategy;

import core.Move;
import core.MoveType;
import core.State;
import heuristics.Heuristic;

import java.util.*;

/**
 * Represents the HillClimbingStrategy
 */
public class GreedyHillClimbingStrategy extends AbstractStrategy {

    private List<Move> listMoves;
    private final State initialState;

    private final Heuristic heuristic;
    private final int nrIterations;
    //the max number of random moves selected to start from a random state
    private final int maxDepth;
    //the max number of improvements using the heuristic from a state
    private final int maxNrImprovements;

    private boolean foundSolution;

    public GreedyHillClimbingStrategy( State initialState , Heuristic heuristic , int nrIterations , int maxDepth , int maxNrImprovements ) {
        this.initialState = initialState;
        this.heuristic = heuristic;
        this.nrIterations = nrIterations;
        this.maxDepth = maxDepth;
        this.maxNrImprovements = maxNrImprovements;

        this.strategyName="Greedy Hill Climbing";
    }

    @Override
    public void start() {

        setHasSolution( initialState );

        if(hasSolution) {

            listMoves = new ArrayList<>();
            foundSolution = false;

            execute();
        }
    }

    /**
     * Generates a list of random moves ,at most having maxDepth moves
     * @return a list of random moves
     */
    private List<Move> generateRandomMoves(){
        List<Move> randomMoves=new ArrayList<>();
        Random random=new Random();
        State currentState=initialState;
        for(int i=0;i<maxDepth;++i){
            int moveIndex;
            do{
                moveIndex=random.nextInt(listPossibleMoves.size());
            }
            while (!State.moveIsValid( currentState,listPossibleMoves.get( moveIndex ) ));

            var move=listPossibleMoves.get( moveIndex );

            currentState=State.executeMove( currentState, move);
            randomMoves.add( move );
        }
        return randomMoves;
    }

    /**
     * Executes the HillClimbing algorithm
     */
    private void execute(){

        //If the initial state is final,end here
        if(initialState.isFinal()){
            foundSolution=true;
            return;
        }

        //Start the iteration
        for(int nrIteration=0;nrIteration<nrIterations&&!foundSolution;++nrIteration){

            //Begin from a random state based on choosing some random moves from the initial state
            State state=initialState;
            List<Move> listMoves=generateRandomMoves();
            for(int i=0;i<listMoves.size();++i){

                var move=listMoves.get( i );

                if(State.moveIsValid( state,move )){
                    state=State.executeMove( state,move );

                    //If we managed to get to a final state going randomly,end here
                    if(state.isFinal()){
                        foundSolution=true;
                        while(listMoves.size()>i+1){
                            listMoves.remove( listMoves.size()-1 );
                        }
                        this.listMoves=listMoves;

                        return;
                    }
                }
            }

            //Start the improvements using the heuristic
            double bestScore,currentScore;
            bestScore=heuristic.evaluate( state );

            int nrImprovements=0;
            do{
                //choose the best move which gives the smallest score using the heuristic
                Move bestMove=null;
                currentScore=heuristic.getMaximumValue();
                for(var move:listPossibleMoves){
                    if(State.moveIsValid( state,move )) {
                        State newState = State.executeMove( state , move );
                        double newScore = heuristic.evaluate( newState );
                        if (newScore < currentScore) {
                            currentScore = newScore;
                            bestMove = move;
                        }
                    }
                }

                //no move is available or too bad
                if(currentScore == heuristic.getMaximumValue()){
                    break;
                }

                //if there is an improvement,save the move and update the current state
                if(bestScore>=currentScore){
                    assert bestMove != null;
                    bestScore=currentScore;
                    state=State.executeMove( state, bestMove );
                    listMoves.add( bestMove );
                }

                //if it's the minimum possible value,that should mean it's a final state
                if(bestScore == heuristic.getMinimumValue()){
                    break;
                }

                ++nrImprovements;
            }
            while(bestScore>=currentScore&&nrImprovements<=maxNrImprovements);

            if(state.isFinal()){
                foundSolution=true;
                this.listMoves=listMoves;
            }
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
            }
            else{
                System.out.println(strategyName+" strategy was not able to find a solution");
            }
        }
        else{
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
                System.out.println(strategyName+" strategy was not able to find a solution after generating all states using " + nrIterations + " transitions/moves from the initial state");
            }
        } else {
            System.out.println("The initial state from which we started doesn't have a solution");
        }
        return actions;
    }
}

package strategy;

import core.Move;
import core.State;
import heuristics.Heuristic;

import java.util.ArrayList;
import java.util.List;

public class StochasticHillClimbingStrategy extends AbstractStrategy{

    private List<Move> listMoves;
    private final State initialState;

    private final Heuristic heuristic;
    private final int nrIterations;
    private final int maxNrImprovements;

    private boolean foundSolution;

    public StochasticHillClimbingStrategy( State initialState , Heuristic heuristic , int nrIterations,int maxNrImprovements ) {
        this.initialState = initialState;
        this.heuristic = heuristic;
        this.nrIterations = nrIterations;
        this.maxNrImprovements = maxNrImprovements;

        this.strategyName="Stochastic Hill Climbing";
    }

    @Override
    public void start() {

        setHasSolution( initialState );

        if(hasSolution){
            foundSolution=false;
            listMoves=new ArrayList<>();

            execute();
        }
    }

    /**
     * Choose a move based on probabilities.The idea is to assign a higher probability to the moves that have a lower score
     * in such a way that each moves probability sums up to 1
     * @param currentState the current state
     * @param listChosenMoves the list of moves to choose from
     * @return the chosen move based on the probabilities
     */
    private Move chooseMove(State currentState,List<Move> listChosenMoves){
        int nrMoves=listChosenMoves.size();

        double[] sumProbabilities=new double[nrMoves];

        double score,sum=0;
        for(int i=0;i<nrMoves;++i){
            //get the score
            score=heuristic.getMaximumValue()-heuristic.evaluate( State.executeMove( currentState,listChosenMoves.get( i ) ) );

            sum+=score;

            sumProbabilities[i]=(i==0?score:sumProbabilities[i-1]+score);
        }

        //make the values between 0 and 1
        for(int i=0;i<nrMoves;++i){
            sumProbabilities[i]/=sum;
        }
        //to make sure that the last moves has the summed probabilities to 1
        sumProbabilities[nrMoves-1]=1.0;

        //the probability of the chosen move
        double chance=Math.random();
        for(int i=0;i<nrMoves;++i){
            if(chance<=sumProbabilities[i]){
                return listChosenMoves.get( i );
            }
        }

        //should never be reached
        return null;
    }

    private void execute(){

        //If the initial state is final,end here
        if(initialState.isFinal()){
            foundSolution=true;
            return;
        }

        //Start the iteration
        for(int nrIteration=0;nrIteration<nrIterations&&!foundSolution;++nrIteration){

            int nrImprovements=0;

            State currentState=initialState;

            List<Move> listChosenMoves=new ArrayList<>();

//            System.out.println("\n\nIteration: "+nrIteration);

            while(nrImprovements<maxNrImprovements){

                List<Move> listGoodMoves=new ArrayList<>();

                var currentScore=heuristic.evaluate( currentState );

                for(var move:listPossibleMoves){

                    if(!State.moveIsValid( currentState,move )){
                        continue;
                    }

                    //check to see if move leads to a final state
                    if(State.executeMove( currentState,move ).isFinal()){
                        //if yes,save this as the only move available
                        listGoodMoves.clear();
                        listGoodMoves.add( move );
                        break;
                    }

                    //leads to an equal of better state,add it
                    if(heuristic.evaluate( State.executeMove( currentState,move ) )<=currentScore ){
                        listGoodMoves.add( move );
                    }
                }

//                System.out.println("\nImprovement number "+nrImprovements+" from state "+currentState+" has good moves:");
//                System.out.println(listGoodMoves);

                if(listGoodMoves.size()==0){
                    break;
                }

                var nextMove=chooseMove(currentState,listGoodMoves);
                if(nextMove==null){
                    System.out.println("A grave error has occurred");
                    System.exit( 0 );
                }

                currentState=State.executeMove( currentState,nextMove );
                listChosenMoves.add( nextMove );

//                System.out.println("Next move = "+nextMove+",new state = "+currentState);

                if(currentState.isFinal()){
                    foundSolution=true;
                    listMoves=listChosenMoves;
                    break;
                }

                ++nrImprovements;
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
}

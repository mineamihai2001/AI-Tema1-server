package strategy;

import core.Move;
import core.MoveType;
import core.State;

import java.util.*;

public class BFSV2Strategy extends AbstractStrategy {

    private final static int maxVisitedStates=50000;

    //Represents a "node" or more like a state and a move from that state,used to get the moves from a final state to the initial state
    static class Node {
        private final State previousState;
        private final Move move;

        public Node( State previousState , Move move ) {
            this.previousState = previousState;
            this.move = move;
        }

        public State getPreviousState() {
            return previousState;
        }

        public Move getMove() {
            return move;
        }
    }

    //the queue
    private Queue<State> queue;
    //a map that associates for a state the previous state from where it comes and the moves from that previous state
    private Map<State, Node> previousNode;
    private final State initialState;

    private boolean foundSolution;
    private List<Move> listMoves;

    public BFSV2Strategy( State initialState ) {
        this.initialState = initialState;
        strategyName="BFSV2";
    }

    void bfs(){
        State finalState=null;

        if(initialState.isFinal()){
            finalState=initialState;
            foundSolution=true;
        }
        else {
            boolean stop=false;

            previousNode.put( initialState , new Node( null , null ) );
            queue.add( initialState );

            while (!queue.isEmpty() && !stop) {

                //extract and remove first element
                State state = queue.poll();

                if (state == null || previousNode.size() > maxVisitedStates) {
                    stop=true;
                } else {
                    //see neighboring states and if a neighbor is new,add it to the map and queue
                    for ( var move : listPossibleMoves ) {
                        if (State.moveIsValid( state , move )) {
                            State newState = State.executeMove( state , move );
                            if (!previousNode.containsKey( newState )) {
                                previousNode.put( newState , new Node( state , move ) );
                                queue.add( newState );
                            }

                            if (newState.isFinal()) {
                                foundSolution = stop = true;
                                finalState = newState;
                                break;
                            }
                        }
                    }
                }
            }
        }

        //if we found a final state,go backwards using previousNode to get the list of moves
        if(foundSolution){
            computeListMoves( finalState );
        }
    }

    /**
     * Computes the list of moves from the initial state to the given finalState using the previousNode map
     * @param finalState the final state
     */
    private void computeListMoves(State finalState){
        listMoves=new ArrayList<>();
        Node node;

        //while we are not at the initial state
        while(!initialState.equals( finalState )){
            //get previous state and move
            node=previousNode.get( finalState );

            //add move
            listMoves.add( node.getMove() );

            //go to the previous state
            finalState=node.getPreviousState();
        }
        Collections.reverse( listMoves );
    }

    /**
     * Start function to start/run the strategy
     */
    @Override
    public void start() {

        setHasSolution( initialState );

        if(hasSolution) {

            foundSolution = false;
            previousNode = new HashMap<>();

            queue = new LinkedList<>();

            bfs();
        }
    }

    /**
     * Print the solution found ,or an appropriate message if there is no solution found
     */
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
                System.out.println( "BFS was not able to find a solution after visiting more than " + maxVisitedStates + " states " );
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
                    System.out.println(">>>>>>>" + result);
                    actions.add(result);
                }
                System.out.println("Is this state final? " + state.isFinal());
            } else {
                System.out.println(strategyName+" strategy was not able to find a solution after generating all states using " + maxVisitedStates + " transitions/moves from the initial state");
            }
        } else {
            System.out.println("The initial state from which we started doesn't have a solution");
        }
        System.out.println(actions);
        return actions;
    }
}

package strategy;

import core.Move;
import core.MoveType;
import core.State;
import heuristics.Heuristic;

import java.util.*;

public class AStarV2Strategy extends AbstractStrategy{

    private final State initialState;
    private final Heuristic heuristic;
    private final int maxNrVisitedStates;

    private List<Move> listMoves;
    private boolean foundSolution;

    //used to choose the next state that has the smallest current cost in the queue
    private PriorityQueue<Node> priorityQueue;

    //for each state,keeps the previous state and the move to execute from that state to get the the current state
    private Map<State,Direction> before;
    //keeps the distance from the initial state form each state and the best cost associated to a state at a time
    private Map<State,Double> distanceFromInitialState,currentCost;
    //all visited/explored states
    private Set<State> exploredStates;

    /**
     * Useful for reconstructing the solution
     */
    static class Direction {
        private final State previousState;
        private final Move move;

        public Direction( State previousState , Move move ) {
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

    /**
     * Used to associate a state to a given cost, so it can be used with a priority queue
     */
    static class Node implements Comparable<Node>{
        private final State state;
        private final Double cost;

        public Node( State state , Double cost ) {
            this.state = state;
            this.cost = cost;
        }

        public State getState() {
            return state;
        }

        public Double getCost() {
            return cost;
        }

        @Override
        public boolean equals( Object o ) {
            if (this == o) return true;
            if (!( o instanceof Node node )) return false;
            return getState().equals( node.getState() ) && getCost().equals( node.getCost() );
        }

        @Override
        public int hashCode() {
            return Objects.hash( getState() , getCost() );
        }

        @Override
        public int compareTo( Node o ) {
            double cost=this.getCost()-o.getCost();
            return cost<0?-1:(cost>0?1:0);
        }
    }


    public AStarV2Strategy( State initialState , Heuristic heuristic,int maxNrVisitedStates ) {
        this.initialState = initialState;
        this.heuristic = heuristic;
        this.maxNrVisitedStates=maxNrVisitedStates;

        this.strategyName="A* V2";
    }

    private void addUnexploredNeighbors(State currentState){

//        System.out.println("\nExplore neighbors for "+currentState);

        for(var move:listPossibleMoves){
            //if it not a valid state,skip
            if(!State.moveIsValid( currentState,move )){
                continue;
            }

            //if it's a new state,update the cost,distance and who is before this state
            var neighborState=State.executeMove( currentState,move );
            if(!exploredStates.contains( neighborState )){

//                System.out.println("Unexplored neighbor: "+neighborState);

                //compute the distance from the initial state
                distanceFromInitialState.put( neighborState,distanceFromInitialState.get( currentState ) + 1 );
                //add who is before him,useful to compute the solution
                before.put( neighborState,new Direction( currentState,move ) );

                //compute cost
                double cost=distanceFromInitialState.get( neighborState )+heuristic.evaluate( neighborState );
                if(!currentCost.containsKey( neighborState )){
                    //if it's a new unexplored
                    currentCost.put( neighborState,cost );
                    priorityQueue.add( new Node( neighborState, cost) );
                }
                else {
                    //still unexplored,but already added to the priority queue

                    double currentCost = this.currentCost.get( neighborState );
                    //replace with the lower cost in the priority queue
                    if (cost < currentCost) {
                        this.currentCost.put( neighborState,cost );
                        priorityQueue.remove( new Node( neighborState , currentCost ) );
                        priorityQueue.add( new Node(neighborState,cost) );
                    }
                }
            }
        }
    }

    private void execute(){

        if(initialState.isFinal()){
            foundSolution=true;
            return;
        }

        before.put( initialState,null );
        distanceFromInitialState.put( initialState,0D );
        currentCost.put( initialState,0D );

        State finalState=null;
        double bestCost =heuristic.getMaximumValue();
        addUnexploredNeighbors( initialState );

        do{
            var node=priorityQueue.poll();
            assert node != null;

//            System.out.println("Current best node: "+node.getState()+" with cost "+node.getCost());

            //if the current cost is bigger to the best score,no point to continue
            var currentCost=node.getCost();
            if(currentCost>bestCost||exploredStates.size()>maxNrVisitedStates){
                break;
            }

            var state=node.getState();
            exploredStates.add( state );
            if(state.isFinal()){
                foundSolution=true;
                bestCost=this.currentCost.get( state );
                finalState=state;
            }

            addUnexploredNeighbors( state );
        }
        while(!priorityQueue.isEmpty());

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
        Direction node;

        //while we are not at the initial state
        while(!initialState.equals( finalState )){
            //get previous state and move
            node=before.get( finalState );

            //add move
            listMoves.add( node.getMove() );

            //go to the previous state
            finalState=node.getPreviousState();
        }
        Collections.reverse( listMoves );
    }

    @Override
    public void start() {

        setHasSolution( initialState );

        if(hasSolution){
            priorityQueue=new PriorityQueue<>();
            distanceFromInitialState=new HashMap<>();
            before=new HashMap<>();
            currentCost=new HashMap<>();
            exploredStates=new HashSet<>();
            listMoves=new ArrayList<>();

            execute();
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
                System.out.println(strategyName+" strategy was not able to find a solution ");
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
                System.out.println("BacktrackingV2 strategy was not able to find a solution after generating all states");
            }
        } else {
            System.out.println("The initial state from which we started doesn't have a solution");
        }
        return actions;
    }
}

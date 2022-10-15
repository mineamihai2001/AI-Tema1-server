package core;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class State {
    private long capacity0,capacity1;
    private final long n, m, k;

    public State(long capacity0,long capacity1,long n,long m,long k){
        this.capacity0=capacity0;
        this.capacity1=capacity1;

        this.n =n;
        this.m =m;
        this.k =k;
    }

    public static State getInitialState(long n,long m,long k){
        return new State( 0,0,n,m,k );
    }

    public boolean isFinal(){
        return capacity0==k||capacity1==k;
    }

    @Override
    public String toString() {
        return "State{" +
                "capacity0 = " + capacity0 +
                ", capacity1 = " + capacity1 +
                ", n = " + n +
                ", m = " + m +
                ", k = " + k +
                '}';
    }

    /**
     * Executes a move according to the move type for the given state
     * This is more of an "interface" that makes it easier to do a transitions
     * @param state the state for which this move/operation will be executed
     * @param move the move that contains the move type(FILL,EMPTY or POUR) and the vase number
     * @return the new state after the move is applied
     */
    public static State executeMove( State state, Move move){
        State newState=state.copy();

        var moveType=move.getMoveType();
        var nrVase=move.getVaseNumber();

        switch (moveType){
            case FILL:
                return newState.fill( nrVase );
            case EMPTY:
                return newState.empty( nrVase );
            case POUR:
                return newState.pour( nrVase );
            default:
                System.out.println("WARNING!Move type not recognized!");
                System.exit( 0 );
        }
        return null;
    }

    /**
     * Fill a vase to the max capacity
     * @param nrVase the vase which will be filled to the max capacity(0 if it's the first one,otherwise it's the second one)
     * @return the new state
     */
    private State fill(int nrVase){
        State newState=this.copy();
        if(nrVase==0){
            newState.setCapacity0( newState.getN() );
        }
        else{
            newState.setCapacity1( newState.getM() );
        }
        return newState;
    }

    /**
     * Empty a vase
     * Basically set the capacity of the vase to 0
     * @param nrVase the number of vase for which we empty the water(0 if it's the first one,otherwise it's the second one)
     * @return the new state after the operation
     */
    private State empty(int nrVase){
        State newState=this.copy();
        if(nrVase==0){
            newState.setCapacity0( 0 );
        }
        else{
            newState.setCapacity1( 0 );
        }
        return newState;
    }

    /**
     * Pour water from the vase with nrVase
     * @param nrVase The vase from where the water is poured(0 if it's the first one,otherwise it's the second one)
     * @return new state after the operation
     */
    private State pour(int nrVase){
        //compute the quantity of water that will be taken from one vase to the other
        //assume we pour from x to y,with n and m max capacity,then we can add to y at most min(x,m-y)
        long pouredWater=nrVase==0?min(this.getCapacity0(),this.getM()-this.getCapacity1()):min(this.getCapacity1(),this.getN()-this.getCapacity0()),x,y;

        //add/subtract the water accordingly
        x=(this.getCapacity0()+(nrVase==0?-pouredWater:pouredWater));
        y=(this.getCapacity1()+(nrVase==0?pouredWater:-pouredWater));

        //set new capacities
        State newState=this.copy();

        newState.setCapacity0( x );
        newState.setCapacity1( y );

        return newState;
    }

    /**
     * Check to see if after a move for the given state we get a different state
     * @param move the move
     * @return true if the new state after applying the move is different,false otherwise
     */
    public static boolean moveIsValid(State state,Move move){
        var moveType=move.getMoveType();
        var vaseNumber=move.getVaseNumber();
        if(moveType==MoveType.FILL){
            return !(vaseNumber==0?state.getCapacity0()==state.getN():state.getCapacity1()==state.getM());
        }
        else if(moveType==MoveType.EMPTY){
            return !(vaseNumber==0?state.getCapacity0()==0:state.getCapacity1()==0);
        }
        else if(moveType==MoveType.POUR){
            long pouredWater=(vaseNumber==0?min(state.getCapacity0(),state.getM()-state.getCapacity1()):min(state.getCapacity1(),state.getN()-state.getCapacity0()));
            return pouredWater!=0;
        }
        else{
            System.out.println("Unknown move type!");
            System.exit( 0 );
        }
        return false;
    }

    private long computeGCD(long a,long b){
        long r;
        while(b>0){
            r=a%b;
            a=b;
            b=r;
        }
        return a;
    }

    public boolean hasSolution(){
        long n=getN(),m=getM(),k=getK();
        if(k>max(n,m)){
            return Boolean.FALSE;
        }

        long gcd=computeGCD( n,m );
        if(gcd==0){
            return k==0;
        }

        return k%gcd==0;
    }

    public long getCapacity0() {
        return capacity0;
    }

    public long getCapacity1() {
        return capacity1;
    }

    public long getN() {
        return n;
    }

    public long getM() {
        return m;
    }

    public long getK() {
        return k;
    }

    public void setCapacity0( long capacity0 ) {
        this.capacity0 = capacity0;
    }

    public void setCapacity1( long capacity1 ) {
        this.capacity1 = capacity1;
    }

    public State copy(){
        return new State( this.capacity0,this.capacity1,this.n,this.m,this.k );
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (!( o instanceof State state )) return false;
        return getCapacity0() == state.getCapacity0() && getCapacity1() == state.getCapacity1() && getN() == state.getN() && getM() == state.getM() && getK() == state.getK();
    }

    @Override
    public int hashCode() {
        return Objects.hash( getCapacity0() , getCapacity1() , getN() , getM() , getK() );
    }
}

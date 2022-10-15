package strategy;

import core.Move;
import core.MoveType;
import core.State;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStrategy implements Strategy {
    protected final static List<Move> listPossibleMoves;

    protected boolean hasSolution;

    protected String strategyName;

    static {
        listPossibleMoves = new ArrayList<>();
        listPossibleMoves.add( new Move( MoveType.EMPTY,0 ) );
        listPossibleMoves.add( new Move( MoveType.EMPTY,1 ) );
        listPossibleMoves.add( new Move( MoveType.FILL,0 ) );
        listPossibleMoves.add( new Move( MoveType.FILL,1 ) );
        listPossibleMoves.add( new Move( MoveType.POUR,0 ) );
        listPossibleMoves.add( new Move( MoveType.POUR,1 ) );
    }

    protected void setHasSolution( State state){
        hasSolution=state.hasSolution();
    }
}

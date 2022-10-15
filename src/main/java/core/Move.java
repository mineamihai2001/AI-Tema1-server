package core;

import java.util.Objects;

/**
 * Represents a move or an operation so that we can go from the current state to the next state
 */
public class Move {
    private final MoveType moveType;
    private final int vaseNumber;

    public Move( MoveType moveType , int vaseNumber ) {
        this.moveType = moveType;
        this.vaseNumber = vaseNumber;
    }

    @Override
    public String toString() {
        return "Move{" +
                "moveType=" + moveType +
                ", vaseNumber=" + vaseNumber +
                '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (!( o instanceof Move move )) return false;
        return vaseNumber == move.vaseNumber && moveType == move.moveType;
    }

    @Override
    public int hashCode() {
        return Objects.hash( moveType , vaseNumber );
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public int getVaseNumber() {
        return vaseNumber;
    }

}

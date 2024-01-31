package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        //Chess Positions are initialized with board coordinates, but they are read as array coordinates
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return (row-1);
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return (col-1);
    }

    @Override
    public String toString() {
        return "R" + (getRow()+1) + "C" + (getColumn()+1);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;
        ChessPosition other = (ChessPosition) o;
        return (this.getRow() == other.getRow() && this.getColumn() == other.getColumn());
    }
    @Override
    public int hashCode() {
        return getRow()*53 + getColumn()*73;
    }
}

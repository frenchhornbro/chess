package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public static int BOARDSIZE = 8;
    private ChessPiece[][] squares = new ChessPiece[BOARDSIZE][BOARDSIZE];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public ChessPiece[][] getBoard() {
        return squares;
    }
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (squares[i][j] != null) {
                    if (!first) retStr.append("\n");
                    String addStr = squares[i][j].toString(); // + " at R" + (j+1) + "C" + (i+1);
                    retStr.append(addStr);
                    first = false;
                }
            }
        }
        return retStr.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;
        ChessBoard other = (ChessBoard) o;
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if(!squares[i][j].equals(other.getBoard()[i][j])) return false;
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int retVal = 1;
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                retVal *= squares[i][j].hashCode();
            }
        }
        return retVal;
    }
}

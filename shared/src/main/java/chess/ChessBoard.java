package chess;

import static chess.ChessPiece.PieceType.ROOK;
import static chess.ChessPiece.PieceType.BISHOP;
import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.QUEEN;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public final static int BOARDSIZE = 8;
    private ChessPiece[][] squares = new ChessPiece[BOARDSIZE][BOARDSIZE];
    public ChessBoard() {

    }

    public ChessBoard(boolean newGame) {
        if (newGame) resetBoard();
    }

    public ChessBoard(ChessPiece[][] inputtedBoard) {
        squares = inputtedBoard;
    }

    /**
     * Adds a chess piece to the chessboard
     * <p>
     * parameter position where to add the piece to
     * parameter piece    the piece to add
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
        ChessPiece.PieceType[] resetPieces = new ChessPiece.PieceType[]{ROOK, KNIGHT, BISHOP, KING, QUEEN, BISHOP, KNIGHT, ROOK};

        for (int i = 1; i <= BOARDSIZE; i++) {
            addPiece(new ChessPosition(1,i),new ChessPiece(ChessGame.TeamColor.WHITE, resetPieces[i-1]));
            addPiece(new ChessPosition(2,i),new ChessPiece(ChessGame.TeamColor.WHITE, PAWN));
            addPiece(new ChessPosition(7,i),new ChessPiece(ChessGame.TeamColor.BLACK, PAWN));
            addPiece(new ChessPosition(8,i),new ChessPiece(ChessGame.TeamColor.BLACK, resetPieces[i-1]));
        }
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (squares[i][j] != null) {
                    if (!first) retStr.append(",");
                    retStr.append(squares[i][j].toString());
                    retStr.append(" R");
                    retStr.append(i+1);
                    retStr.append("C");
                    retStr.append(j+1);
                    first = false;
                }
            }
            if (!first) {
                retStr.append("\n");
                first = true;
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
                if(squares[i][j] == null) {
                    if (other.getBoard()[i][j] != null) return false;
                }
                else if(!squares[i][j].equals(other.getBoard()[i][j])) return false;
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

package chess;

import chess.piecemovescalculators.BishopMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //System.out.println(board.toString());
        //System.out.println(myPosition.toString());
        switch (this.type.toString()) {
            case "KING":
                throw new RuntimeException("Not implemented");
            case "QUEEN":
                throw new RuntimeException("Not implemented");
            case "BISHOP":
                BishopMovesCalculator calculator = new BishopMovesCalculator();
                return calculator.pieceMoves(board, myPosition);
            case "KNIGHT":
                throw new RuntimeException("Not implemented");
            case "ROOK":
                throw new RuntimeException("Not implemented");
            case "PAWN":
                throw new RuntimeException("Not implemented");
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return getTeamColor().toString() + " " + getPieceType().toString();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;
        ChessPiece other = (ChessPiece) o;
        return (this.pieceColor == other.pieceColor && this.type == other.type);
    }
    @Override
    public int hashCode() {
        switch (this.type.toString()) {
            case "KING":
                if (this.pieceColor.toString().equals("WHITE")) return 1;
                return 2;
            case "QUEEN":
                if (this.pieceColor.toString().equals("WHITE")) return 3;
                return 4;
            case "BISHOP":
                if (this.pieceColor.toString().equals("WHITE")) return 5;
                return 6;
            case "KNIGHT":
                if (this.pieceColor.toString().equals("WHITE")) return 7;
                return 8;
            case "ROOK":
                if (this.pieceColor.toString().equals("WHITE")) return 9;
                return 10;
            case "PAWN":
                if (this.pieceColor.toString().equals("WHITE")) return 11;
                return 12;
        }
        return 13;
    }
}

package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = null;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    //Override the toString method here to be able to get better results
    @Override
    public String toString() {
        if (getPromotionPiece() == null) return getStartPosition().toString() + "->" + getEndPosition().toString();
        return getStartPosition().toString() + "->" + getEndPosition().toString() + "(" + getPromotionPiece() + ")";
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;
        ChessMove other = (ChessMove) o;
        return (this.hashCode() == other.hashCode());
    }
    @Override
    public int hashCode() {
        if (getPromotionPiece() == null) return (startPosition.getRow()*3 + startPosition.getColumn()*7 + endPosition.getRow()*11 + endPosition.getColumn()*19);
        int promotion = 132;
        switch (getPromotionPiece().toString()) {
            case "QUEEN":
                promotion = 23;
                break;
            case "ROOK":
                promotion = 37;
                break;
            case "BISHOP":
                promotion = 43;
                break;
            case "KNIGHT":
                promotion = 57;
        }
        return (startPosition.getRow() * startPosition.getColumn() * endPosition.getRow() * endPosition.getColumn() * promotion);
    }
}

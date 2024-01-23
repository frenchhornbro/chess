package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    public static int BOARDSIZE = 8;
    public KingMovesCalculator() {

    }

    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <=2; j++) {
                if (i != 1 || j != 1) {
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow()+i,startPosition.getColumn()+j);
                    if (validMove(startPosition, endPosition, board)) possibleMoves.add(new ChessMove(startPosition,endPosition));
                }
            }
        }
        return possibleMoves;
    }
    private boolean validMove(ChessPosition startPos, ChessPosition endPos, ChessBoard board) {
        if(endPos.getRow() >= 0 && endPos.getColumn() >= 0 && endPos.getRow() < BOARDSIZE && endPos.getColumn() < BOARDSIZE){
            if (board.getPiece(endPos) == null) return true; //This is a blank tile
            if (board.getPiece(endPos).getTeamColor() == board.getPiece(startPos).getTeamColor()) return false; //This is a friendly piece
            return true; //This is an enemy piece
        }
        return false; //This is outside the boundaries of the board
    }
}


package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public static int BOARDSIZE = 8;
    public KnightMovesCalculator() {

    }

    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //Remember that for creating new ChessPositions using the .getRow and .getColumn methods of a ChessPosition,
        //you have to add 1 to parameters given to get into array format
        int[] input1 = new int[]{-1,-1,0,0,2,2,3,3};
        int[] input2 = new int[]{0,2,-1,3,-1,3,0,2};
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(startPosition.getRow()+input1[i],startPosition.getColumn()+input2[i]);
            if (validMove(startPosition, endPosition, board)) possibleMoves.add(new ChessMove(startPosition,endPosition));
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


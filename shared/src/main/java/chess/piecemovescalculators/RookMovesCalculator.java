package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    public static int BOARDSIZE = 8;
    public RookMovesCalculator() {

    }

    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves = addMoves(possibleMoves,startPosition, board,true,true);
        possibleMoves = addMoves(possibleMoves,startPosition, board,true,false);
        possibleMoves = addMoves(possibleMoves,startPosition, board,false,false);
        possibleMoves = addMoves(possibleMoves,startPosition, board,false,true);
        return possibleMoves;
    }
    private ArrayList<ChessMove> addMoves(ArrayList<ChessMove> possibleMoves, ChessPosition startPos, ChessBoard board, boolean isRow, boolean isAdd) {
        int x = startPos.getRow();
        int y = startPos.getColumn();
        boolean cont = true;
        int i = 0;
        while(cont) {
            i++;
            int testX, testY;
            if (isRow) {
                testX = isAdd ? x+i : x-i;
                testY = y;
            }
            else {
                testX = x;
                testY = isAdd ? y+i : y-i;
            }
            ChessPosition endPos = new ChessPosition(testX+1, testY+1);
            boolean[] movesValid = validMove(startPos, endPos, board);
            if (!movesValid[0]) cont = false;
            else {
                ChessMove validBishopMove = new ChessMove(startPos,endPos);
                possibleMoves.add(validBishopMove);
                if (!movesValid[1]) cont = false;
            }
        }
        return possibleMoves;
    }
    private boolean[] validMove(ChessPosition startPos, ChessPosition endPos, ChessBoard board) {
        if(endPos.getRow() >= 0 && endPos.getColumn() >= 0 && endPos.getRow() < BOARDSIZE && endPos.getColumn() < BOARDSIZE){
            //Format of the return arrays: [THIS IS A VALID PLACE TO GO TO, YOU ARE ABLE TO CONTINUE]
            if (board.getPiece(endPos) == null) return new boolean[]{true, true}; //This is a blank tile
            if (board.getPiece(endPos).getTeamColor() == board.getPiece(startPos).getTeamColor()) return new boolean[]{false, false}; //This is a friendly piece
            return new boolean[]{true, false}; //This is an enemy piece
        }
        return new boolean[]{false, false}; //This is outside the boundaries of the board
    }
}
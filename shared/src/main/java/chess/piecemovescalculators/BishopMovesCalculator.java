package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public BishopMovesCalculator () {

    }
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPos) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        addMoves(possibleMoves,startPos, board,true,true);
        addMoves(possibleMoves,startPos, board,true,false);
        addMoves(possibleMoves,startPos, board,false,false);
        addMoves(possibleMoves,startPos, board,false,true);
        return possibleMoves;
    }
    private void addMoves(ArrayList<ChessMove> possibleMoves, ChessPosition startPos, ChessBoard board, boolean xAdd, boolean yAdd) {
        int x = startPos.getRow();
        int y = startPos.getColumn();
        boolean cont = true;
        int i = 0;
        while(cont) {
            i++;
            int testX = xAdd? x+i : x-i;
            int testY = yAdd? y+i : y-i;
            ChessPosition endPos = new ChessPosition(testX+1, testY+1);
            boolean[] movesValid = isValidMove(startPos, endPos, board);
            if (!movesValid[0]) cont = false;
            else {
                ChessMove validBishopMove = new ChessMove(startPos,endPos);
                possibleMoves.add(validBishopMove);
                if (!movesValid[1]) cont = false;
            }
        }
    }

        //ChessBoard has a double array of ChessPieces. You can use board.getPiece() to tell if there's another piece in a spot.
        //The slots in the array can either be empty (no pieces) or not (there's a piece there)
    private boolean[] isValidMove(ChessPosition startPos, ChessPosition endPos, ChessBoard board) {
        if(endPos.getRow() >= 0 && endPos.getColumn() >= 0 && endPos.getRow() < BOARDSIZE && endPos.getColumn() < BOARDSIZE){
            //Format of the return arrays: [THIS IS A VALID PLACE TO GO TO, YOU ARE ABLE TO CONTINUE]
            if (board.getPiece(endPos) == null) return new boolean[]{true, true}; //This is a blank tile
            if (board.getPiece(endPos).getTeamColor() == board.getPiece(startPos).getTeamColor()) return new boolean[]{false, false}; //This is a friendly piece
            return new boolean[]{true, false}; //This is an enemy piece
        }
        return new boolean[]{false, false}; //This is outside the boundaries of the board
    }
}

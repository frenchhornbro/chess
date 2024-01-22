package chess.piecemovescalculators;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public static int BOARDSIZE = 8;
    public BishopMovesCalculator () {

    }
    public Collection<ChessMove> pieceMoves (chess.ChessBoard board, chess.ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves = addMoves(possibleMoves,position,true,true);
        possibleMoves = addMoves(possibleMoves,position,true,false);
        possibleMoves = addMoves(possibleMoves,position,false,true);
        possibleMoves = addMoves(possibleMoves,position,false,false);
        return possibleMoves;
    }
    private ArrayList<ChessMove> addMoves(ArrayList<ChessMove> possibleMoves, chess.ChessPosition position, boolean xAdd, boolean yAdd) {
        int x = position.getRow();
        int y = position.getColumn();
        ChessPosition startPosition = new ChessPosition(x,y);
        boolean cont = true;
        int i = 0;
        while(cont) {
            i++;
            int testX = xAdd? x+i : x-i;
            int testY = yAdd? y+i : y-i;
            if (!validMove(testX,testY)) cont = false;
            else {
                ChessPosition endPosition = new ChessPosition(testX,testY);
                ChessMove validBishopMove = new ChessMove(startPosition,endPosition);
                possibleMoves.add(validBishopMove);
            }
            //TODO: Don't continue if there's a piece in the way that can be captured
        }
        return possibleMoves;
    }

        //ChessBoard has a double array of ChessPieces. You can use board.getPiece() to tell if there's another piece in a spot.
        //The slots in the array can either be empty (no pieces) or not (there's a piece there)
    public boolean validMove(int x, int y) {
        if(x > 0 && y > 0 && x <= 8 && y <= 8){
            return true;    //TODO: don't stop here, add step two
        }
        else {
            return false;
        }
        //TODO: Second, implement:
        //Is there a piece in the way?
        //Check if there's a piece in that position on the board
        //If there is, only return true if it's the same color as the piece that's trying to move
        //Have the calculator above stop checking for more moves if there's a piece in the way
    }
}

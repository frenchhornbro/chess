package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public PawnMovesCalculator() {

    }

    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPos) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        boolean promote = false;
        if (board.getPiece(startPos).getTeamColor().toString().equals("WHITE")) {
            if (startPos.getRow() == 6) promote = true;
        }
        else if (startPos.getRow() == 1) promote = true;
        addSingleMove(possibleMoves, startPos, board, promote);
        addDoubleMove(possibleMoves, startPos, board);
        attack(possibleMoves, startPos, board, promote);
        return possibleMoves;
    }
    private void addSingleMove (ArrayList<ChessMove> possibleMoves, ChessPosition startPos, ChessBoard board, boolean promote) {
        int i = 0;
        if (board.getPiece(startPos).getTeamColor().toString().equals("WHITE")) i = 2;
        ChessPosition endPos = new ChessPosition(startPos.getRow()+i, startPos.getColumn()+1);
        if (isValidForwardMove(endPos, board)) {
            if (promote) addPromotions(possibleMoves, startPos, endPos);
            else possibleMoves.add(new ChessMove(startPos, endPos));
        }
    }
    private void addDoubleMove (ArrayList<ChessMove> possibleMoves, ChessPosition startPos, ChessBoard board) {
        int i = 0, j = -1;
        if (board.getPiece(startPos).getTeamColor().toString().equals("WHITE")) {
            if (startPos.getRow() != 1) return;
            i = 2;
            j = 3;
        }
        else if (startPos.getRow() != 6) return;
        ChessPosition endPos1 = new ChessPosition(startPos.getRow()+i, startPos.getColumn()+1);
        ChessPosition endPos2 = new ChessPosition(startPos.getRow()+j, startPos.getColumn()+1);
        if (isValidForwardMove(endPos1, board) && isValidForwardMove(endPos2, board)) possibleMoves.add(new ChessMove(startPos, endPos2));
    }
    private void attack(ArrayList<ChessMove> possibleMoves, ChessPosition startPos, ChessBoard board, boolean promote) {
        int i = 0;
        if (board.getPiece(startPos).getTeamColor().toString().equals("WHITE")) i = 2;
        ChessPosition endPos1 = new ChessPosition(startPos.getRow() + i, startPos.getColumn());
        ChessPosition endPos2 = new ChessPosition(startPos.getRow() + i, startPos.getColumn() + 2);
        if (isValidAttack(endPos1, startPos, board)) {
            if (promote) addPromotions(possibleMoves, startPos, endPos1);
            else possibleMoves.add(new ChessMove(startPos, endPos1));
        }
        if (isValidAttack(endPos2, startPos, board)) {
            if (promote) addPromotions(possibleMoves, startPos, endPos2);
            else possibleMoves.add(new ChessMove(startPos, endPos2));
        }
    }
    private boolean isValidAttack(ChessPosition attackPos, ChessPosition startPos, ChessBoard board) {
        if (attackPos.getColumn() < 0 || attackPos.getColumn() >= BOARDSIZE) return false;
        if (board.getPiece(attackPos) == null) return false;
        if (board.getPiece(attackPos).getTeamColor() == board.getPiece(startPos).getTeamColor()) return false;
        return true;
    }
    private boolean isValidForwardMove(ChessPosition endPos, ChessBoard board) {
        if(endPos.getRow() >= 0 && endPos.getColumn() >= 0 && endPos.getRow() < BOARDSIZE && endPos.getColumn() < BOARDSIZE){
            if (board.getPiece(endPos) == null) return true; //This is a blank tile
            return false; //There is a piece in the way
        }
        return false; //This is outside the boundaries of the board
    }
    private void addPromotions(ArrayList<ChessMove> possibleMoves, ChessPosition startPos, ChessPosition endPos) {
        possibleMoves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.QUEEN));
        possibleMoves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.BISHOP));
        possibleMoves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.ROOK));
        possibleMoves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.KNIGHT));
    }
}


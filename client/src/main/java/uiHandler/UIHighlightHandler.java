package uiHandler;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.PrintHelper;
import java.util.ArrayList;
import java.util.Collection;

public class UIHighlightHandler extends UIHandler {
    public Collection<ChessMove> highlight(ArrayList<String> coordinate, ChessBoard board) {
        if (coordinate.size() != 1) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printHighlight();
            return null;
        }

        if (coordinate.getFirst().length() != 2) {
            System.out.println("Incorrect coordinate");
            return null;
        }

        // Convert params to ChessPosition
        //(int) a = 97, (int) 1 = 49
        int letter = (int) coordinate.getFirst().charAt(0) - 96;
        int num = (int) coordinate.getFirst().charAt(1) - 48;
        if (letter < 1 || letter > 8 || num < 1 || num > 8) {
            System.out.println("Incorrect coordinate");
            return null;
        }
        ChessPosition pos = new ChessPosition(num, letter);
        ChessPiece piece = board.getPiece(pos);
        if (piece == null) return null;
        System.out.print(piece.pieceMoves(board, pos));
        return piece.pieceMoves(board, pos);
    }
}

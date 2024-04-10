package uiHandler;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.PrintHelper;
import java.util.ArrayList;
import java.util.Collection;

public class UIHighlightHandler extends UIHandler {
    public boolean validCoordinate(String coordinate) {
        if (coordinate.length() != 2) {
            System.out.println("Incorrect coordinate");
            return false;
        }

        // Convert params to ChessPosition: (int) a = 97, (int) 1 = 49
        int letter = (int) coordinate.charAt(0) - 96;
        int num = (int) coordinate.charAt(1) - 48;
        if (letter < 1 || letter > 8 || num < 1 || num > 8) {
            System.out.println("Incorrect coordinate");
            return false;
        }
        return true;
    }

    /** Convert a letter in a Chess coordinate for use in a ChessPosition object
     * <p>
     * (int) 'a' = 97
     * */
    public int convertChar(char letter) {
        return (int) letter - 96;
    }

    /** Convert a number in a Chess coordinate for use in a ChessPosition object
     * <p>
     * (int) '1' = 49
     * */
    public int convertNum(char num) {
        return (int) num - 48;
    }
    public Collection<ChessMove> highlight(ArrayList<String> coordinate, ChessBoard board) {
        if (coordinate.size() != 1) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printHighlight();
            return null;
        }
        if (!validCoordinate(coordinate.getFirst())) return null;
        if (coordinate.size() != 1) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printHighlight();
            return null;
        }
        int letter = convertChar(coordinate.getFirst().charAt(0));
        int num = convertNum(coordinate.getFirst().charAt(1));
        ChessPosition pos = new ChessPosition(num, letter);
        ChessPiece piece = board.getPiece(pos);
        if (piece == null) return null;
        System.out.print(piece.pieceMoves(board, pos));
        return piece.pieceMoves(board, pos);
    }
}

package uiHandler;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.PrintHelper;
import java.util.ArrayList;
import java.util.Collection;
import static chess.ChessBoard.BOARDSIZE;

public class UIHighlightHandler extends UIHandler {
    public Collection<ChessMove> highlight(ArrayList<String> coordinate, ChessBoard board) {
        if (coordinate.size() != 1) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printHighlight();
            return null;
        }
        if (!validCoordinate(coordinate.getFirst())) return null;
        int letter = convertChar(coordinate.getFirst().charAt(0));
        int num = convertNum(coordinate.getFirst().charAt(1));
        ChessPosition pos = new ChessPosition(num, BOARDSIZE+1-letter);
        ChessPiece piece = board.getPiece(pos);
        if (piece == null) return null;
        System.out.print(piece.pieceMoves(board, pos));
        return piece.pieceMoves(board, pos);
    }
}
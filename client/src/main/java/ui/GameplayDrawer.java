package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import static chess.ChessBoard.BOARDSIZE;
import static ui.EscapeSequences.*;

public class GameplayDrawer {
    private static boolean blackSquare;

    public GameplayDrawer() {
        blackSquare = true;
    }

    public static void draw (ChessBoard board) {
        System.out.println(ERASE_SCREEN);
        printWhite(board);
        System.out.println();
        printBlack(board);
    }

    public static void printWhite(ChessBoard board) {
        int[] num = new int[BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) num[i] = BOARDSIZE - i;

        printAlpha(true);
        for (int i = 0; i < BOARDSIZE; i++) {
            System.out.print(num[i] + " ");
            for (int j = 0; j < BOARDSIZE; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
                System.out.print(chessPiece(piece));
            }
            System.out.print(RESET);
            System.out.println(" " + num[i]);
            blackSquare = !blackSquare;
        }
        printAlpha(true);
    }

    public static void printBlack(ChessBoard board) {
        int[] num = new int[BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) num[i] = BOARDSIZE - i;

        printAlpha(false);
        for (int i = BOARDSIZE-1; i >= 0; i--) {
            System.out.print(num[i] + " ");
            for (int j = BOARDSIZE-1; j >= 0; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
                System.out.print(chessPiece(piece));
            }
            System.out.print(RESET);
            System.out.println(" " + num[i]);
            blackSquare = !blackSquare;
        }
        printAlpha(false);
    }

    private static void printAlpha(boolean white) {
        String[] alpha = {"a", "b", "c", "d", "e", "f", "g", "h"};
        System.out.print("\u2000\u2000\u2000\u2004\u2004");
        if (white) for (String letter : alpha) System.out.print(letter + "\u2000\u2000\u2004\u2008\u2008");
        else for (int i = alpha.length; i > 0; i--) System.out.print(alpha[i-1] + "\u2000\u2000\u2004\u2008\u2008");
        System.out.println();
    }

    private static String chessPiece(ChessPiece piece) {
        System.out.print(SET_TEXT_BOLD);
        if (blackSquare) {
            blackSquare = false;
            System.out.print(SET_BG_COLOR_DARK_GREY);
        }
        else {
            blackSquare = true;
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        if (piece == null) return EMPTY;
        if (piece.getTeamColor().toString().equals("WHITE")) {
            return switch (piece.getPieceType().toString()) {
                case ("KING") -> WHITE_KING;
                case ("QUEEN") -> WHITE_QUEEN;
                case ("ROOK") -> WHITE_ROOK;
                case ("BISHOP") -> WHITE_BISHOP;
                case ("KNIGHT") -> WHITE_KNIGHT;
                default -> WHITE_PAWN;
            };
        }
        else {
            return switch (piece.getPieceType().toString()) {
                case ("KING") -> BLACK_KING;
                case ("QUEEN") -> BLACK_QUEEN;
                case ("ROOK") -> BLACK_ROOK;
                case ("BISHOP") -> BLACK_BISHOP;
                case ("KNIGHT") -> BLACK_KNIGHT;
                default -> BLACK_PAWN;
            };
        }
    }
}

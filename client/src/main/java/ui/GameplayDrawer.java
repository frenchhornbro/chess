package ui;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.Collection;
import static chess.ChessBoard.BOARDSIZE;
import static ui.EscapeSequences.*;

public class GameplayDrawer {
    private static boolean blackSquare;

    public GameplayDrawer() {
        blackSquare = true;
    }

    public static void draw (ChessBoard board, String playerColor, Collection<ChessMove> moves) {
        System.out.print(ERASE_SCREEN);
        if (playerColor == null || playerColor.equalsIgnoreCase("WHITE")) printWhite(board, moves);
        else printBlack(board, moves);
        System.out.println();
    }

    public static void printWhite(ChessBoard board, Collection<ChessMove> moves) {
        int[] num = new int[BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) num[i] = BOARDSIZE - i;

        printAlpha(false);
        for (int i = 0; i < BOARDSIZE; i++) {
            System.out.print(num[i] + "\u2003");
            for (int j = 0; j < BOARDSIZE; j++) {
                //TODO: Print the moves in one color, the current position in another, and the pieces that can be captured in a third
                ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
                System.out.print(chessPiece(piece, moves, i, j));
            }
            System.out.print(RESET);
            System.out.println(" " + num[i]);
            blackSquare = !blackSquare;
        }
        printAlpha(false);
    }

    public static void printBlack(ChessBoard board, Collection<ChessMove> moves) {
        int[] num = new int[BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) num[i] = BOARDSIZE - i;

        printAlpha(true);
        for (int i = BOARDSIZE-1; i >= 0; i--) {
            System.out.print(num[i] + " ");
            for (int j = BOARDSIZE-1; j >= 0; j--) {
                ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
                System.out.print(chessPiece(piece, moves, i, j));
            }
            System.out.print(RESET);
            System.out.println(" " + num[i]);
            blackSquare = !blackSquare;
        }
        printAlpha(true);
    }

    private static void printAlpha(boolean white) {
        String[] alpha = {"a", "b", "c", "d", "e", "f", "g", "h"};
        System.out.print(EMPTY);
        if (!white) for (String letter : alpha) System.out.print(letter + "\u2003\u2003");
        else for (int i = alpha.length; i > 0; i--) System.out.print(alpha[i-1] + "\u2003\u2003");
        System.out.println();
    }

    private static String chessPiece(ChessPiece piece, Collection<ChessMove> collMoves, int row, int col) {
        System.out.print(SET_TEXT_BOLD);
        if (blackSquare) {
            blackSquare = false;
            System.out.print(SET_BG_COLOR_DARK_GREY);
        }
        else {
            blackSquare = true;
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        if (collMoves != null) {
            if (!collMoves.isEmpty()) {
                //TODO: Consider only allowing querying moves for the person whose turn it is
                row = BOARDSIZE - 1 - row;
                ArrayList<ChessMove> moves = new ArrayList<>(collMoves);
                ChessPosition startPos = moves.getFirst().getStartPosition();
                if (startPos.getRow() == row && startPos.getColumn() == col) {
                    System.out.print(SET_BG_COLOR_BLUE);
                }
                else {
                    for (ChessMove move : moves) {
                        ChessPosition endPos = move.getEndPosition();
                        if (endPos.getRow() == row && endPos.getColumn() == col) {
                            System.out.print(SET_BG_COLOR_YELLOW);
                        }
                    }
                }
            }
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

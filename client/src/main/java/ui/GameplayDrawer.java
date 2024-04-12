package ui;

import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import static chess.ChessBoard.BOARDSIZE;
import static ui.EscapeSequences.*;

public class GameplayDrawer {
    private static boolean blackSquare;

    public GameplayDrawer() {
        blackSquare = true;
    }

    public static void draw (ChessGame game, String playerColor, Collection<ChessMove> moves, boolean resigned) {
        ChessBoard board = game.getBoard();
        ChessGame.TeamColor turn = game.getTeamTurn();

        System.out.print(ERASE_SCREEN);
        if (playerColor == null) System.out.println("\tObserving");
        else System.out.println("\tPlaying: " + playerColor.toUpperCase());
        if (playerColor == null || playerColor.equalsIgnoreCase("WHITE")) printWhite(board, moves);
        else printBlack(board, moves);

        if (game.getCheckmate() != 0) System.out.println("\tCHECKMATE");
        else if (game.isInCheck(ChessGame.TeamColor.WHITE)) System.out.println("\tCHECK: WHITE");
        else if (game.isInCheck(ChessGame.TeamColor.BLACK)) System.out.println("\tCHECK: BLACK");
        else if (game.getStalemate() != 0) System.out.println("\tSTALEMATE");
        else if (resigned) System.out.println("\tGAME OVER");
        else System.out.println("\tTurn: " + turn);
        if (moves!=null && moves.isEmpty()) System.out.println("\nNo valid moves");
        System.out.println();
    }

    public static void printWhite(ChessBoard board, Collection<ChessMove> moves) {
        int[] num = new int[BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) num[i] = BOARDSIZE - i;

        printAlpha(false);
        for (int i = BOARDSIZE-1; i >= 0; i--) {
            System.out.print(BOARDSIZE+1-num[i] + "\u2003");
            for (int j = 0; j < BOARDSIZE; j++) {
                printSquare(board, moves, i, j);
            }
            System.out.print(RESET);
            System.out.println("\u2003" + (BOARDSIZE+1-num[i]));
            blackSquare = !blackSquare;
        }
        printAlpha(false);
    }

    public static void printBlack(ChessBoard board, Collection<ChessMove> moves) {
        int[] num = new int[BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) num[i] = BOARDSIZE - i;

        printAlpha(true);
        for (int i = 0; i < BOARDSIZE; i++) {
            System.out.print(BOARDSIZE+1-num[i] + "\u2003");
            for (int j = BOARDSIZE-1; j >= 0; j--) {
                printSquare(board, moves, i, j);
            }
            System.out.print(RESET);
            System.out.println("\u2003" + (BOARDSIZE+1-num[i]));
            blackSquare = !blackSquare;
        }
        printAlpha(true);
    }

    private static void printSquare(ChessBoard board, Collection<ChessMove> moves, int i, int j) {
        ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
        System.out.print(chessPiece(piece, moves, board, i, j));
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printAlpha(boolean black) {
        String[] alpha = {"a", "b", "c", "d", "e", "f", "g", "h"};
        System.out.print(EMPTY);
        if (!black) for (String letter : alpha) System.out.print(letter + "\u2003\u2003");
        else for (int i = alpha.length; i > 0; i--) System.out.print(alpha[i-1] + "\u2003\u2003");
        System.out.println();
    }

    private static String chessPiece(ChessPiece piece, Collection<ChessMove> collMoves, ChessBoard board, int row, int col) {
        System.out.print(SET_TEXT_BOLD);
        if (blackSquare) {
            blackSquare = false;
            System.out.print("\u001b[48;5;95m");
        }
        else {
            blackSquare = true;
            System.out.print("\u001b[48;5;222m");
        }
        if (collMoves != null) {
            if (!collMoves.isEmpty()) {
                ArrayList<ChessMove> moves = new ArrayList<>(collMoves);
                ChessPosition startPos = moves.getFirst().getStartPosition();
                if (startPos.getRow() == row && startPos.getColumn() == col) {
                    System.out.print(SET_BG_COLOR_GREEN);
                }
                else {
                    for (ChessMove move : moves) {
                        ChessPosition endPos = move.getEndPosition();
                        if (endPos.getRow() == row && endPos.getColumn() == col) {
                            System.out.print(SET_BG_COLOR_YELLOW);
                            if (piece != null && board.getPiece(endPos) != null &&
                                    !piece.getTeamColor().equals(board.getPiece(startPos).getTeamColor())) {
                                System.out.print(SET_BG_COLOR_RED);
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (piece == null) return EMPTY;
        System.out.print(SET_TEXT_COLOR_MAGENTA);
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
            System.out.print(SET_TEXT_COLOR_BLUE);
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

package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    public static int BOARDSIZE = 8;
    private TeamColor teamTurn;
    private ChessBoard board;
    private boolean stalemate;
    private boolean checkmate;

    public ChessGame () {
        this.teamTurn = null;
        this.board = null;
        this.stalemate = isInStalemate(teamTurn);
        this.checkmate = isInCheckmate(teamTurn);
    }
    public ChessGame(TeamColor turn, ChessBoard newBoard) {
        this.teamTurn = turn;
        this.board = newBoard;
        this.stalemate = isInStalemate(teamTurn);
        this.checkmate = isInCheckmate(teamTurn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        return piece.pieceMoves(getBoard(),startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //Utilize validMoves here (if it's not in the collection returned, if it would leave the king in check, or if it's not that color's turn, throw InvalidMoveException)
        /*
        - Is this move in the collection validMoves?
        - Is it this team's turn?
        - Will this leave this team's king in check?
        If Yes, Yes, No, then:
        - Remove piece at endPos
        - Create piece from startPos at endPos
        - Remove piece from startPos
        Else
        - throw InvalidMoveException
         */
        Collection<ChessMove> validMoveCollection = validMoves(move.getStartPosition());
        if (validMoveCollection.contains(move)) {
            //need to check 2 other conditions
        }

        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        /*
        For each square on the chessboard:
        - If that piece is null, proceed
        - Else, if that piece's team color is not teamColor
            - Add that piece's validMoves to total collection
        - (Also maybe check to see where the teamColor king is)
        If the king's position is in the collection validMoves return true
        Else return false
         */
        ChessPosition kingPos = null;
        Collection<ChessMove> enemyMoves = new ArrayList<>();
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                ChessPosition currPos = new ChessPosition(i,j);
                if (board.getPiece(currPos) != null) {
                    if (board.getPiece(currPos).getTeamColor() != teamColor) enemyMoves.addAll(validMoves(currPos));
                    else if (board.getPiece(currPos).getPieceType().toString().equals("KING")) kingPos = new ChessPosition(i,j);
                }
            }
        }
        for (ChessMove move: enemyMoves) if (move.getEndPosition() == kingPos) return true;
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

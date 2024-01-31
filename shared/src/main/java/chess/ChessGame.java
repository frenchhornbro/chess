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
        this.stalemate = false;
        this.checkmate = false;
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
            if (board.getPiece(move.getStartPosition()) != null) {
                ChessGame.TeamColor queriedTurn = board.getPiece(move.getStartPosition()).getTeamColor();
                if (queriedTurn == teamTurn) {
                    if (!leavesKingInCheck(move)) {
                        updateTeamConditions(move);
                    }
                    else throw new InvalidMoveException("This move " + move + " leaves you in check for the board:\n" + board);
                }
                else  throw new InvalidMoveException("It is not your turn " + queriedTurn.toString() + ", it's " + teamTurn +
                        "'S turn (move = " + move + ")");
            }
            else throw new InvalidMoveException("Piece is null");
        }
        else throw new InvalidMoveException("Invalid move");
    }

    private void updateTeamConditions(ChessMove lastMove) {
        //TeamColor just finished their turn.
        //That means that promotion must be checked if the most recent move was a pawn
        //Then change whose turn it is
        //Check, Checkmate, and Stalemate must be queried for the next team

        /*if (shouldPromote(lastMove)) promote(lastMove);*/
        if (teamTurn == TeamColor.WHITE) teamTurn = TeamColor.BLACK;
        else teamTurn = TeamColor.WHITE;
        if (kingExists()) {
            if (isInCheck(teamTurn)) {
                //if (isInCheckmate(teamTurn)) checkmate = true;
            }
            else if (isInStalemate(teamTurn)) stalemate = true;
        }

    }
    /*private boolean shouldPromote(ChessMove lastMove) {
        ChessPiece pieceMoved = board.getPiece(lastMove.getEndPosition());
        if (pieceMoved == null) throw new RuntimeException("Move did not occur");
        if (!pieceMoved.toString().equals("PAWN")) return false;
        if (pieceMoved.getTeamColor() == TeamColor.WHITE) {
            return (lastMove.getEndPosition().getRow() == 7);
        }
        else return (lastMove.getEndPosition().getRow() == 0);
    }
    private void promote(ChessMove lastMove) {
        ChessPiece pawn = board.getPiece(lastMove.getEndPosition());
        if (pawn == null) throw new RuntimeException("Trying to promote a null piece");
        if (!pawn.getPieceType().toString().equals("PAWN")) throw new RuntimeException("Trying to promote a non-pawn");
        new ChessPiece(pawn.getTeamColor(), promotionType);
        deletePiece(pawn);
    }*/
    private boolean leavesKingInCheck(ChessMove move) {
        //Run changeBoard. If the king is in check, undo it and return false, otherwise return true.
        ChessPiece capturedPiece = changeBoard(move);
        if (!kingExists()) return false;
        if (isInCheck(teamTurn)) {
            undoBoard(move, capturedPiece);
            return true;
        }
        return false;
    }

    private ChessPiece changeBoard(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece capturedPiece = board.getPiece(end);
        board.addPiece(end, board.getPiece(start));
        board.addPiece(start,null);
        return capturedPiece;
    }

    private void undoBoard(ChessMove move, ChessPiece capturedPiece) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        board.addPiece(start, board.getPiece(move.getEndPosition()));
        board.addPiece(end, capturedPiece);
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
        for (int i = 1; i <= BOARDSIZE; i++) {
            for (int j = 1; j <= BOARDSIZE; j++) {
                ChessPosition currPos = new ChessPosition(i,j);
                if (board.getPiece(currPos) != null) {
                    if (board.getPiece(currPos).getTeamColor() != teamColor) enemyMoves.addAll(validMoves(currPos));
                    else if (board.getPiece(currPos).getPieceType().toString().equals("KING")) kingPos = new ChessPosition(i,j);
                }
            }
        }
        if (kingPos == null) throw new RuntimeException(teamColor + " King cannot be found on the board");
        for (ChessMove move: enemyMoves) {
            if (move.getEndPosition().toString().equals(kingPos.toString())) return true;
        }
        return false;
    }

    private boolean kingExists() {
        for (int i = 1; i <= BOARDSIZE; i++) {
            for (int j = 1; j <= BOARDSIZE; j++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(i,j));
                if (currPiece != null) {
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        if (currPiece.getTeamColor() == teamTurn) return true;
                    }
                }
            }
        }
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
        //Get the collection of available moves for every piece for teamColor. If collection.size() == 0, return true
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int i = 1; i <= BOARDSIZE; i++) {
            for (int j = 1; j <= BOARDSIZE; j++) {
                ChessPosition currPos = new ChessPosition(i,j);
                if (board.getPiece(currPos) != null) {
                    if (board.getPiece(currPos).getTeamColor() == teamColor) {
                        possibleMoves.addAll(validMoves(currPos));
                    }
                }
            }
        }
        return (possibleMoves.isEmpty());
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

    private void deletePiece(ChessPiece piece) {
        piece = null;
    }
}

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
    public final static int BOARDSIZE = 8;
    private TeamColor teamTurn;
    private ChessBoard board;
    private boolean stalemate;
    private boolean checkmate;
    private boolean gameOver;

    public ChessGame () {
        setBoard(null);
    }
    public ChessGame(TeamColor turn, ChessBoard newBoard) {
        this.teamTurn = (turn == null) ? TeamColor.WHITE : turn;
        this.board = newBoard;
        this.stalemate = isInStalemate(teamTurn);
        this.checkmate = isInCheckmate(teamTurn);
        this.gameOver = stalemate || checkmate;
    }

    public ChessGame(TeamColor turn, ChessBoard newBoard, boolean stalemate, boolean checkmate, boolean gameOver) {
        this.teamTurn = turn;
        this.board = newBoard;
        this.stalemate = stalemate;
        this.checkmate = checkmate;
        this.gameOver = gameOver;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public int getStalemate() {
        return (stalemate) ? 1 : 0;
    }

    public int getCheckmate() {
        return (checkmate) ? 1 : 0;
    }

    public int getGameOver() {
        return (gameOver) ? 1 : 0;
    }

    public void setGameOver(boolean resign) {
        this.gameOver = resign;
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
        //If the board tries to make a move but teamTurn is not specified, teamTurn will be set as whoever first attempts to make a move
        ChessPiece piece = board.getPiece(startPosition);
        if (teamTurn == null) setTeamTurn(board.getPiece(startPosition).getTeamColor());

        //A move is only possible if it is in the piece's pieceMoves list, and it doesn't leave the king in check
        Collection<ChessMove> potentialMoves = piece.pieceMoves(getBoard(),startPosition);
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (ChessMove move : potentialMoves) {
            if (!leavesKingInCheck(move)) {
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
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
        if (board.getPiece(move.getStartPosition()) != null) {
            ChessGame.TeamColor queriedTurn = board.getPiece(move.getStartPosition()).getTeamColor();
            if (teamTurn == null) setTeamTurn(queriedTurn);
            if (!gameOver) {
                if (queriedTurn == teamTurn) {
                    Collection<ChessMove> validMoveCollection = validMoves(move.getStartPosition());
                    if (validMoveCollection.contains(move)) {
                        if (!leavesKingInCheck(move)) {
                            applyMove(move);        //This does nothing with the returned captured ChessPiece
                            moveCastlingRook(move);
                            updateTeamConditions(move);
                        }
                        else throw new InvalidMoveException(move + " leaves you in check");
                    }
                    else throw new InvalidMoveException("invalid move");
                }
                else throw new InvalidMoveException("moving out of turn");
            }
            else throw new InvalidMoveException("game is over");
        }
        else throw new InvalidMoveException("can't move from a blank square");
    }

    private void updateTeamConditions(ChessMove lastMove) {
        ChessPiece pieceMoved = board.getPiece(lastMove.getEndPosition());
        if (pieceMoved == null) return;
        /*
        TeamColor just finished their turn.
        That means that promotion must be checked if the most recent move was a pawn
        Then change whose turn it is
        Check, Checkmate, and Stalemate must be queried for the next team
        */
        if (shouldPromote(lastMove)) promote(lastMove);
        if (teamTurn == TeamColor.WHITE) teamTurn = TeamColor.BLACK;
        else teamTurn = TeamColor.WHITE;
        if (kingExists() && isInCheck(teamTurn)) {
            if (isInCheckmate(teamTurn)) {
                checkmate = true;
                gameOver = true;
            }
        }
        else if (isInStalemate(teamTurn)) {
            stalemate = true;
            gameOver = true;
        }
    }
    private boolean shouldPromote(ChessMove lastMove) {
        ChessPiece pieceMoved = board.getPiece(lastMove.getEndPosition());
        if (pieceMoved == null) return false;
        if (!pieceMoved.getPieceType().toString().equals("PAWN")) return false;
        if (pieceMoved.getTeamColor() == TeamColor.WHITE) {
            return (lastMove.getEndPosition().getRow() == 7);
        }
        else {
            return (lastMove.getEndPosition().getRow() == 0);
        }
    }
    private void promote(ChessMove lastMove) {
        ChessPiece pawn = board.getPiece(lastMove.getEndPosition());
        if (pawn == null) return;
        if (!pawn.getPieceType().toString().equals("PAWN")) return;
        if (lastMove.getPromotionPiece() == null) return;
        board.addPiece(lastMove.getEndPosition(), new ChessPiece(pawn.getTeamColor(), lastMove.getPromotionPiece()));
    }
    private boolean leavesKingInCheck(ChessMove move) {
        //Run applyMove. If the king is in check, undo it and return false, otherwise undo it and return true.
        if (!kingExists()) return false;
        ChessPiece capturedPiece = applyMove(move);
        if (isInCheck(teamTurn)) {
            undoMove(move, capturedPiece);
            return true;
        }
        undoMove(move, capturedPiece);
        return false;
    }

    private ChessPiece applyMove(ChessMove move) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(start);
        if (movingPiece == null) return null;
        ChessPiece capturedPiece = board.getPiece(end);
        if (capturedPiece != null && capturedPiece.getTeamColor() == teamTurn) return null;
        board.addPiece(end, movingPiece);
        board.addPiece(start,null);
        return capturedPiece;
    }

    private void moveCastlingRook(ChessMove castle) {
        //If the piece that has just moved is a king, and said king has moved 2 squares, it has castled
        ChessPosition kingPos = castle.getEndPosition();
        ChessPiece king = board.getPiece(kingPos);
        if (king.getPieceType() != ChessPiece.PieceType.KING) return;
        int numSquaresMoved = kingPos.getColumn() - castle.getStartPosition().getColumn();
        if (numSquaresMoved == 1 || numSquaresMoved == -1) return;
        if (numSquaresMoved == 2) {
            board.addPiece(new ChessPosition(kingPos.getRow()+1,kingPos.getColumn()),
                           new ChessPiece(king.getTeamColor(), ChessPiece.PieceType.ROOK));
            board.addPiece(new ChessPosition(kingPos.getRow()+1, 8), null);
        }
        else if (numSquaresMoved == -2) {
            board.addPiece(new ChessPosition(kingPos.getRow()+1,kingPos.getColumn()+2),
                    new ChessPiece(king.getTeamColor(), ChessPiece.PieceType.ROOK));
            board.addPiece(new ChessPosition(kingPos.getRow()+1, 1), null);
        }
    }
    private void undoMove(ChessMove move, ChessPiece capturedPiece) {
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
                ChessPiece currPiece = board.getPiece(currPos);
                if (currPiece != null) {
                    if (currPiece.getTeamColor() != teamColor) {
                        enemyMoves.addAll(currPiece.pieceMoves(board, currPos));
                    }
                    else if (currPiece.getPieceType().toString().equals("KING") && currPiece.getTeamColor() == teamColor) {
                        kingPos = new ChessPosition(i,j);
                    }
                }
            }
        }
        if (kingPos == null) return false;
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
        /*If the teamColor king is in check
        Then check if any valid teamColor moves will stop the king from being in check
        If not, the king is in checkmate*/
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (!isInCheck(teamColor)) return false;
        for (int i = 1; i <= BOARDSIZE; i++) {
            for (int j = 1; j <= BOARDSIZE; j++) {
                ChessPosition currPos = new ChessPosition(i,j);
                ChessPiece currPiece = board.getPiece(currPos);
                if (currPiece != null) {
                    if (currPiece.getTeamColor() == teamColor) {
                        possibleMoves.addAll(currPiece.pieceMoves(board, currPos));
                    }
                }
            }
        }
        for (ChessMove move: possibleMoves) {
            if (!leavesKingInCheck(move)) return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        /*Get the collection of available moves for every piece for teamColor.
        For each possible move, check if that move would put the king in check.
        If a move can be made without putting the king in check, return false;
        Else, return true;*/
        if (teamColor == null) return false;
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
        if (possibleMoves.isEmpty()) return true;
        for (ChessMove move : possibleMoves) {
            if (!leavesKingInCheck(move)) return false;
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.teamTurn = null;
        this.board = board;
        this.stalemate = false;
        this.checkmate = false;
        this.gameOver = false;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "Team turn: " + this.teamTurn + ", Stalemate: " + this.stalemate + ", Checkmate: " + this.checkmate
                + ", GameOver: " + this.gameOver + ", ChessBoard: " + ((this.board == null) ? "null" : this.board.toString());
    }
}

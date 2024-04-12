package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends MovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        rookMoves(possibleMoves,startPosition, board,true,true);
        rookMoves(possibleMoves,startPosition, board,true,false);
        rookMoves(possibleMoves,startPosition, board,false,false);
        rookMoves(possibleMoves,startPosition, board,false,true);
        return possibleMoves;
    }
}


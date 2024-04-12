package chess.piecemovescalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends MovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition startPos) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        bishopMoves(possibleMoves,startPos, board,true,true);
        bishopMoves(possibleMoves,startPos, board,true,false);
        bishopMoves(possibleMoves,startPos, board,false,false);
        bishopMoves(possibleMoves,startPos, board,false,true);
        return possibleMoves;
    }
}

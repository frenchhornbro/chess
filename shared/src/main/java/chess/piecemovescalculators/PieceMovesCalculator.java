package chess.piecemovescalculators;

import chess.ChessMove;
import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves (chess.ChessBoard board, chess.ChessPosition startPosition);
}

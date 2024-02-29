package chess.piecemovescalculators;

import chess.ChessMove;
import java.util.Collection;

public interface PieceMovesCalculator {
    public final static int BOARDSIZE = 8;
    public Collection<ChessMove> pieceMoves (chess.ChessBoard board, chess.ChessPosition startPosition);
}

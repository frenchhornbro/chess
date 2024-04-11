package uiHandler;

import chess.ChessMove;
import chess.ChessPosition;
import ui.Client;
import java.util.ArrayList;
import static ui.PrintHelper.printMove;

public class UIMoveHandler extends UIHandler {
    public void move(ArrayList<String> params, Client client, String storageGameID) {

        if (params.size() != 2 && params.size() != 3) {
            System.out.println("Incorrect number of parameters");
            printMove();
            return;
        }
        String strStartPos = params.getFirst();
        String strEndPos = params.get(1);
        String pieceType = (params.size() == 3) ? params.get(2) : null;
        if (!validCoordinate(strStartPos) || !validCoordinate(strEndPos) || (params.size() == 3 && !validPromotionType(pieceType))) {
            printMove();
            return;
        }
        ChessPosition startPos = new ChessPosition(convertNum(strStartPos.charAt(1)), convertChar(strStartPos.charAt(0)));
        ChessPosition endPos = new ChessPosition(convertNum(strEndPos.charAt(1)), convertChar(strEndPos.charAt(0)));
        ChessMove move = (params.size() == 3) ? new ChessMove(startPos, endPos, toPieceType(pieceType)) : new ChessMove(startPos, endPos);
        client.webSocketClient.move(client.getAuthToken(), storageGameID, move);
    }
}

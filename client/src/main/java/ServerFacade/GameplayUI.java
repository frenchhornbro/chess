package ServerFacade;

import chess.ChessBoard;
import dataAccess.SQLGameDAO;
import handler.UIJoinHandler;
import ui.GameplayDrawer;
import java.util.ArrayList;

public class GameplayUI {
    public void goToGameplayUI(String gameID, ArrayList<Integer> storageIDs) {
        gameID = UIJoinHandler.convertID(gameID, storageIDs);
        try {
            SQLGameDAO sqlGameDAO = new SQLGameDAO();
            ChessBoard board = sqlGameDAO.getBoard(Integer.parseInt(gameID));
            GameplayDrawer.draw(board);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

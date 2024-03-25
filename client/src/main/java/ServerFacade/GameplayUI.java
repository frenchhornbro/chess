package ServerFacade;

import ui.Client;
import uiHandler.UIDrawBoardHandler;
import uiHandler.UIJoinHandler;
import java.util.ArrayList;

public class GameplayUI {
    public void goToGameplayUI(int port, Client client, String displayGameID, ArrayList<Integer> storageIDs) {
        String storageGameID = String.valueOf(UIJoinHandler.convertID(displayGameID, storageIDs));
        try {
            UIDrawBoardHandler drawBoardHandler = new UIDrawBoardHandler();
            drawBoardHandler.drawBoard(port, client.getAuthToken(), storageGameID);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

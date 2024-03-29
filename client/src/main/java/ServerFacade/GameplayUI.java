package ServerFacade;

import ui.Client;
import ui.PrintHelper;
import uiHandler.UIDrawBoardHandler;
import uiHandler.UIJoinHandler;
import java.util.ArrayList;

public class GameplayUI {
    public void goToGameplayUI(int port, Client client, String displayGameID, ArrayList<Integer> storageIDs, String playerColor) {
        String storageGameID = String.valueOf(UIJoinHandler.convertID(displayGameID, storageIDs));
        UIDrawBoardHandler drawBoardHandler = new UIDrawBoardHandler();
        drawBoardHandler.drawBoard(port, client.getAuthToken(), storageGameID, playerColor);
        /*
            Commands:
            Help
            Redraw Chess Board
            Leave
            Make Move
            Resign
            Highlight Legal Moves
         */
        String input = "";
        while (!input.equalsIgnoreCase("leave")) {
            System.out.print("> ");
            ArrayList<String> commands = PreLoginUI.getCommands();
            input = commands.getFirst();
            ArrayList<String> params = new ArrayList<>(commands);
            switch(input.toLowerCase()) {
                //TODO: I doubt any of these commands should be available to just observers except help and leave
                case ("help"):
                    PrintHelper.printDraw();
                    PrintHelper.printHighlight();
                    PrintHelper.printMove();
                    PrintHelper.printResign();
                    PrintHelper.printLeave();
                    PrintHelper.printHelp();
                    break;
                case ("leave"):
                    System.out.println("Leaving...");
                    return;
                case ("redraw chess board"):
                    break;
                case ("make move"):
                    break;
                case ("resign"):
                    break;
                case ("highlight legal moves"):
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
    }
}

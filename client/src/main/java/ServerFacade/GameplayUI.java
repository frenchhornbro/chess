package ServerFacade;

import chess.ChessMove;
import ui.Client;
import ui.GameplayDrawer;
import uiHandler.UIHighlightHandler;
import uiHandler.UIJoinHandler;
import java.util.ArrayList;
import java.util.Collection;
import static ui.PrintHelper.*;

public class GameplayUI {
    private final UIHighlightHandler highlightHandler;
    public GameplayUI() {
        this.highlightHandler = new UIHighlightHandler();
    }
    public boolean goToGameplayUI(int port, Client client, String displayGameID, ArrayList<Integer> storageIDs, String playerColor) {
        String storageGameID = String.valueOf(UIJoinHandler.convertID(displayGameID, storageIDs));
        if (playerColor == null) client.webSocketClient.observe(client.getAuthToken(), storageGameID);
        else client.webSocketClient.join(client.getAuthToken(), storageGameID, playerColor);
        String input = "";
        while (!input.equalsIgnoreCase("leave")) {
            ArrayList<String> commands = PreLoginUI.getCommands();
            input = commands.getFirst();
            ArrayList<String> params = new ArrayList<>(commands);
            params.removeFirst();
            switch(input.toLowerCase()) {
                //TODO: I doubt any of these commands should be available to just observers except help, draw, and leave
                case ("help"):
                    printDraw();
                    printHighlight();
                    printMove();
                    printResign();
                    printLeave();
                    printHelp();
                    break;
                case ("leave"):
                    client.webSocketClient.leave(client.getAuthToken(), storageGameID);
                    System.out.println("Leaving...");
                    return false;
                case ("exit"):
                    System.out.print("Exiting...");
                    return true;
                case ("draw"):
                case ("redraw"):
                    highlightHandler.clearScreen();
                    GameplayDrawer.draw(client.webSocketClient.board, playerColor, null);
                    break;
                case ("move"):
                    //TODO: Figure out how to get the move - maybe highlightHandler has some ideas?
                    ChessMove move = null; //FIXME
                    client.webSocketClient.move(client.getAuthToken(), storageGameID, move);
                    break;
                case ("resign"):
                    //TODO: Send a notification of player resigning
                    break;
                case ("highlight"):
                    Collection<ChessMove> moves = highlightHandler.highlight(params, client.webSocketClient.board);
                    if (moves != null) {
                        highlightHandler.clearScreen();
                        GameplayDrawer.draw(client.webSocketClient.board, playerColor, moves);
                    }
                    break;
                case ("clear"):
                    highlightHandler.clearScreen();
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
        return false;
    }
}

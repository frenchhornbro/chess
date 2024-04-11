package ServerFacade;

import chess.ChessMove;
import ui.Client;
import ui.GameplayDrawer;
import uiHandler.UIHighlightHandler;
import uiHandler.UIJoinHandler;
import uiHandler.UIMoveHandler;
import java.util.ArrayList;
import java.util.Collection;
import static ui.PrintHelper.*;

public class GameplayUI {
    private final UIHighlightHandler highlightHandler;
    private final UIMoveHandler moveHandler;

    public GameplayUI() {
        this.highlightHandler = new UIHighlightHandler();
        this.moveHandler = new UIMoveHandler();
    }
    public boolean goToGameplayUI(Client client, String displayGameID, ArrayList<Integer> storageIDs, String playerColor) {
        String storageGameID = String.valueOf(UIJoinHandler.convertID(displayGameID, storageIDs));
        if (playerColor == null) client.webSocketClient.observe(client.getAuthToken(), storageGameID);
        else client.webSocketClient.join(client.getAuthToken(), storageGameID, playerColor);
        String input = "";
        while (!input.equalsIgnoreCase("leave")) {
            System.out.print("> ");
            ArrayList<String> commands = PreLoginUI.getCommands();
            input = commands.getFirst();
            ArrayList<String> params = new ArrayList<>(commands);
            params.removeFirst();
            switch(input.toLowerCase()) {
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
                    GameplayDrawer.draw(client.webSocketClient.game, playerColor, null, client.webSocketClient.resigned);
                    break;
                case ("move"):
                    moveHandler.move(params, client, storageGameID);
                    break;
                case ("resign"):
                    client.webSocketClient.resign(client.getAuthToken(), storageGameID);
                    GameplayDrawer.draw(client.webSocketClient.game, playerColor, null, client.webSocketClient.resigned);
                    break;
                case ("highlight"):
                    Collection<ChessMove> moves = highlightHandler.highlight(params, client.webSocketClient.board);
                    if (moves != null) {
                        highlightHandler.clearScreen();
                        GameplayDrawer.draw(client.webSocketClient.game, playerColor, moves, client.webSocketClient.resigned);
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

package ServerFacade;

import chess.ChessBoard;
import chess.ChessMove;
import ui.Client;
import uiHandler.UIDrawBoardHandler;
import uiHandler.UIHighlightHandler;
import uiHandler.UIJoinHandler;
import java.util.ArrayList;
import java.util.Collection;

import static ui.PrintHelper.*;

public class GameplayUI {
    private final UIDrawBoardHandler drawBoardHandler;
    private final UIHighlightHandler highlightHandler;
    private ChessBoard board;
    public GameplayUI() {
        this.drawBoardHandler = new UIDrawBoardHandler();
        this.highlightHandler = new UIHighlightHandler();
        this.board = null;
    }
    public boolean goToGameplayUI(int port, Client client, String displayGameID, ArrayList<Integer> storageIDs, String playerColor) {
        //TODO: Send a notification of observers joining, and of players joining as a specified color
        // Do this with the /connect endpoint
        String storageGameID = String.valueOf(UIJoinHandler.convertID(displayGameID, storageIDs));
        board = drawBoardHandler.drawBoard(port, client.getAuthToken(), storageGameID, playerColor, null);
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
                    System.out.println("Leaving...");
                    //TODO: Send a notification of players and observers leaving
                    return false;
                case ("exit"):
                    System.out.print("Exiting...");
                    return true;
                case ("draw"):
                case ("redraw"):
                    drawBoardHandler.clearScreen();
                    drawBoardHandler.drawBoard(port, client.getAuthToken(), storageGameID, playerColor, null);
                    break;
                case ("move"):
                    //TODO: If checkmate or stalemate are true, prevent further moves from being made
                    //TODO: Send a notification of move being made
                    break;
                case ("resign"):
                    //TODO: Prevent further moves from being made after a player resigns
                    //TODO: Send a notification of player resigning
                    break;
                case ("highlight"):
                    Collection<ChessMove> moves = highlightHandler.highlight(params, board);
                    if (moves != null) {
                        drawBoardHandler.clearScreen();
                        drawBoardHandler.drawBoard(port, client.getAuthToken(), storageGameID, playerColor, moves);
                    }
                    break;
                case ("clear"):
                    drawBoardHandler.clearScreen();
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
        return false;
    }
}

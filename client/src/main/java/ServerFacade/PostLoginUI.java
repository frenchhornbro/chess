package ServerFacade;

import dataStorage.GameStorage;
import ui.Client;
import ui.EscapeSequences;
import uiHandler.UICreateHandler;
import uiHandler.UIJoinHandler;
import uiHandler.UIListHandler;
import uiHandler.UILogoutHandler;
import java.util.ArrayList;
import static ui.PrintHelper.*;

public class PostLoginUI {
    private final UILogoutHandler logoutHandler;
    private final UICreateHandler createHandler;
    private final UIListHandler listHandler;
    private final UIJoinHandler joinHandler;
    private final GameplayUI gameplayUI;

    public PostLoginUI() {
        this.logoutHandler = new UILogoutHandler();
        this.createHandler = new UICreateHandler();
        this.listHandler = new UIListHandler();
        this.joinHandler = new UIJoinHandler();
        this.gameplayUI = new GameplayUI();
    }

    /** Runs the Logged In UI. Returns true if it wants to fully quit and false otherwise.*/
    public boolean goToPostLogin(int port, Client client, String username) {
        System.out.print(EscapeSequences.ERASE_SCREEN);
        printPostLoginUI();
        String input = "";
        while (!input.equalsIgnoreCase("logout")) {
            System.out.print("> ");
            ArrayList<String> commands = PreLoginUI.getCommands();
            input = commands.getFirst();
            ArrayList<String> params = new ArrayList<>(commands);
            params.removeFirst();
            switch(input.toLowerCase()) {
                case ("help"):
                    if (commands.size() > 1) {
                        System.out.println("\033[32mhelp\033[39m cannot receive parameters");
                        break;
                    }
                    printCreate();
                    printList();
                    printJoin();
                    printObserve();
                    printLogout();
                    printClear();
                    printQuit();
                    printHelp();
                    break;
                case ("exit"):
                case ("quit"):
                    System.out.println("Exiting...");
                    client.setAuthToken(null);
                    return true;
                case ("logout"):
                    if (logoutHandler.logout(port, params, client.getAuthToken())) {
                        client.setAuthToken(null);
                        System.out.println("Logging out...");
                        printPreLoginUI();
                        return false;
                    }
                    else input = "";
                    break;
                case ("create"):
                    if (createHandler.create(port, params, client.getAuthToken())) {
                        StringBuilder gameName = new StringBuilder();
                        for (int i = 0; i < params.size(); i++) {
                            gameName.append(params.get(i));
                            if (i != params.size()-1) gameName.append(" ");
                        }
                        System.out.println("New game created: " + gameName);
                    }
                    break;
                case ("list"):
                    ArrayList<GameStorage> games = listHandler.list(port, params, client.getAuthToken());
                    if (games != null) {
                        ArrayList<Integer> gameIDs = new ArrayList<>();
                        client.setGameIDs(gameIDs);
                        for (int i = 0; i < games.size(); i++) {
                            GameStorage game = games.get(i);
                            System.out.print("Game: " + i + "\t");
                            System.out.print("Name: " + game.getGameName() + "\t");
                            System.out.print("White: " + game.getWhiteUsername() + "\t");
                            System.out.println("Black: " + game.getBlackUsername());
                            gameIDs.add(game.getGameID());
                        }
                        client.setGameIDs(gameIDs);
                    }
                    break;
                case ("join"):
                    String displayJoinGameID = joinHandler.join(port, params, client.getAuthToken(), client.getGameIDs(),
                            false, username);
                    if (!displayJoinGameID.equals("false")) {
                        if (this.gameplayUI.goToGameplayUI(client, displayJoinGameID, client.getGameIDs(), params.get(1))) return true;
                    }
                    break;
                case ("observe"):
                    String displayObserveGameID = joinHandler.join(port, params, client.getAuthToken(), client.getGameIDs(),
                            true, username);
                    if (!displayObserveGameID.equals("false")) {
                        if (this.gameplayUI.goToGameplayUI(client, displayObserveGameID, client.getGameIDs(), null)) return true;
                    }
                    break;
                case ("clear"):
                    if (commands.size() > 1) {
                        System.out.println("\033[32mclear\033[39m cannot receive parameters");
                        break;
                    }
                    logoutHandler.clearScreen();
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
        return false;
    }
}
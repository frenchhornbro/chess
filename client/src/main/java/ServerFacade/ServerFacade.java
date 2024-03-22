package ServerFacade;

import ui.Client;
import ui.EscapeSequences;

public class ServerFacade {
    private final PreLoginUI preLoginUI;

    public ServerFacade() {
        preLoginUI = new PreLoginUI();
    }

    /** Starts the game. For use by a Client class. */
    public void start(Client client) {
        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");
        preLoginUI.goToPreLogin(client);
    }
}
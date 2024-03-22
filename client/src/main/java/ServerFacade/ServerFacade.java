package ServerFacade;

import handler.*;
import ui.Client;
import ui.EscapeSequences;

public class ServerFacade {
    private final PreLoginUI preLoginUI;

    public ServerFacade() {
        UIRegisterHandler registerHandler = new UIRegisterHandler();
        UILoginHandler loginHandler = new UILoginHandler();
        UILogoutHandler logoutHandler = new UILogoutHandler();
        UICreateHandler createHandler = new UICreateHandler();
        UIListHandler listHandler = new UIListHandler();
        preLoginUI = new PreLoginUI(registerHandler, loginHandler, logoutHandler, createHandler, listHandler);
    }

    /** Starts the game. For use by a Client class. */
    public void start(Client client) {
        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");
        preLoginUI.goToPreLogin(client);
    }
}
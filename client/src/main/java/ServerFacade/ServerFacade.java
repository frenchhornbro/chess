package ServerFacade;

import handler.*;
import ui.Client;
import ui.EscapeSequences;

public class ServerFacade {
    public final UIRegisterHandler registerHandler;
    public final UILoginHandler loginHandler;
    public final UILogoutHandler logoutHandler;
    public final UICreateHandler createHandler;
    private final PreLoginUI preLoginUI;

    public ServerFacade() {
        registerHandler = new UIRegisterHandler();
        loginHandler = new UILoginHandler();
        logoutHandler = new UILogoutHandler();
        createHandler = new UICreateHandler();
        preLoginUI = new PreLoginUI(registerHandler, loginHandler, logoutHandler, createHandler);
    }

    /** Starts the game. For use by a Client class. */
    public void start(Client client) {
        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");
        preLoginUI.goToPreLogin(client);
    }
}
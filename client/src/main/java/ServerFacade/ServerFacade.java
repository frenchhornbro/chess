package ServerFacade;

import handler.UILoginHandler;
import handler.UILogoutHandler;
import handler.UIRegisterHandler;
import ui.Client;
import ui.EscapeSequences;
import java.util.Scanner;

public class ServerFacade {
    protected final UIRegisterHandler registerHandler;
    protected final UILoginHandler loginHandler;
    protected final UILogoutHandler logoutHandler;
    private final PreLoginUI preLoginUI;

    public ServerFacade() {
        registerHandler = new UIRegisterHandler();
        loginHandler = new UILoginHandler();
        logoutHandler = new UILogoutHandler();
        preLoginUI = new PreLoginUI();
    }

    /** Starts the game. For use by a Client class. */
    public void start(Client client) {
        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");
        preLoginUI.goToPreLogin(client);
    }

    protected String[] getCommands() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return line.split(" ");
    }
}
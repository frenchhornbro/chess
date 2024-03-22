package ServerFacade;

import handler.*;
import ui.Client;
import java.util.Scanner;

import static ui.PrintHelper.*;

public class PreLoginUI {
    private final PostLoginUI postLoginUI;
    private final UIRegisterHandler registerHandler;
    private final UILoginHandler loginHandler;

    public PreLoginUI(UIRegisterHandler registerHandler, UILoginHandler loginHandler, UILogoutHandler logoutHandler,
                      UICreateHandler createHandler, UIListHandler listHandler) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        postLoginUI = new PostLoginUI(logoutHandler, createHandler, listHandler);
    }

    /** Runs the Logged Out UI. */
    protected void goToPreLogin(Client client) {
        printPreLoginUI();

        String input = "";
        while (!input.equalsIgnoreCase("quit")) {
            System.out.print("> ");
            String[] commands = getCommands();
            input = commands[0];
            String[] params = new String[commands.length - 1];
            System.arraycopy(commands, 1, params, 0, commands.length - 1);
            switch (input.toLowerCase()) {
                case ("help"):
                    if (commands.length > 1) {
                        System.out.println("\033[32mhelp\033[39m cannot receive parameters");
                        break;
                    }
                    printRegister();
                    printLogin();
                    printQuit();
                    printHelp();
                    break;
                case ("exit"):
                case ("quit"):
                    System.out.println("Exiting...");
                    return;
                case ("login"): {
                    String authToken = loginHandler.login(params);
                    if (authToken != null) {
                        client.setAuthToken(authToken);
                        if (postLoginUI.goToPostLogin(client)) return;
                    }
                }
                break;
                case ("register"): {
                    String authToken = registerHandler.register(params);
                    if (authToken != null) {
                        client.setAuthToken(authToken);
                        if (postLoginUI.goToPostLogin(client)) return;
                    }
                    break;
                }
                case ("clear"):
                    registerHandler.clearScreen();
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
    }

    public static String[] getCommands() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return line.split(" ");
    }
}

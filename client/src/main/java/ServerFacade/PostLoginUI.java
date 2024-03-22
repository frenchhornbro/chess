package ServerFacade;

import handler.UICreateHandler;
import handler.UILogoutHandler;
import ui.Client;
import java.util.Scanner;
import static ui.PrintHelper.*;
import static ui.PrintHelper.printPreLoginUI;

public class PostLoginUI {
    private final UILogoutHandler logoutHandler;
    private final UICreateHandler createHandler;

    public PostLoginUI(UILogoutHandler logoutHandler, UICreateHandler createHandler) {
        this.logoutHandler = logoutHandler;
        this.createHandler = createHandler;
    }

    /** Runs the Logged In UI. Returns true if it wants to fully quit and false otherwise.*/
    public boolean goToPostLogin(Client client) {
        System.out.println("\t\033[36;107;1m[Logged In UI]\033[39;49;0m");
        String input = "";
        while (!input.equalsIgnoreCase("logout")) {
            System.out.print("> ");
            String[] commands = getCommands();
            input = commands[0];
            String[] params = new String[commands.length - 1];
            System.arraycopy(commands, 1, params, 0, commands.length - 1);
            //TODO: Figure out what do to if the client leaves here? Do we close their session in this phase? Maybe just call quit.
            switch(input.toLowerCase()) {
                case ("help"):
                    if (commands.length > 1) {
                        System.out.println("\033[32mhelp\033[39m cannot receive parameters");
                        break;
                    }
                    printCreate();
                    printList();
                    printJoin();
                    printObserve();
                    printLogout();
                    printQuit();
                    printHelp();
                    break;
                case ("exit"):
                case ("quit"):
                    System.out.println("Exiting...");
                    client.setAuthToken(null); //TODO: Should this be here?
                    return true;
                case ("logout"):
                    if (logoutHandler.logout(params, client.getAuthToken())) {
                        client.setAuthToken(null);
                        System.out.println("Logging out...");
                        printPreLoginUI();
                        return false;
                    }
                    else input = "";
                    break;
                case ("create"):
                    if (createHandler.create(params, client.getAuthToken())) {
                        System.out.println("New game created: " + params[0]);
                    }
                    break;
                case ("list"):
                    System.out.println("Need to do");
                    break;
                case ("join"):
                    System.out.println("Need to do");
                    break;
                case ("observe"):
                    System.out.println("Need to do");
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
        return false;
    }

    public static String[] getCommands() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return line.split(" ");
    }
}

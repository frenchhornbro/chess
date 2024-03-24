package ServerFacade;

import ui.Client;
import uiHandler.UILoginHandler;
import uiHandler.UIRegisterHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static ui.PrintHelper.*;

public class PreLoginUI {
    private final PostLoginUI postLoginUI;
    private final UIRegisterHandler registerHandler;
    private final UILoginHandler loginHandler;
    private final boolean testEnv;
    private final ArrayList<ArrayList<String>> testCommands;

    public PreLoginUI(boolean testEnv, ArrayList<ArrayList<String>> testCommands) {
        this.registerHandler = new UIRegisterHandler();
        this.loginHandler = new UILoginHandler();
        this.testEnv = testEnv;
        this.testCommands = testCommands;
        postLoginUI = new PostLoginUI();
    }

    /** Runs the Logged Out UI. */
    public void goToPreLogin(int port, Client client) {
        printPreLoginUI();

        String input = "";
        while (!input.equalsIgnoreCase("quit")) {
            System.out.print("> ");
            ArrayList<String> commands = (testEnv) ? testCommands.getFirst() : getCommands();
            input = commands.getFirst();
            ArrayList<String> params = new ArrayList<>(commands);
            params.removeFirst();
            switch (input.toLowerCase()) {
                case ("help"):
                    if (commands.size() > 1) {
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
                    String authToken = loginHandler.login(port, params);
                    if (authToken != null) {
                        client.setAuthToken(authToken);
                        if (postLoginUI.goToPostLogin(port, client)) return;
                    }
                }
                break;
                case ("register"): {
                    String authToken = registerHandler.register(port, params);
                    if (authToken != null) {
                        client.setAuthToken(authToken);
                        if (postLoginUI.goToPostLogin(port, client)) return;
                    }
                    break;
                }
                case ("clear"):
                    registerHandler.clearScreen();
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
            if (testEnv){
                if (!testCommands.isEmpty()) testCommands.removeFirst();
                else input = "quit";
            }
        }
    }

    public static ArrayList<String> getCommands() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] strArr = line.split(" ");
        return new ArrayList<>(Arrays.asList(strArr));
    }
}

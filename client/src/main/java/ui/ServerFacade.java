package ui;

import java.util.Scanner;

public class ServerFacade {
    private final ServerFacadeHandlers handlers;

    public ServerFacade(int port) {
        handlers = new ServerFacadeHandlers();
    }

    public void start(Client client) {
        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");
        goToPreLogin(client);
    }

    private void goToPreLogin(Client client) {
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
                    printExit();
                    printHelp();
                    break;
                case ("exit"):
                    input = "quit";
                case ("quit"):
                    System.out.println("Exiting...");
                    break;
                case ("login"): {
                    String authToken = handlers.login(params);
                    if (authToken != null) {
                        client.setAuthToken(authToken);
                        goToPostLogin(client);
                    }
                }
                break;
                case ("register"): {
                    String authToken = handlers.register(params);
                    if (authToken != null) {
                        client.setAuthToken(authToken);
                        goToPostLogin(client);
                    }
                    break;
                }
                case ("clear"):
                    handlers.clearScreen();
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
    }

    private void goToPostLogin(Client client) {
        System.out.println("\t\033[36;107;1m[Logged In UI]\033[39;49;0m");
        String input = "";
        while (!input.equalsIgnoreCase("logout")) {
            System.out.print("> ");
            String[] commands = getCommands();
            input = commands[0]; //TODO: There are multiple words here... so input needs to be an array
            String[] params = new String[commands.length - 1];
            System.arraycopy(commands, 1, params, 0, commands.length - 1);
            //TODO: Figure out what do to if the client leaves here? Do we close their session in this phase?
            switch(input.toLowerCase()) {
                case ("help"):
                    if (commands.length > 1) {
                        System.out.println("\033[32mhelp\033[39m cannot receive parameters");
                        break;
                    }
                    printCreateGame();
                    printListGames();
                    printJoinGame();
                    printJoinObserver();
                    printLogout();
                    printHelp();
                    break;
                case ("logout"):
                    if (handlers.logout(params, client.getAuthToken())) {
                        client.setAuthToken(null);
                        System.out.println("Logging out...");
                        printPreLoginUI();
                        return;
                    }
                    else input = "";
                    break;
                case ("create game"):
                    break;
                case ("list games"):
                    break;
                case ("join game"):
                    break;
                case ("join observer"):
                    break;
                default:
                    System.out.println("\033[31mInvalid command:\033[39m " + input);
            }
        }
    }

    public static void printPreLoginUI() {
        System.out.println("\t\033[36;107;1m[Logged Out UI]\033[39;49;0m");
    }

    public static void printRegister() {
        System.out.println("\033[32mregister\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m \033[31m<EMAIL>\033[39m - create account");
    }

    public static void printLogin() {
        System.out.println("\033[32mlogin\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m - login");
    }

    private static void printExit() {
        System.out.println("\033[32mquit\033[39m - exit application");
    }

    private static void printHelp() {
        System.out.println("\033[32mhelp\033[39m - display possible commands");
    }

    private static void printCreateGame() {

    }

    private static void printListGames() {

    }

    private static void printJoinGame() {

    }

    private static void printJoinObserver() {

    }

    public static void printLogout() {
        System.out.println("\033[32mlogout\033[39m - display possible commands");
    }

    private String[] getCommands() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return line.split(" ");
    }
}
//TODO: Refactor all print helpers statements into a static PrintStatements class
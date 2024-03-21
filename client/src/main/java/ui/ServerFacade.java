package ui;

import java.util.Scanner;
import server.Server;

public class ServerFacade {
    private final ServerFacadeHandlers handlers;

    public ServerFacade(int port) {
        handlers = new ServerFacadeHandlers();
    }

    public static void main(String[] args) {
        Server server = new Server();
        int port = 8080;
        server.run(port);
        ServerFacade serverFacade = new ServerFacade(port);

        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");

        serverFacade.preLogin();
        server.stop();
        System.exit(0);
    }

    private void preLogin() {
        System.out.println("[Logged Out UI]");

        String input = "";
        while (!input.equalsIgnoreCase("quit")) {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            String[] commands = line.split(" ");
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
                    System.out.println("\033[32mquit\033[39m - exit application");
                    System.out.println("\033[32mhelp\033[39m - display possible commands");
                    break;
                case ("exit"):
                    input = "quit";
                case ("quit"):
                    System.out.println("Exiting...");
                    break;
                //TODO: Have login and register return a bool if successful or something
                case ("login"):
                    if(handlers.login(params)) postLogin();
                    //I believe I have to make a Client class and set a public variable authToken to the returned authToken
                    break;
                case ("register"):
                    if (handlers.register(params)) postLogin();
                    break;
                default:
                    System.out.println("Invalid command: " + input);
            }
        }
    }

    public static void postLogin() {
        System.out.println("[Logged In UI]");
    }

    public static void printRegister() {
        System.out.println("\033[32mregister\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m \033[31m<EMAIL>\033[39m - create account");
    }

    public static void printLogin() {
        System.out.println("\033[32mlogin\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m - login");
    }
}

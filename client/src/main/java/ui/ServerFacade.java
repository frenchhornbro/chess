package ui;
import java.util.Scanner;

public class ServerFacade {
    public ServerFacade(int port) {
        //ServerFacadeTests will start the server for us.
        //However, when a client runs this automatically, we have to start the server in this code.
    }

    public static void main(String[] args) {
        System.out.println(EscapeSequences.BLACK_KING + "Welcome to the game of Chess." + EscapeSequences.BLACK_QUEEN);
        System.out.println("\tType \033[32mhelp\033[39m to get started.");
        System.out.println("[Logged Out UI]");

        String input = "";
        while (!input.equals("quit")) {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            String[] commands = line.split(" ");
            input = commands[0];
            switch (input.toLowerCase()) {
                case ("help"):
                    if (commands.length > 1) {
                        System.out.println("\033[32mhelp\033[39m cannot receive parameters");
                        break;
                    }
                    System.out.println("\033[32mregister\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m \033[31m<EMAIL>\033[39m - create account");
                    System.out.println("\033[32mlogin\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m - login");
                    System.out.println("\033[32mquit\033[39m - exit application");
                    System.out.println("\033[32mhelp\033[39m - display possible commands");
                    break;
                case ("quit"):
                    System.out.println("Exiting...");
                    break;
                case ("login"):
                    System.out.println("Command was " + input);
                    //TODO: Call the login function from the server, then move to Logged In UI
                    //How do we do this? Probably use an HttpURLConnection
                    break;
                case ("register"):
                    System.out.println("Command was " + input);
                    //TODO: Call the register function from the server, then move to Logged In UI
                    break;
                default:
                    System.out.println("Invalid command: " + input);
            }
        }
    }

    private void preLogin() {
        /*
        Pre commands:
            Help
            Quit
            Login
            Register
        */
        String input = "";

    }
}

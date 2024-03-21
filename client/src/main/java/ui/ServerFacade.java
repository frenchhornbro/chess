package ui;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import server.Server;

public class ServerFacade {
    public ServerFacade(int port) {

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
            String[] params = new String[commands.length-1];
            System.arraycopy(commands, 1, params, 0, commands.length - 1);
            switch (input.toLowerCase()) {
                case ("help"):
                    if (commands.length > 1) {
                        System.out.println("\033[32mhelp\033[39m cannot receive parameters");
                        break;
                    }
                    printRegister();
                    System.out.println("\033[32mlogin\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m - login");
                    System.out.println("\033[32mquit\033[39m - exit application");
                    System.out.println("\033[32mhelp\033[39m - display possible commands");
                    break;
                case ("exit"):
                    input = "quit";
                case ("quit"):
                    System.out.println("Exiting...");
                    break;
                case ("login"):
                    //TODO: Call the login function from the server, then move to Logged In UI
                    try {
                        URI uri = new URI("http://localhost:8080/session");
                        URL url = uri.toURL();
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.connect();
                        try (InputStream response = connection.getInputStream()) {
                            InputStreamReader reader = new InputStreamReader(response);
                            System.out.println(new Gson().fromJson(reader, Map.class));
                        }
                        //I believe I have to make a Client class and set a public variable authToken to the returned authToken
                        //Maybe open up the server in the Client class
                    }
                    catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case ("register"):
                    register(params);
                    break;
                default:
                    System.out.println("Invalid command: " + input);
            }
        }
    }

    private void register(String[] params) {
        //TODO: Move to Logged In UI
        if (params.length != 3) {
            System.out.println("Incorrect number of parameters");
            printRegister();
            return;
        }
        try {
            String[] titles = {"username", "password", "email"};

            HttpURLConnection http = getHttpURLConnection("POST", true, true, titles, params);
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                System.out.println(new Gson().fromJson(reader, Map.class));
            }
            //I believe I have to make a Client class and set a public variable authToken to the returned authToken
        }
        catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
            //TODO: This is just printing the error code, not the message...
        }
    }

    private HttpURLConnection getHttpURLConnection(String requestMethod, boolean doInput, boolean doOutput,
                                                   String[] fields, String[] params) throws Exception {
        if (fields.length != params.length) throw new Exception("Error: Fields and params were of different length");
        URI uri = new URI("http://localhost:8080/user");
        URL url = uri.toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setConnectTimeout(5000);
        http.setRequestMethod(requestMethod);
        http.setDoInput(doInput);
        http.setDoOutput(doOutput);

        //Header
//        http.addRequestProperty("Content-Type", "application/json");

        //Body
        HashMap<String, String> bodyVars = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            bodyVars.put(fields[i], params[i]);
        }
        try (OutputStream outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(bodyVars);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();
        return http;
    }

    private void printRegister() {
        System.out.println("\033[32mregister\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m \033[31m<EMAIL>\033[39m - create account");
    }
}

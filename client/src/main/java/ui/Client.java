package ui;

import ServerFacade.ServerFacade;
import server.Server;

public class Client {
    private String authToken;
    public Client() {
        authToken = null;
    }

    public static void main(String[] args) {
        //Set up server and client
        Server server = new Server();
        int port = 8080;
        server.run(port);
        ServerFacade serverFacade = new ServerFacade();
        Client client = new Client();

        //Display UIs
        serverFacade.start(client);

        //Stop server
        server.stop();
        System.exit(0);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String newAuthToken) {
        authToken = newAuthToken;
    }
}

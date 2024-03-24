package ui;

import ServerFacade.ServerFacade;
import server.Server;

import java.util.ArrayList;

public class Client {
    private String authToken;
    private ArrayList<Integer> gameIDs;
    public Client() {
        authToken = null;
        gameIDs = new ArrayList<>();
    }

    public static void main(String[] args) {
        //Set up server and client
        Server server = new Server();
        int port = 8080;
        server.run(port);
        ServerFacade serverFacade = new ServerFacade(false, new ArrayList<>());
        Client client = new Client();

        //Display UIs
        serverFacade.start(port, client);

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

    public ArrayList<Integer> getGameIDs() {
        return gameIDs;
    }

    public void setGameIDs(ArrayList<Integer> newGameIDs) {
        gameIDs = newGameIDs;
    }
}

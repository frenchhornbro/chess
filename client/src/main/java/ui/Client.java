package ui;

import ServerFacade.ServerFacade;
import java.util.ArrayList;

public class Client {
    private String authToken;
    private ArrayList<Integer> gameIDs;
    public Client() {
        authToken = null;
        gameIDs = new ArrayList<>();
    }

    public static void main(String[] args) {
        //Set up client
        ServerFacade serverFacade = new ServerFacade(false, new ArrayList<>());
        int port = 8080;
        Client client = new Client();

        //Display UIs
        serverFacade.start(port, client);
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

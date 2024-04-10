package ui;

import ServerFacade.ServerFacade;
import webSocket.WebSocketClient;
import java.util.ArrayList;

public class Client {
    private String authToken;
    private ArrayList<Integer> gameIDs;
    public WebSocketClient webSocketClient;
    public Client(int port) {
        authToken = null;
        gameIDs = new ArrayList<>();
        webSocketClient = new WebSocketClient();
        webSocketClient.instantiateWS(port);
    }

    public static void main(String[] args) {
        //Set up client
        ServerFacade serverFacade = new ServerFacade(false, new ArrayList<>());
        int port = 8080;
        Client client = new Client(port);

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

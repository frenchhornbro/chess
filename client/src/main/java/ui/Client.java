package ui;

import ServerFacade.ServerFacade;
import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;

public class Client extends Endpoint {
    private String authToken;
    private ArrayList<Integer> gameIDs;
    private Session session;
    public Client(int port) {
        authToken = null;
        gameIDs = new ArrayList<>();
        instantiateWS(port);
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

    private void instantiateWS(int port) {
        try {
            URI uri = new URI("ws://localhost:" + port + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    System.out.println(message);
                }
            });
        }
        catch (Exception ex) {
            System.out.println("The client's WebSocket instantiation encountered an error: " + ex.getMessage());
        }
    }

    public void send (String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

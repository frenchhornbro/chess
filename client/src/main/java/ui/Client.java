package ui;

import ServerFacade.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
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
                    Gson serial = new Gson();
                    ServerMessage serverMessage = serial.fromJson(message, ServerMessage.class);
                    if (serverMessage.getGame() != null) {
                        ChessGame chessGame = serverMessage.getGame();
                        ChessBoard board = chessGame.getBoard();
                        //TODO: Print the board from here, not in the GameplayUI
                        //TODO: Get rid of the /board endpoint. We're going to get the chessboard from this WebSocket message. Probably.
                        GameplayDrawer.draw(board, serverMessage.getMessage(), null);
                        System.out.print("> ");
                    }
                    else {
                        System.out.println("Message: " + serverMessage.getMessage());
                    }
                }
            });
        }
        catch (Exception ex) {
            System.out.println("The client's WebSocket instantiation encountered an error: " + ex.getMessage());
        }
    }

    public void send (UserGameCommand command) {
        try {
            String msg = new Gson().toJson(command);
            this.session.getBasicRemote().sendText(msg);
        }
        catch (Exception ex) {
            System.out.print("Error: Message not sent");
        }
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

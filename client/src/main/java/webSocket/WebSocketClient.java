package webSocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.GameplayDrawer;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import javax.websocket.*;
import java.net.URI;
import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

public class WebSocketClient extends Endpoint {
    private Session session;
    public ChessGame game;
    public ChessBoard board;
    public String playerColor;
    public boolean resigned;

    public void instantiateWS(int port) {
        try {
            URI uri = new URI("ws://localhost:" + port + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson serial = new Gson();
                    ServerMessage serverMessage = serial.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ERROR:
                            System.out.println(serverMessage.getErrorMessage());
                            System.out.print("> ");
                            break;
                        case LOAD_GAME:
                            if (serverMessage.getPlayerColor() != null) playerColor = serverMessage.getPlayerColor();
                            if (serverMessage.getGame() != null) {
                                game = serverMessage.getGame();
                                board = serverMessage.getGame().getBoard();
                                resigned = (serverMessage.getGame().getGameOver() != 0);
                                GameplayDrawer.draw(game, playerColor, null, resigned);
                            }
                            System.out.print("> ");
                            break;
                        case NOTIFICATION:
                        default:
                            if (serverMessage.getMessage().contains("resign")) {
                                resigned = true;
                                GameplayDrawer.draw(game, playerColor, null, true);
                            }
                            System.out.print(serverMessage.getMessage());
                    }
                }
            });
        }
        catch (Exception ex) {
            System.out.println("The client's WebSocket instantiation encountered an error: " + ex.getMessage());
        }
    }

    public void join(String authToken, String gameID, String playerColor) {
        send(new UserGameCommand(JOIN_PLAYER, authToken, gameID, playerColor));
    }

    public void observe(String authToken, String gameID) {
        send(new UserGameCommand(JOIN_OBSERVER, authToken, gameID));
    }

    public void leave(String authToken, String gameID) {
        send(new UserGameCommand(LEAVE, authToken, gameID));
    }

    public void move(String authToken, String gameID, ChessMove move) {
        send(new UserGameCommand(MAKE_MOVE, authToken, gameID, move));
    }

    public void resign(String authToken, String gameID) {
        send(new UserGameCommand(RESIGN, authToken, gameID));
    }

    private void send(UserGameCommand command) {
        try {
            String msg = new Gson().toJson(command);
            this.session.getBasicRemote().sendText(msg);
        }
        catch (Exception ex) {
            System.out.println("Error: Message not sent");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
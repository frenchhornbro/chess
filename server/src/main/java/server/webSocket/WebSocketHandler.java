package server.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        HashMap<String, Object> command = new Gson().fromJson(message, HashMap.class);
        System.out.println("Server received a message: " + message);
        String authToken = (String) command.get("authToken");
        SQLAuthDAO authDAO = new SQLAuthDAO();
        if (authDAO.getAuth(authToken) == null) {
            sendError(session, "Error: unauthorized");
            return;
        }
        String username = authDAO.getUser(authToken);
        Object objGameID = command.get("gameID");
        String gameID = String.valueOf(objGameID);
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        ChessGame game = sqlGameDAO.getGame(gameID);
        String commandType = (String) command.get("commandType");
        String playerColor = (String) command.get("playerColor");
        switch (commandType) {
            case "JOIN_PLAYER":
                join(session, username, game, playerColor);
                break;
            case "JOIN_OBSERVER":
                observe(session, username, game);
                break;
            case "MAKE_MOVE":
                System.out.println("A move was made");
                break;
            case "LEAVE":
                System.out.println("Someone left");
                break;
            case "RESIGN":
                System.out.println("A player resigned");
                break;
            default:
                System.out.println("Incorrect message sent: " + message);
        }
    }

    private void sendError(Session session, String errorMsg) {
        try {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setErrorMessage(errorMsg);
            session.getRemote().sendString(new Gson().toJson(error));
        }
        catch (Exception ex) {
            System.out.println("Error: could not send error message to connection");
        }
    }

    private void join(Session session, String username, ChessGame game, String playerColor) {
        ServerMessage userMsg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        userMsg.setGame(game);
        userMsg.setMessage(playerColor);
        ServerMessage broadcastMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        broadcastMsg.setMessage(username + " has joined as " + playerColor);
        connections.add(username, session);
        connections.broadcast(username, userMsg, broadcastMsg);
        System.out.println(broadcastMsg.getMessage());
    }

    private void observe(Session session, String username, ChessGame game) {
        ServerMessage userMsg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        userMsg.setGame(game);
        ServerMessage broadcastMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        broadcastMsg.setMessage(username + " has joined as an observer");
        connections.add(username, session);
        connections.broadcast(username, userMsg, broadcastMsg);
        System.out.println(broadcastMsg.getMessage());
    }
}

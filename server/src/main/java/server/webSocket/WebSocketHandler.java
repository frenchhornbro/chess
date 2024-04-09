package server.webSocket;

import chess.ChessBoard;
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
        Gson serial = new Gson();
        HashMap<String, Object> command = serial.fromJson(message, HashMap.class);
        System.out.println("Server received a message: " + message);
        String authToken = (String) command.get("authToken");
        SQLAuthDAO authDAO = new SQLAuthDAO();
        String username = authDAO.getUser(authToken);
//        String gameID = (String) command.get("gameID");
//        SQLGameDAO sqlGameDAO = new SQLGameDAO();
//        ChessBoard board = sqlGameDAO.getBoard(Integer.parseInt(gameID));
        String commandType = (String) command.get("commandType");
        String playerColor = (String) command.get("playerColor");
        switch (commandType) {
            case "JOIN_PLAYER":
                ServerMessage userMsg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                userMsg.setGame(new ChessGame(ChessGame.TeamColor.WHITE, new ChessBoard(true))); //TODO: Need to get the actual ChessGame
                ServerMessage broadcastMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                broadcastMsg.setMessage(username + " has joined as " + playerColor);
                connections.add(username, session);
                connections.broadcast(username, userMsg, broadcastMsg);
                System.out.println(broadcastMsg.getMessage());
                break;
            case "JOIN_OBSERVER":
                session.getRemote().sendString("An observer joined");
                System.out.println("An observer joined");
                break;
            case "MAKE_MOVE":
                session.getRemote().sendString("A move was made");
                System.out.println("A move was made");
                break;
            case "LEAVE":
                session.getRemote().sendString("Someone left");
                System.out.println("Someone left");
                break;
            case "RESIGN":
                session.getRemote().sendString("A player resigned");
                System.out.println("A player resigned");
                break;
            default:
                session.getRemote().sendString("Incorrect message sent: " + message);
                System.out.println("Incorrect message sent: " + message);
        }
    }
}

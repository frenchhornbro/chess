package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();
    public void add (String username, String gameID, Session session) {
        Connection newConnection = new Connection(username, session);
        if (connections.get(gameID) == null) {
            ConcurrentHashMap<String, Connection> newGameLobby = new ConcurrentHashMap<>();
            newGameLobby.put(username, newConnection);
            connections.put(gameID, newGameLobby);
        }
        else connections.get(gameID).put(username, newConnection);
    }

    public void remove(String username, String gameID) {
        connections.get(gameID).remove(username);
    }

    public void broadcast (String gameID, String originUser, ServerMessage userMessage, ServerMessage broadcastMessage) {
        for (Connection connection : connections.get(gameID).values()) {
            if (connection.session.isOpen()) {
                if (connection.username.equals(originUser)) {
                    if (userMessage != null) connection.send(new Gson().toJson(userMessage));
                }
                else if (broadcastMessage != null) connection.send(new Gson().toJson(broadcastMessage));
            }
            else {
                remove(connection.username, gameID);
            }
        }
    }
}

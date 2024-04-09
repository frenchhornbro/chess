package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public void add (String username, Session session) {
        connections.put(username, new Connection(username, session));
    }
    public void broadcast (String originUser, ServerMessage userMessage, ServerMessage broadcastMessage) {
        for (Connection connection : connections.values()) {
            if (connection.session.isOpen()) {
                if (connection.username.equals(originUser)) {
                    if (userMessage != null) connection.send(new Gson().toJson(userMessage));
                    else System.out.println("userMessage is null");
                }
                else if (broadcastMessage != null) connection.send(new Gson().toJson(broadcastMessage));
                else System.out.println("broadcastMessage is null");
            }
            else {
                connections.remove(connection.username);
                System.out.println("A connection was removed for " + connection.username);
            }
        }
    }
}

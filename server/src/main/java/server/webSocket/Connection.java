package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    public String username;
    public Session session;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void send(String json) {
        try {
            session.getRemote().sendString(json);
        }
        catch(Exception ex) {
            System.out.println("Message failed to send to connection");
        }
    }
}
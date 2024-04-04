package server;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

@WebSocket
public class WebSocketServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WebSocketServer.class);
        Spark.get("/echo/:msg", (request, response) -> "HTTP response: " + request.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        switch (message) {
            case ("JOIN_PLAYER"):
                session.getRemote().sendString("A player joined");
                break;
            case ("JOIN_OBSERVER"):
                session.getRemote().sendString("An observer joined");
                break;
            case ("MAKE_MOVE"):
                session.getRemote().sendString("A move was made");
                break;
            case ("LEAVE"):
                session.getRemote().sendString("Someone left");
                break;
            case ("RESIGN"):
                session.getRemote().sendString("A player resigned");
                break;
            default:
                session.getRemote().sendString("Incorrect message sent: " + message);
        }
    }
}

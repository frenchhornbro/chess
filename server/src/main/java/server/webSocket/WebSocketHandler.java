package server.webSocket;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {
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

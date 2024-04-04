package ui;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WebSocketClient extends Endpoint {
    public static void main(String[] args) throws Exception {
        var ws = new WebSocketClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo:");
        String input = scanner.nextLine();
        while (!input.equals("quit")) {
            ws.send(input);
            input = scanner.nextLine();
        }
    }

    private final Session session;
    public WebSocketClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
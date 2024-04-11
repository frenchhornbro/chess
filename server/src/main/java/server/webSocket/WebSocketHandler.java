package server.webSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataStorage.GameStorage;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import java.util.HashMap;
import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private SQLGameDAO gameDAO;
    private SQLAuthDAO authDAO;

    public WebSocketHandler() {
        try {
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        HashMap<String, Object> command = new Gson().fromJson(message, HashMap.class);
        System.out.println("Server received a message: " + message);
        String authToken = (String) command.get("authToken");
        if (authDAO.getAuth(authToken) == null) {
            sendError(session, "unauthorized");
            return;
        }
        Object objGameID = command.get("gameID");
        String gameID = String.valueOf(objGameID);
        if (gameDAO.gameIsNull((int) Double.parseDouble(gameID))) {
            sendError(session, "invalid ID");
            return;
        }
        String username = authDAO.getUser(authToken);
        System.out.println("onMessage username = " + username);
        GameStorage gameData = gameDAO.getGameData(gameID, true);
        String commandType = (String) command.get("commandType");
        String playerColor = (String) command.get("playerColor");
        ChessMove chessMove = null;
        if (command.get("move") != null) {
            System.out.println(command.get("move").toString().replace("=", ":"));
            chessMove = new Gson().fromJson(command.get("move").toString().replace("=", ":"), ChessMove.class);
        }
        switch (commandType) {
            case "JOIN_PLAYER" -> join(session, gameID, username, playerColor, gameData);
            case "JOIN_OBSERVER" -> observe(session, gameID, username, gameData);
            case "MAKE_MOVE" -> move(session, gameID, username, chessMove, gameData);
            case "LEAVE" -> leave(session, gameID, username);
            case "RESIGN" -> resign(session, gameID, username, gameData);
            default -> System.out.println("Incorrect message sent: " + message);
        }
    }

    private static boolean incorrectUser(Session session, GameStorage gameData, String playerColor, String username) {
        if (playerColor == null) {
            sendError(session, "color not specified for the user " + username);
            return true;
        }
        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (gameData.getWhiteUsername() == null) {
                sendError(session, "game wasn't joined");
                return true;
            }
            if (!gameData.getWhiteUsername().equals(username)) {
                sendError(session, "WHITE is already taken");
                return true;
            }
        }
        else if (playerColor.equalsIgnoreCase("BLACK")) {
            if (gameData.getBlackUsername() == null) {
                sendError(session, "game wasn't joined");
                return true;
            }
            if (!gameData.getBlackUsername().equals(username)) {
                sendError(session, "BLACK is already taken");
                return true;
            }
        }
        else {
            sendError(session, "invalid color");
            return true;
        }
        return false;
    }

    private boolean actionValid (Session session, GameStorage gameData, String username) {
        try {
            switch (gameData.getGame().getTeamTurn()) {
                case WHITE:
                    if (!username.equals(gameData.getWhiteUsername())) {
                        sendError(session, "you can't move for WHITE");
                        return false;
                    }
                    break;
                case BLACK:
                    if (!username.equals(gameData.getBlackUsername())) {
                        sendError(session, "you can't move for BLACK");
                        return false;
                    }
                    break;
                default:
                    sendError(session, "invalid color");
                    return false;
            }
            return true;
        }
        catch (Exception ex) {
            sendError(session, ex.getMessage());
            return false;
        }
    }

    private static void sendError(Session session, String errorMsg) {
        try {
            ServerMessage error = new ServerMessage(ERROR);
            error.setErrorMessage("Error: " + errorMsg);
            System.out.println(error.getErrorMessage());
            session.getRemote().sendString(new Gson().toJson(error));
        }
        catch (Exception ex) {
            System.out.println("Error: could not send error message to connection");
        }
    }

    private void join(Session session, String gameID, String username, String playerColor, GameStorage gameData) {
        try {
            if (incorrectUser(session, gameData, playerColor, username)) return;
            ServerMessage userMsg = new ServerMessage(LOAD_GAME);
            userMsg.setGame(gameData.getGame());
            userMsg.setPlayerColor(playerColor);
            ServerMessage broadcastMsg = new ServerMessage(NOTIFICATION);
            broadcastMsg.setMessage(username + " has joined as " + playerColor + "\n> ");
            connections.add(username, gameID, session);
            connections.broadcast(gameID, username, userMsg, broadcastMsg);
            System.out.println(broadcastMsg.getMessage());
        }
        catch (Exception ex) {
            sendError(session, ex.getMessage());
        }
    }

    private void observe(Session session, String gameID, String username, GameStorage gameData) {
        try {
            ServerMessage userMsg = new ServerMessage(LOAD_GAME);
            userMsg.setGame(gameData.getGame());
            ServerMessage broadcastMsg = new ServerMessage(NOTIFICATION);
            broadcastMsg.setMessage(username + " has joined as an observer\n> ");
            connections.add(username, gameID, session);
            connections.broadcast(gameID, username, userMsg, broadcastMsg);
            System.out.println(broadcastMsg.getMessage());
        }
        catch (Exception ex) {
            sendError(session, ex.getMessage());
        }
    }

    private void move(Session session, String gameID, String username, ChessMove move, GameStorage gameData) {
        try {
            if (gameData.getGame().getGameOver() != 0) {
                sendError(session, "game is over");
                return;
            }
            if (!actionValid(session, gameData, username)) return;
            gameData.getGame().makeMove(move);
            gameDAO.updateBoard(gameID, gameData.getGame().getBoard());
            gameDAO.updateGame(gameID, gameData.getGame());
            ServerMessage reloadMsg = new ServerMessage(LOAD_GAME);
            reloadMsg.setGame(gameData.getGame());
            connections.broadcast(gameID, username, reloadMsg, reloadMsg);
            ServerMessage notify = new ServerMessage(NOTIFICATION);
            notify.setMessage(username + " made a move: " + move + "\n> ");
            connections.broadcast(gameID, username, null, notify);
        }
        catch (Exception ex) {
            sendError(session, ex.getMessage());
        }
    }

    private void resign(Session session, String gameID, String username, GameStorage gameData) {
        try {
            if (!username.equals(gameData.getWhiteUsername()) && ! username.equals(gameData.getBlackUsername())) {
                sendError(session, "observers can't resign");
                return;
            }
            if (gameData.getGame().getGameOver() != 0) {
                sendError(session, "game is already over");
                return;
            }
            gameData.getGame().setGameOver(true);
            gameDAO.updateGame(gameID, gameData.getGame());

            ServerMessage resignMsg = new ServerMessage(NOTIFICATION);
            resignMsg.setMessage(username + " resigned\n> ");
            connections.broadcast(gameID, username, resignMsg, resignMsg);
            System.out.println(resignMsg.getMessage());
        }
        catch (Exception ex) {
            sendError(session, ex.getMessage());
        }
    }

    private void leave(Session session, String gameID, String username) {
        try {
            connections.remove(username, gameID);
            ServerMessage leaveMsg = new ServerMessage(NOTIFICATION);
            leaveMsg.setMessage(username + " left\n> ");
            connections.broadcast(gameID, username, null, leaveMsg);
            System.out.println(leaveMsg.getMessage());
        }
        catch (Exception ex) {
            sendError(session, ex.getMessage());
        }
    }

    @OnWebSocketError
    public void webSocketError(Session session, Throwable throwable) {
        System.out.println("Session disconnected");
    }
}
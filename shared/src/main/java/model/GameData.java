package model;

import chess.ChessGame;

import java.util.HashSet;

public class GameData {
    private final HashSet<String> observers;
    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private ChessGame game;

    public GameData(int gameID, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.gameName = gameName;
        this.game = game;
        this.observers = new HashSet<>();
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setWhiteUsername(String username) {
        this.whiteUsername = username;
    }

    public void setBlackUsername(String username) {
        this.blackUsername = username;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public void setObserver(String observer) {
        this.observers.add(observer);
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder();
        retStr.append("Game ID: \"");
        retStr.append(this.gameID);
        retStr.append("\" whiteUsername: \"");
        retStr.append(this.whiteUsername);
        retStr.append("\" blackUsername: \"");
        retStr.append(this.blackUsername);
        retStr.append("\" gameName: \"");
        retStr.append(this.gameName);
        retStr.append("\", observers: \"");
        retStr.append(this.observers);
        retStr.append("\"");
        return retStr.toString();
    }
}

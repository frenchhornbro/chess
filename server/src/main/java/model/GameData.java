package model;

import chess.ChessGame;

import java.util.HashSet;

public class GameData {
    private final HashSet<String> observers;
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
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

    public HashSet<String> getObservers() {
        return this.observers;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
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

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setObserver(String observer) {
        this.observers.add(observer);
    }
}

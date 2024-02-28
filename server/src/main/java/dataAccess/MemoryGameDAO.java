package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO {
    private final HashMap<String, GameData> gameDataBase = new HashMap<>();
    private int currGameNum;

    public MemoryGameDAO() {
        this.currGameNum = 0;
    }

    public GameData createGame(String gameName) {
        //GameData contains the following:
        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        //Not all of this has to be set, however.
        //While creating a game, a gameName should be inserted externally
        //Users will eventually be set
        //ChessGame and gameID should be set automatically
        int gameID = currGameNum++;
        return new GameData(gameID, gameName, new ChessGame(null, new ChessBoard()));
    }

    public void delete() {
        throw new RuntimeException("Not implemented yet");
    }
    public void clear() {
        gameDataBase.clear();
    }
}

package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO {
    private final HashMap<Integer, GameData> gameDataBase = new HashMap<>();
    private int currGameNum;

    public MemoryGameDAO() {
        this.currGameNum = 1;
    }

    public GameData createGame(String gameName) {
        //GameData contains the following:
        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        //Not all of this has to be set, however.
        //While creating a game, a gameName should be inserted externally
        //Users will eventually be set
        //ChessGame and gameID should be set automatically
        int gameID = currGameNum++;
        GameData game = new GameData(gameID, gameName, new ChessGame(null, new ChessBoard()));
        gameDataBase.put(gameID, game);
        return game;
    }

    public GameData getGame(int gameID) {
        return gameDataBase.get(gameID);
    }

    public HashMap<Integer, GameData> getGames() {
        return gameDataBase;
    }

    public void updateGame(GameData game, String playerColor, String username) throws DataAccessException {
        if (playerColor.equals("WHITE")){
            if (game.getWhiteUsername() != null) throw new DataAccessException("Error: already taken");
            game.setWhiteUsername(username);
        }
        else if (playerColor.equals("BLACK")) {
            if (game.getBlackUsername() != null) throw new DataAccessException("Error: already taken");
            game.setBlackUsername(username);
        }
        else game.setObserver(username);
    }
    public void clear() {
        gameDataBase.clear();
    }
}

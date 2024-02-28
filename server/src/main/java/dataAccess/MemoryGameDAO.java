package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO {
    private final HashMap<Integer, GameData> gameDataBase = new HashMap<>();
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
        GameData game = new GameData(gameID, gameName, new ChessGame(null, new ChessBoard()));
        gameDataBase.put(gameID, game);
        return game;
    }

    public GameData getGame(int gameID) {
        return gameDataBase.get(gameID);
    }

    public void updateGame(GameData game, String playerColor, String username) throws DataAccessException {
        if (playerColor.equals("WHITE")){
        //TODO: Maybe check this is already set and throw an error if it is?
            game.setWhiteUsername(username);
        }
        else if (playerColor.equals("BLACK")) {
            game.setBlackUsername(username);
        }
        else throw new DataAccessException("Error: bad request");
        //TODO: If getColor is already set, throw error 403
    }

    public void delete() {
        throw new RuntimeException("Not implemented yet");
    }
    public void clear() {
        gameDataBase.clear();
    }
}

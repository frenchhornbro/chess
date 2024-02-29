package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO {
    private final ArrayList<GameData> gameDataBase = new ArrayList<>();
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
        gameDataBase.add(game);
        return game;
    }

    public GameData getGame(int gameID) {
        return gameDataBase.get(gameID);
    }

    public ArrayList<GameData> getGames() {
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

    public void delete() {
        throw new RuntimeException("Not implemented yet");
    }
    public void clear() {
        gameDataBase.clear();
    }
}

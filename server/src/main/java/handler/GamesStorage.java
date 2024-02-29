package handler;

import model.GameData;
import java.util.ArrayList;

public class GamesStorage {
    private final ArrayList<GameStorage> games;

    public GamesStorage(ArrayList<GameData> games) {
        this.games = new ArrayList<>();
        for (GameData game : games) {
            this.games.add(new GameStorage(game));
        }
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder();
        retStr.append("{");
        for (GameStorage game : this.games) {
            retStr.append("[GameID: \"");
            retStr.append(game.gameID);
            retStr.append("\", WhiteUserName: \"");
            retStr.append(game.whiteUsername);
            retStr.append("\", BlackUserName: \"");
            retStr.append(game.blackUsername);
            retStr.append("\", GameName: \"");
            retStr.append(game.gameName);
            retStr.append("\"]");
        }
        retStr.append("}");
        return retStr.toString();
    }

    private static class GameStorage {
        private final int gameID;
        private final String whiteUsername;
        private final String blackUsername;
        private final String gameName;

        private GameStorage(GameData game) {
            this.gameID = game.getGameID();
            this.whiteUsername = (game.getWhiteUsername() == null) ? "" : game.getWhiteUsername();
            this.blackUsername = (game.getBlackUsername() == null) ? "" : game.getBlackUsername();
            this.gameName = (game.getGameName() == null) ? "" : game.getGameName();
        }
    }
}

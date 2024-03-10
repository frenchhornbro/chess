package dataStorage;

import java.util.ArrayList;

public class GamesStorage {
    private final ArrayList<GameStorage> games;

    public GamesStorage(ArrayList<GameStorage> games) {
        this.games = games;
    }

    public ArrayList<GameStorage> getGames() {
        return games;
    }

    @Override
    public String toString() {
        StringBuilder retStr = new StringBuilder();
        retStr.append("{");
        for (GameStorage game : this.games) {
            retStr.append("[GameID: \"");
            retStr.append(game.getGameID());
            retStr.append("\", WhiteUserName: \"");
            retStr.append(game.getWhiteUsername());
            retStr.append("\", BlackUserName: \"");
            retStr.append(game.getBlackUsername());
            retStr.append("\", GameName: \"");
            retStr.append(game.getGameName());
            retStr.append("\"]");
        }
        retStr.append("}");
        return retStr.toString();
    }
}

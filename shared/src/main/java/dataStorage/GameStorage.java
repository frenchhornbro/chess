package dataStorage;

public class GameStorage {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;

    public GameStorage(int gameID, String whiteUsername, String blackUsername, String gameName) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
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

    @Override
    public String toString() {
        return "GameID = " + gameID + ", GameName = " + gameName + ", WhiteUsername = " + whiteUsername
                + ", BlackUsername = " + blackUsername;
    }
}

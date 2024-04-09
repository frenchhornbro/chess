package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    protected CommandType commandType;
    private final String authToken;
    private String gameID;
    private String playerColor;

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public UserGameCommand(CommandType commandType, String authToken, String gameID, String playerColor) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN

    }

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public String getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}

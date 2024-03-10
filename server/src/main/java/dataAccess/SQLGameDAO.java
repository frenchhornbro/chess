package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class SQLGameDAO extends SQLDAO {
    private final HashMap<Integer, GameData> gameDataBase = new HashMap<>();

    public SQLGameDAO() throws Exception {

    }

    public int createGame(String gameName) throws DataAccessException {
        /*
        GameData contains the following:
        int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        Not all of this has to be set, however.
        Users will eventually be set
        ChessGame and gameID should be set automatically
        Return the gameID
        */
        try {
            String createDataStatement = "INSERT INTO gameData (gameName) VALUES (?)";
            int gameID = updateDB(createDataStatement, gameName);
            //TODO: Figure out how to serialize the ChessGame
            // I think the ChessBoard needs to be turned into a JSON object
            //  And then we have to write something to repopulate it for the get method(?)

            ChessBoard chessBoard = new ChessBoard(true);
            ChessGame chessGame = new ChessGame(null, chessBoard);
            String createGameStatement = """
                INSERT INTO chessGame
                (gameID, teamTurn, stalemate, checkmate,
                wKingRookMoved, wQueenRookMoved, wKingMoved,
                bKingRookMoved, bQueenRookMoved, bKingMoved)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
            updateDB(createGameStatement,
                gameID, chessGame.getTeamTurn(), chessGame.getStalemate(), chessGame.getCheckmate(),
                chessGame.getWKingRookMoved(), chessGame.getWQueenRookMoved(), chessGame.getWKingMoved(),
                chessGame.getBKingRookMoved(), chessGame.getBQueenRookMoved(), chessGame.getBKingMoved()
                );
            String createBoardStatement = "INSERT INTO chessBoard (gameID, board) VALUES (?, ?)";
            updateDB(createBoardStatement, gameID, chessBoard.toString());
            //TODO ^^^ do we want to do a toString(), or a JSON conversion?

            return gameID;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }

    }

    public boolean gameIsNull (int gameID) throws DataAccessException {
        try {
            String queryStatement = "SELECT gameID FROM gameData where gameID=?";
            return (queryDB(queryStatement, gameID) == null);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public HashMap<Integer, GameData> getGames() {
        return gameDataBase;
    }

    public void updateGame(int gameID, String playerColor, String authToken) throws DataAccessException {
        //If playerColor is  WHITE or BLACK (not already taken), set them accordingly
        //If playerColor is null or some other String, set them as an observer
        try {
            String getUsernameStatement = "SELECT username FROM authData WHERE authToken=?";
            String username = queryDB(getUsernameStatement, authToken);
            if (playerColor == null) {
                String setObserverStatement = "INSERT INTO observers (gameID, username) VALUES (?, ?)";
                updateDB(setObserverStatement, gameID, username);
            }
            else if (playerColor.equals("WHITE")){
                String getWhiteUsernameStatement = "SELECT whiteUsername FROM gameData WHERE gameID=?";
                if (queryDB(getWhiteUsernameStatement, gameID) != null){
                    throw new DataAccessException("Error: already taken");
                }
                String setWhiteUsernameStatement = "UPDATE gameData SET whiteUsername=? WHERE gameID=?";
                updateDB(setWhiteUsernameStatement, username, gameID);
            }
            else if (playerColor.equals("BLACK")){
                String getBlackUsernameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";
                if (queryDB(getBlackUsernameStatement, gameID) != null) {
                    throw new DataAccessException("Error: already taken");
                }
                String setBlackUsernameStatement = "UPDATE gameData SET blackUsername=? WHERE gameID=?";
                updateDB(setBlackUsernameStatement, username, gameID);
            }
            else {
                String setObserverStatement = "INSERT INTO observers (gameID, username) VALUES (?, ?)";
                updateDB(setObserverStatement, gameID, username);
            }
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }

    }
    public void clear() throws Exception {
        String clearStatement = "DELETE FROM gameData";
        updateDB(clearStatement);
        clearStatement = "DELETE FROM chessGame";
        updateDB(clearStatement);
        clearStatement = "DELETE FROM chessBoard";
        updateDB(clearStatement);
    }
}

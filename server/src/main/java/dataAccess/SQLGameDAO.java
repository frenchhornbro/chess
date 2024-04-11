package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataStorage.GameStorage;
import dataStorage.GamesStorage;
import java.util.ArrayList;

public class SQLGameDAO extends SQLDAO {

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

            ChessBoard chessBoard = new ChessBoard(true);
            ChessGame chessGame = new ChessGame(null, chessBoard);
            String createGameStatement = "INSERT INTO chessGame (gameID, teamTurn, stalemate, checkmate, gameOver) VALUES (?, ?, ?, ?, ?);";
            updateDB(createGameStatement,
                gameID, chessGame.getTeamTurn(), chessGame.getStalemate(), chessGame.getCheckmate(), chessGame.getGameOver());
            String createBoardStatement = "INSERT INTO chessBoard (gameID, boardText) VALUES (?, ?)";
            updateDB(createBoardStatement, gameID, new Gson().toJson(chessBoard));
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

    public GamesStorage getGames(boolean includeAll) throws DataAccessException {
        //Get all the gameData, pack it into a GamesStorage Object, and return that
        try {
            String getGameIDsStatement = "SELECT gameID FROM gameData";
            ArrayList<String> gameIDs = queryArrayDB(getGameIDsStatement);

            ArrayList<GameStorage> games = new ArrayList<>();
            for (String gameID : gameIDs) {
                games.add(getGameData(gameID, includeAll));
            }
            return new GamesStorage(games);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public GameStorage getGameData(String gameID, boolean includeAll) throws DataAccessException {
        try {
            //From gameData
            String getWhiteUserNameStatement = "SELECT whiteUsername FROM gameData WHERE gameID=?";
            String whiteUsername = queryDB(getWhiteUserNameStatement, gameID);
            String getBlackUserNameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";
            String blackUsername = queryDB(getBlackUserNameStatement, gameID);
            String getGameNameStatement = "SELECT gameName FROM gameData WHERE gameID=?";
            String gameName = queryDB(getGameNameStatement, gameID);
            ChessGame game = (includeAll) ? getGame(gameID) : null;
            int intGameID = (int) Double.parseDouble(gameID);
            return new GameStorage(intGameID, whiteUsername, blackUsername, gameName, game);

        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public ChessGame getGame(String gameID) throws DataAccessException {
        try {
            //From chessGame
            String getTeamTurnStatement = "SELECT teamTurn FROM chessGame WHERE gameID=?";
            String teamTurnStr = queryDB(getTeamTurnStatement, gameID);
            ChessGame.TeamColor teamTurn = (teamTurnStr == null) ? null : switch (teamTurnStr.toUpperCase()) {
                case ("WHITE") -> ChessGame.TeamColor.WHITE;
                case ("BLACK") -> ChessGame.TeamColor.BLACK;
                default -> null;
            };
            String getStalemateStatement = "SELECT stalemate FROM chessGame WHERE gameID=?";
            boolean stalemate = (Integer.parseInt(queryDB(getStalemateStatement, gameID)) != 0);
            String getCheckmateStatement = "SELECT checkmate FROM chessGame WHERE gameID=?";
            boolean checkmate = (Integer.parseInt(queryDB(getCheckmateStatement, gameID)) != 0);
            String getGameOverStatement = "SELECT gameOver FROM chessGame WHERE gameID=?";
            boolean gameOver = (Integer.parseInt(queryDB(getGameOverStatement, gameID)) != 0);

            //From chessBoard
            ChessBoard board = getBoard((int) Double.parseDouble(gameID));
            return new ChessGame(teamTurn, board, stalemate, checkmate, gameOver);
        }
        catch (Exception ex) {
            System.out.print(ex.getMessage());
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void setGameValues(int gameID, String playerColor, String authToken) throws DataAccessException {
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

                //Prevent user from joining if that color for that game is already taken
                // (unless they were the one that took it)
                String query = queryDB(getWhiteUsernameStatement, gameID);
                if (query != null && !query.equals(username)){
                    throw new DataAccessException("Error: already taken");
                }
                String setWhiteUsernameStatement = "UPDATE gameData SET whiteUsername=? WHERE gameID=?";
                updateDB(setWhiteUsernameStatement, username, gameID);
            }
            else if (playerColor.equals("BLACK")){
                String getBlackUsernameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";

                //Prevent user from joining if that color for that game is already taken
                // (unless they were the one that took it)
                String query = queryDB(getBlackUsernameStatement, gameID);
                if (query != null && !query.equals(username)) {
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

    public ChessBoard getBoard(int gameID) throws Exception {
        String getBoardStatement = "SELECT boardText FROM chessBoard WHERE gameID=?";
        String boardText = queryDB(getBoardStatement, gameID);
        return new Gson().fromJson(boardText, ChessBoard.class);
    }

    public void updateBoard(String gameID, ChessBoard board) throws Exception {
        String updateBoardStatement = "UPDATE chessBoard SET boardText=? WHERE gameID=?";
        updateDB(updateBoardStatement, new Gson().toJson(board), gameID);
    }

    public void updateGame(String gameID, ChessGame game) throws Exception {
        String updateGameStatement = "UPDATE chessGame SET teamTurn=?, stalemate=?, checkmate=?, gameOver=? WHERE gameID=?";
        updateDB(updateGameStatement,game.getTeamTurn(), game.getStalemate(), game.getCheckmate(), game.getGameOver(), gameID);
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

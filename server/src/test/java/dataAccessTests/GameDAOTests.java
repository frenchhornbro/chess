package dataAccessTests;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameDAOTests extends DAOTests {
    private final SQLGameDAO sqlGameDAO;
    private final SQLAuthDAO sqlAuthDAO;

    public GameDAOTests() throws Exception {
        this.sqlGameDAO = new SQLGameDAO();
        this.sqlAuthDAO = new SQLAuthDAO();
    }

    @BeforeEach
    public void clearDB() throws Exception {
        sqlAuthDAO.clear();
        sqlGameDAO.clear();
    }

    @Test
    public void createGamePositive() throws Exception {
        //Create game
        String gameName = "myGame";
        int gameID = sqlGameDAO.createGame(gameName);
        String gameIDString = Integer.toString(gameID);

        //Test that the gameData is stored correctly
        String testStatement = "select gameName from gameData where gameID=?";
        Assertions.assertEquals(gameName, performQuery(testStatement, gameIDString));
        testStatement = "select gameName from gameData where gameID=?";
        Assertions.assertEquals(gameName, performQuery(testStatement, gameIDString));
        testStatement = "select whiteUsername from gameData where gameID=?";
        Assertions.assertNull(performQuery(testStatement, gameIDString));
        testStatement = "select blackUsername from gameData where gameID=?";
        Assertions.assertNull(performQuery(testStatement, gameIDString));

        //Test that the ChessGame data is stored correctly
        ChessBoard testBoard = new ChessBoard(true);
        ChessGame testGame = new ChessGame(null, testBoard);
        testStatement = "select gameID from chessGame where gameID=?";
        Assertions.assertEquals(gameIDString, performQuery(testStatement, gameIDString));
        testStatement = "select teamTurn from chessGame where gameID=?";
        String turn = (testGame.getTeamTurn() == null) ? null : testGame.getTeamTurn().toString();
        Assertions.assertEquals(turn, performQuery(testStatement, gameIDString));
        testStatement = "select stalemate from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select checkmate from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select wKingRookMoved from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select wQueenRookMoved from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select wKingMoved from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select bKingRookMoved from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select bQueenRookMoved from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
        testStatement = "select bKingMoved from chessGame where gameID=?";
        Assertions.assertEquals(Integer.toString(0), performQuery(testStatement, gameIDString));
    }

    @Test
    public void createGameNegative() throws Exception {
        //Create game
        String gameName = "myGame";
        sqlGameDAO.createGame(gameName);

        //Attempting to access a nonexistent game returns null
        Assertions.assertTrue(sqlGameDAO.gameIsNull(-987654321));
    }

    @Test
    public void updateGamePositive() throws Exception {
        //Create auths for two players
        String user1 = "username";
        String authToken1 = sqlAuthDAO.createAuth(user1);
        String user2 = "newUser";
        String authToken2 = sqlAuthDAO.createAuth(user2);

        //Create game
        String gameName = "Wow! A game!";
        int gameID = sqlGameDAO.createGame(gameName);
        String gameIDString = Integer.toString(gameID);

        //Update username into whiteUsername and blackUsername
        sqlGameDAO.setGameValues(gameID, "WHITE", authToken1);
        sqlGameDAO.setGameValues(gameID, "BLACK", authToken2);

        //Verify game has those usernames
        String testStatement = "select whiteUsername from gameData where gameID=?";
        Assertions.assertEquals(user1, performQuery(testStatement, gameIDString));
        testStatement = "select blackUsername from gameData where gameID=?";
        Assertions.assertEquals(user2, performQuery(testStatement, gameIDString));
    }

    @Test
    public void updateGameNegative() throws Exception {
        //Create auths for four players
        String authToken1 = sqlAuthDAO.createAuth("userOne");
        String authToken2 = sqlAuthDAO.createAuth("userTwo");
        String authToken3 = sqlAuthDAO.createAuth("userThree");
        String authToken4 = sqlAuthDAO.createAuth("userFour");

        //Create game
        String gameName = "Wow! A game!";
        int gameID = sqlGameDAO.createGame(gameName);

        //Update usernames into whiteUsername and blackUsername
        sqlGameDAO.setGameValues(gameID, "WHITE", authToken1);
        sqlGameDAO.setGameValues(gameID, "BLACK", authToken2);

        //Verify updating whiteUsername or blackUsername will throw an error
        Assertions.assertThrows(DataAccessException.class,
                                () -> sqlGameDAO.setGameValues(gameID, "WHITE", authToken3));
        Assertions.assertThrows(DataAccessException.class,
                () -> sqlGameDAO.setGameValues(gameID, "BLACK", authToken4));
    }

    @Test
    public void gameIsNullPositive() throws Exception {
        Assertions.assertTrue(sqlGameDAO.gameIsNull(-987654321));
    }

    @Test
    public void gameIsNullNegative() throws Exception {
        int gameID = sqlGameDAO.createGame("Too lazy to come up with a good name");
        Assertions.assertFalse(sqlGameDAO.gameIsNull(gameID));
    }

    @Test
    public void getGamesPositive() {
        //Create multiple games
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("Game 1"));
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("Game 2"));
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("Game 3"));
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.getGames(false));
    }

    @Test
    public void getGamesNegative() throws Exception {
        //When no games are added, a blank array is returned
        Assertions.assertEquals("{}", sqlGameDAO.getGames(false).toString());
    }

    @Test
    public void getBoardPositive() throws Exception {
        //A returned game is not null
        String gameName = "myGame";
        int gameID = sqlGameDAO.createGame(gameName);
        Assertions.assertNotNull(sqlGameDAO.getBoard(gameID));
    }

    @Test
    public void getBoardNegative() throws Exception {
        //A returned game for a false gameID has no attributes
        int gameID = 12345;
        Assertions.assertEquals("", sqlGameDAO.getBoard(gameID).toString());
    }

    @Test
    public void sqlGameClearPositive() throws Exception {
        //Clearing doesn't throw an error and clears the database
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("Game Num 1"));
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("Game Num 2"));
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("Game Num 3"));
        Assertions.assertDoesNotThrow(sqlGameDAO::clear);
        String query = "SELECT gameID FROM gameData WHERE gameName=?";
        Assertions.assertEquals("Not found", performQuery(query, "Game Num 1"));
        Assertions.assertEquals("Not found", performQuery(query, "Game Num 2"));
        Assertions.assertEquals("Not found", performQuery(query, "Game Num 3"));
    }
}

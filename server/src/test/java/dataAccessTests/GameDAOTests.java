package dataAccessTests;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
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
    public void createGameSuccess() throws Exception {
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

        //Test that the ChessBoard is stored correctly
        //TODO: We can compare if the to_string or to_Json or whatever works...
        // but what about that actual ChessBoard object itself?
        String boardString = testBoard.toString();
        testStatement = "select board from chessBoard where gameID=?";
        Assertions.assertEquals(boardString, performQuery(testStatement, gameIDString));

        //TODO: Also test if a blank ChessBoard can be stored
    }

    @Test
    public void createGameFailure() throws Exception {
        //TODO: How do we fail this???

    }

    @Test
    public void updateGameSuccess() throws Exception {
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
        sqlGameDAO.updateGame(gameID, "WHITE", authToken1);
        sqlGameDAO.updateGame(gameID, "BLACK", authToken2);

        //Verify game has those usernames
        String testStatement = "select whiteUsername from gameData where gameID=?";
        Assertions.assertEquals(user1, performQuery(testStatement, gameIDString));
        testStatement = "select blackUsername from gameData where gameID=?";
        Assertions.assertEquals(user2, performQuery(testStatement, gameIDString));
    }

    @Test
    public void updateGameFailure() throws Exception {
        //Create auths for four players
        String authToken1 = sqlAuthDAO.createAuth("userOne");
        String authToken2 = sqlAuthDAO.createAuth("userTwo");
        String authToken3 = sqlAuthDAO.createAuth("userThree");
        String authToken4 = sqlAuthDAO.createAuth("userFour");

        //Create game
        String gameName = "Wow! A game!";
        int gameID = sqlGameDAO.createGame(gameName);

        //Update usernames into whiteUsername and blackUsername
        sqlGameDAO.updateGame(gameID, "WHITE", authToken1);
        sqlGameDAO.updateGame(gameID, "BLACK", authToken2);

        //Verify updating whiteUsername or blackUsername will throw an error
        Assertions.assertThrows(DataAccessException.class,
                                () -> sqlGameDAO.updateGame(gameID, "WHITE", authToken3));
        Assertions.assertThrows(DataAccessException.class,
                () -> sqlGameDAO.updateGame(gameID, "BLACK", authToken4));
    }

    @Test
    public void sqlGameClearSuccess() throws Exception {

    }
}

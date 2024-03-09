package dataAccessTests;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.SQLGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameDAOTests extends DAOTests {
    private final SQLGameDAO sqlGameDAO;

    public GameDAOTests() throws Exception {
        this.sqlGameDAO = new SQLGameDAO();
    }

    @BeforeEach
    public void clearDB() throws Exception {
        sqlGameDAO.clear();
    }

    @Test
    public void createGameSuccess() throws Exception {
        //Create game
        String gameName = "myGame";
        int gameID = sqlGameDAO.createGame(gameName);
        String gameIDString = Integer.toString(gameID);

        //Data is still there
        String createStatement = "select gameName from gameData where gameName=?";
        //TODO: Also test for whiteUsername and blackUsername
        Assertions.assertEquals(gameName, performQuery(createStatement, gameName));

        //Test that the ChessGame data is stored correctly
        ChessBoard testBoard = new ChessBoard(true);
        ChessGame testGame = new ChessGame(null, testBoard);
        String testStatement = "select gameID from chessGame where gameID=?";
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
        //Create game with invalid authToken

    }

    @Test
    public void updateGameSuccess() throws Exception {
        //Create game

        //Update usernames into whiteUsername and blackUsername

        //Verify game has those usernames
    }

    @Test
    public void updateGameFailure() throws Exception {
        //Create game

        //Update usernames into whiteUsername and blackUsername

        //Update new usernames into whiteUsername and blackUsername

        //Verify this throws an error
    }

    @Test
    public void sqlGameClearSuccess() throws Exception {

    }
}

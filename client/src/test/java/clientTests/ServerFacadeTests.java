package clientTests;

import chess.ChessBoard;
import dataStorage.GameStorage;
import handler.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.GameplayDrawer;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static UIRegisterHandler registerHandler;
    private static UILoginHandler loginHandler;
    private static UILogoutHandler logoutHandler;
    private static UICreateHandler createHandler;
    private static UIListHandler listHandler;
    private static UIJoinHandler joinHandler;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        registerHandler = new UIRegisterHandler();
        loginHandler = new UILoginHandler();
        logoutHandler = new UILogoutHandler();
        createHandler = new UICreateHandler();
        listHandler = new UIListHandler();
        joinHandler = new UIJoinHandler();
    }

    @BeforeEach
    public void clear() {
        registerHandler.clearData();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clearPositive() {
        Assertions.assertTrue(registerHandler.clearData());
    }

    @Test
    public void registerPositive() {
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        Assertions.assertNotNull(registerHandler.register(args));
    }

    @Test
    public void registerNegative() {
        registerPositive();
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        Assertions.assertNull(registerHandler.register(args));
    }

    @Test
    public void logoutPositive() {
        //Use a valid authToken
        String[] blankList = {};
        String authToken = registrationSetup();
        Assertions.assertTrue(logoutHandler.logout(blankList, authToken));
    }

    @Test
    public void logoutNegative() {
        //Use an invalid authToken
        String[] blankList = {};
        String authToken = "fake auth token";
        Assertions.assertFalse(logoutHandler.logout(blankList, authToken));
    }

    @Test
    public void loginPositive() {
        logoutPositive();
        String[] params = {"mySuperRandomUsername","mySuperR4ndomPwd"};
        Assertions.assertNotNull(loginHandler.login(params));
    }

    @Test
    public void loginNegative() {
        logoutPositive();
        String[] params = {"mySuperRandomUsername","badPwd"};
        Assertions.assertNull(loginHandler.login(params));
    }

    @Test
    public void createPositive() {
        String authToken = registrationSetup();
        String[] params = {"newGame"};
        Assertions.assertTrue(createHandler.create(params, authToken));
    }

    @Test
    public void createNegative() {
        String authToken = "phonyAuthToken";
        String[] params = {"newGame"};
        Assertions.assertFalse(createHandler.create(params, authToken));
    }

    @Test
    public void listPositive() {
        String authToken = registrationSetup();
        String[] params = {"newGame2"};
        Assertions.assertTrue(createHandler.create(params, authToken));
        params[0] = "newGame1";
        Assertions.assertTrue(createHandler.create(params, authToken));
        params[0] = "newGame3";
        Assertions.assertTrue(createHandler.create(params, authToken));
        String[] listParams = {};
        Assertions.assertNotNull(listHandler.list(listParams, authToken));
    }

    @Test
    public void listNegative() {
        Assertions.fail();
    }

    @Test
    public void joinPositive() {
        String authToken = registrationSetup();
        String[] gameParams = {"newGame"};
        Assertions.assertTrue(createHandler.create(gameParams, authToken));
        String[] listParams = {};
        ArrayList<GameStorage> games = listHandler.list(listParams, authToken);
        Assertions.assertNotNull(games);
        String[] whiteParams = {"0", "WHITE"};
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameStorage game : games) gameIDs.add(game.getGameID());
        Assertions.assertTrue(joinHandler.join(whiteParams, authToken, gameIDs, false));
        String[] blackParams = {"0", "BLACK"};
        Assertions.assertTrue(joinHandler.join(blackParams, authToken, gameIDs, false));
        Assertions.assertNotNull(listHandler.list(listParams, authToken));
    }

    @Test
    public void joinNegative() {
        Assertions.fail();
    }

    @Test
    public void drawPositive() {
        //Draw a filled board
        ChessBoard board = new ChessBoard(true);
        Assertions.assertDoesNotThrow(() -> GameplayDrawer.draw(board));
    }

    @Test
    public void drawNegative() {
        //Draw a blank board
        ChessBoard board = new ChessBoard();
        Assertions.assertDoesNotThrow(() -> GameplayDrawer.draw(board));
    }

    private String registrationSetup() {
        String username = "mySuperCoolUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        return registerHandler.register(args);
    }
}
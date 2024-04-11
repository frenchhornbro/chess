package clientTests;

import ServerFacade.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import dataStorage.GameStorage;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Client;
import ui.GameplayDrawer;
import uiHandler.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static int port;
    private static UIRegisterHandler registerHandler;
    private static UILoginHandler loginHandler;
    private static UILogoutHandler logoutHandler;
    private static UICreateHandler createHandler;
    private static UIListHandler listHandler;
    private static UIJoinHandler joinHandler;
    private static UIHighlightHandler highlightHandler;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        registerHandler = new UIRegisterHandler();
        loginHandler = new UILoginHandler();
        logoutHandler = new UILogoutHandler();
        createHandler = new UICreateHandler();
        listHandler = new UIListHandler();
        joinHandler = new UIJoinHandler();
        highlightHandler = new UIHighlightHandler();
    }

    @BeforeEach
    public void clear() {
        registerHandler.clearData(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void startPositive() {
        //Entering and exiting works
        ArrayList<ArrayList<String>> testCommands = new ArrayList<>();
        ArrayList<String> testCommand1 = new ArrayList<>(List.of("help"));
        testCommands.add(testCommand1);
        ArrayList<String> testCommand2 = new ArrayList<>(List.of("quit"));
        testCommands.add(testCommand2);
        ServerFacade serverFacade = new ServerFacade(true, testCommands);
        Assertions.assertDoesNotThrow(() -> serverFacade.start(port, new Client(port)));
    }

    @Test
    public void startNegative() {
        //Bad commands aren't accepted
        ArrayList<ArrayList<String>> testCommands = new ArrayList<>();
        ArrayList<String> testCommand1 = new ArrayList<>(List.of("login", "fake", "creds", "exit"));
        testCommands.add(testCommand1);
        ServerFacade serverFacade = new ServerFacade(true, testCommands);
        Assertions.assertThrows(Exception.class, () -> serverFacade.start(port, new Client(port)));
        ArrayList<String> testCommand2 = new ArrayList<>(List.of("help", "unnecessary", "pars", "quit"));
        testCommands.add(testCommand2);
        ServerFacade otherServerFacade = new ServerFacade(true, testCommands);
        Assertions.assertThrows(Exception.class, () -> otherServerFacade.start(port, new Client(port)));
    }

    @Test
    public void clearPositive() {
        Assertions.assertTrue(registerHandler.clearData(port));
    }

    @Test
    public void registerPositive() {
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        ArrayList<String> args = new ArrayList<>(List.of(username, password, email));
        Assertions.assertNotNull(registerHandler.register(port, args));
    }

    @Test
    public void registerNegative() {
        registerPositive();
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        ArrayList<String> args = new ArrayList<>(List.of(username, password, email));
        Assertions.assertNull(registerHandler.register(port, args));
    }

    @Test
    public void logoutPositive() {
        //Use a valid authToken
        String authToken = registrationSetup();
        Assertions.assertTrue(logoutHandler.logout(port, new ArrayList<>(), authToken));
    }

    @Test
    public void logoutNegative() {
        //Use an invalid authToken
        String authToken = "fake auth token";
        Assertions.assertFalse(logoutHandler.logout(port, new ArrayList<>(), authToken));
    }

    @Test
    public void loginPositive() {
        logoutPositive();
        ArrayList<String> params = new ArrayList<>(List.of("mySuperCoolUsername","mySuperR4ndomPwd"));
        Assertions.assertNotNull(loginHandler.login(port, params));
    }

    @Test
    public void loginNegative() {
        logoutPositive();
        ArrayList<String> params = new ArrayList<>(List.of("mySuperCoolUsername","badPwd"));
        Assertions.assertNull(loginHandler.login(port, params));
    }

    @Test
    public void createPositive() {
        String authToken = registrationSetup();
        ArrayList<String> params = new ArrayList<>(List.of("newGame"));
        Assertions.assertTrue(createHandler.create(port, params, authToken));
    }

    @Test
    public void createNegative() {
        String authToken = "phonyAuthToken";
        ArrayList<String> params = new ArrayList<>(List.of("newGame"));
        Assertions.assertFalse(createHandler.create(port, params, authToken));
    }

    @Test
    public void listPositive() {
        // List outputs the contents of the three games
        String authToken = registrationSetup();
        ArrayList<String> params = new ArrayList<>(List.of("newGame1"));
        Assertions.assertTrue(createHandler.create(port, params, authToken));
        params.set(0, "newGame2");
        Assertions.assertTrue(createHandler.create(port, params, authToken));
        params.set(0, "newGame3");
        Assertions.assertTrue(createHandler.create(port, params, authToken));
        Assertions.assertNotNull(listHandler.list(port, new ArrayList<>(), authToken));
    }

    @Test
    public void listNegative() {
        // Bad authToken prevents listing
        Assertions.assertNull(listHandler.list(port, new ArrayList<>(), "phonyAuthToken"));
    }

    @Test
    public void joinPositive() {
        // Standard join
        String authToken = registrationSetup();
        ArrayList<String> gameParams = new ArrayList<>(List.of("newGame"));
        Assertions.assertTrue(createHandler.create(port, gameParams, authToken));
        ArrayList<GameStorage> games = listHandler.list(port, new ArrayList<>(), authToken);
        Assertions.assertNotNull(games);
        ArrayList<String> whiteParams = new ArrayList<>(List.of("0", "WHITE"));
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameStorage game : games) gameIDs.add(game.getGameID());
        Assertions.assertNotEquals("false", joinHandler.join(port, whiteParams, authToken, gameIDs,
                false, "mySuperCoolUsername"));
        ArrayList<String> blackParams = new ArrayList<>(List.of("0", "BLACK"));
        Assertions.assertNotEquals("false", joinHandler.join(port, blackParams, authToken, gameIDs,
                false, "mySuperCoolUsername"));
        Assertions.assertNotNull(listHandler.list(port, new ArrayList<>(), authToken));
    }

    @Test
    public void joinNegative() {
        //Joining a nonexistent gameID
        String authToken = registrationSetup();
        ArrayList<String> gameParams = new ArrayList<>(List.of("newGame"));
        Assertions.assertTrue(createHandler.create(port, gameParams, authToken));
        ArrayList<GameStorage> games = listHandler.list(port, new ArrayList<>(), authToken);
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameStorage game : games) gameIDs.add(game.getGameID());
        ArrayList<String> joinParams = new ArrayList<>(List.of("123", "WHITE"));
        Assertions.assertEquals("false", joinHandler.join(port, joinParams, authToken, gameIDs,
                false, "mySuperCoolUsername"));
    }

    @Test
    public void drawPositive() {
        //Draw a filled board
        ChessBoard board = new ChessBoard(true);
        ChessGame game = new ChessGame(ChessGame.TeamColor.WHITE, board);
        Assertions.assertDoesNotThrow(() -> GameplayDrawer.draw(game, "WHITE", null));
    }

    @Test
    public void drawNegative() {
        //Draw a blank board
        ChessBoard board = new ChessBoard();
        ChessGame game = new ChessGame(ChessGame.TeamColor.WHITE, board);
        Assertions.assertDoesNotThrow(() -> GameplayDrawer.draw(game, "BLACK", null));
    }

    @Test
    public void highlight() {
        ChessBoard board = new ChessBoard(true);
        ChessGame game = new ChessGame(ChessGame.TeamColor.WHITE, board);
        ArrayList<String> coordinates = new ArrayList<>();
        coordinates.add("d2");
        Collection<ChessMove> moves = highlightHandler.highlight(coordinates, board);
        GameplayDrawer.draw(game, "WHITE", moves);
        GameplayDrawer.draw(game, "BLACK", moves);
    }

    private String registrationSetup() {
        String username = "mySuperCoolUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        ArrayList<String> args = new ArrayList<>(List.of(username, password, email));
        return registerHandler.register(port, args);
    }
}
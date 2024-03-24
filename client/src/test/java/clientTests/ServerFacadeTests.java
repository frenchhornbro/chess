package clientTests;

import ServerFacade.ServerFacade;
import chess.ChessBoard;
import dataStorage.GameStorage;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Client;
import ui.GameplayDrawer;
import uiHandler.*;
import java.util.ArrayList;
import java.util.List;


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
        var port = server.run(0);
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
    public void startPositive() {
        //Entering and exiting works
        ArrayList<ArrayList<String>> testCommands = new ArrayList<>();
        ArrayList<String> testCommand1 = new ArrayList<>(List.of("help"));
        testCommands.add(testCommand1);
        ArrayList<String> testCommand2 = new ArrayList<>(List.of("quit"));
        testCommands.add(testCommand2);
        ServerFacade serverFacade = new ServerFacade(true, testCommands);
        Assertions.assertDoesNotThrow(() -> serverFacade.start(new Client()));
    }

    @Test
    public void startNegative() {
        //Bad commands aren't accepted
        ArrayList<ArrayList<String>> testCommands = new ArrayList<>();
        ArrayList<String> testCommand1 = new ArrayList<>(List.of("login", "fake", "creds", "exit"));
        testCommands.add(testCommand1);
        ServerFacade serverFacade = new ServerFacade(true, testCommands);
        Assertions.assertThrows(Exception.class, () -> serverFacade.start(new Client()));
        ArrayList<String> testCommand2 = new ArrayList<>(List.of("help", "unnecessary", "pars", "quit"));
        testCommands.add(testCommand2);
        ServerFacade otherServerFacade = new ServerFacade(true, testCommands);
        Assertions.assertThrows(Exception.class, () -> otherServerFacade.start(new Client()));
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
        ArrayList<String> args = new ArrayList<>(List.of(username, password, email));
        Assertions.assertNotNull(registerHandler.register(args));
    }

    @Test
    public void registerNegative() {
        registerPositive();
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        ArrayList<String> args = new ArrayList<>(List.of(username, password, email));
        Assertions.assertNull(registerHandler.register(args));
    }

    @Test
    public void logoutPositive() {
        //Use a valid authToken
        String authToken = registrationSetup();
        Assertions.assertTrue(logoutHandler.logout(new ArrayList<>(), authToken));
    }

    @Test
    public void logoutNegative() {
        //Use an invalid authToken
        String authToken = "fake auth token";
        Assertions.assertFalse(logoutHandler.logout(new ArrayList<>(), authToken));
    }

    @Test
    public void loginPositive() {
        logoutPositive();
        ArrayList<String> params = new ArrayList<>(List.of("mySuperCoolUsername","mySuperR4ndomPwd"));
        Assertions.assertNotNull(loginHandler.login(params));
    }

    @Test
    public void loginNegative() {
        logoutPositive();
        ArrayList<String> params = new ArrayList<>(List.of("mySuperCoolUsername","badPwd"));
        Assertions.assertNull(loginHandler.login(params));
    }

    @Test
    public void createPositive() {
        String authToken = registrationSetup();
        ArrayList<String> params = new ArrayList<>(List.of("newGame"));
        Assertions.assertTrue(createHandler.create(params, authToken));
    }

    @Test
    public void createNegative() {
        String authToken = "phonyAuthToken";
        ArrayList<String> params = new ArrayList<>(List.of("newGame"));
        Assertions.assertFalse(createHandler.create(params, authToken));
    }

    @Test
    public void listPositive() {
        // List outputs the contents of the three games
        String authToken = registrationSetup();
        ArrayList<String> params = new ArrayList<>(List.of("newGame1"));
        Assertions.assertTrue(createHandler.create(params, authToken));
        params.set(0, "newGame2");
        Assertions.assertTrue(createHandler.create(params, authToken));
        params.set(0, "newGame3");
        Assertions.assertTrue(createHandler.create(params, authToken));
        Assertions.assertNotNull(listHandler.list(new ArrayList<>(), authToken));
    }

    @Test
    public void listNegative() {
        // Bad authToken prevents listing
        Assertions.assertNull(listHandler.list(new ArrayList<>(), "phonyAuthToken"));
    }

    @Test
    public void joinPositive() {
        // Standard join
        String authToken = registrationSetup();
        ArrayList<String> gameParams = new ArrayList<>(List.of("newGame"));
        Assertions.assertTrue(createHandler.create(gameParams, authToken));
        ArrayList<GameStorage> games = listHandler.list(new ArrayList<>(), authToken);
        Assertions.assertNotNull(games);
        ArrayList<String> whiteParams = new ArrayList<>(List.of("0", "WHITE"));
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameStorage game : games) gameIDs.add(game.getGameID());
        Assertions.assertTrue(joinHandler.join(whiteParams, authToken, gameIDs, false));
        ArrayList<String> blackParams = new ArrayList<>(List.of("0", "BLACK"));
        Assertions.assertTrue(joinHandler.join(blackParams, authToken, gameIDs, false));
        Assertions.assertNotNull(listHandler.list(new ArrayList<>(), authToken));
    }

    @Test
    public void joinNegative() {
        //Joining a nonexistent gameID
        String authToken = registrationSetup();
        ArrayList<String> gameParams = new ArrayList<>(List.of("newGame"));
        Assertions.assertTrue(createHandler.create(gameParams, authToken));
        ArrayList<GameStorage> games = listHandler.list(new ArrayList<>(), authToken);
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameStorage game : games) gameIDs.add(game.getGameID());
        ArrayList<String> joinParams = new ArrayList<>(List.of("123", "WHITE"));
        Assertions.assertFalse(joinHandler.join(joinParams, authToken, gameIDs, false));
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
        ArrayList<String> args = new ArrayList<>(List.of(username, password, email));
        return registerHandler.register(args);
    }
}
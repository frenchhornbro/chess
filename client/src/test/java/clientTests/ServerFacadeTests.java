package clientTests;

import handler.*;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static UIRegisterHandler registerHandler;
    private static UILoginHandler loginHandler;
    private static UILogoutHandler logoutHandler;
    private static UICreateHandler createHandler;
    private static UIListHandler listHandler;

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
        Assertions.fail();
    }

    @Test
    public void startNegative() {
        Assertions.fail();
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
        Assertions.assertTrue(listHandler.list(listParams, authToken));
    }

    @Test
    public void listNegative() {

    }

    private String registrationSetup() {
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        return registerHandler.register(args);
    }
}
package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import ui.ServerFacadeHandlers;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ServerFacadeHandlers handlers;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        handlers = new ServerFacadeHandlers();
        //TODO: Instantiate a ServerFacadeHandlers so we can automatically test logout
        // without having to manually run the whole thing each time
    }

    @BeforeEach
    public void clear() {
        //TODO: Create a clear function for testing
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void startPositive() {

    }

    @Test
    public void startNegative() {

    }

    @Test
    public void registerPositive() {
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        Assertions.assertNotNull(handlers.register(args));
    }

    @Test
    public void registerNegative() {
        registerPositive();
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        Assertions.assertNull(handlers.register(args));
    }

    @Test
    public void logoutPositive() {
        //Use a valid authToken
        String[] blankList = {};
        String username = "mySuperRandomUsername";
        String password = "mySuperR4ndomPwd";
        String email = "email@randEmail.tacosNstuff";
        String[] args = {username, password, email};
        String authToken = handlers.register(args);
        Assertions.assertTrue(handlers.logout(blankList, authToken));
    }

    @Test
    public void logoutNegative() {
        //Use an invalid authToken
        String[] blankList = {};
        String authToken = "fake auth token";
        Assertions.assertFalse(handlers.logout(blankList, authToken));
    }

    //TODO: Have a positive and a negative test for each test in ServerFacadeHandlers
}

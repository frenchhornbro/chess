package dataAccessTests;

import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import server.Server;

public class UserDAOTests {
    private static Server server;
    private final SQLUserDAO sqlUserDAO;

    public UserDAOTests() throws Exception {
        this.sqlUserDAO = new SQLUserDAO();
    }

    @BeforeAll
    public static void startServer() {
        server = null;
        server = new Server();
        server.run(12345);
    }

    @Test
    public void sqlAuthCreateSuccess() throws Exception {
        //Create user
        stopServer();
        startServer();
        //Assert the user is still there
    }

    @Test
    public void sqlAuthCreateFail() throws TestException {

    }

    @Test
    public void sqlAuthGetSuccess() throws TestException {

    }

    @Test
    public void sqlAuthGetFail() throws TestException {

    }
    @Test
    public void sqlAuthDeleteSuccess() throws TestException {

    }
    @Test
    public void sqlAuthDeleteFail() throws TestException {

    }
    @Test
    public void sqlAuthClearSuccess() throws TestException {

    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }
}

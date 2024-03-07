package dataAccessTests;

import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import server.Server;

public class AuthDAOTests {
    private static Server server;
    private final SQLAuthDAO sqlAuthDAO;

    public AuthDAOTests() throws Exception {
        this.sqlAuthDAO = new SQLAuthDAO();
    }

    @BeforeAll
    public static void startServer() {
        server = null;
        server = new Server();
        server.run(12345);
    }

    @Test
    public void sqlAuthCreateSuccess() throws Exception {
        sqlAuthDAO.createAuth("newUsername");
        stopServer();
        startServer();
        //Assert the data is still there
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

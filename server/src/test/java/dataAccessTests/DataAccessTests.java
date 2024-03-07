package dataAccessTests;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import server.Server;

public class DataAccessTests {
    private static Server server;
    private final SQLAuthDAO sqlAuthDAO;
    private final SQLUserDAO sqlUserDAO;
    private final SQLGameDAO sqlGameDAO;

    public DataAccessTests() {
        server = new Server();
        this.sqlUserDAO = server.getSqlUserDAO();
        this.sqlAuthDAO = server.getSQLAuthDAO();
        this.sqlGameDAO = server.getSqlGameDAO();
    }

    @BeforeAll
    public static void startServer() {
        server = null;
        server = new Server();
        server.run(12345);
    }

    @Test
    public void memoryAuthCreateSuccess() throws TestException {
        // Add data, assert that it was added
        sqlAuthDAO.createAuth("newUsername");
        stopServer();
        startServer();
        //Assert the data is still there
    }

    @Test
    public void memoryAuthCreateFail() throws TestException {

    }

    @Test
    public void memoryAuthGetSuccess() throws TestException {

    }

    @Test
    public void memoryAuthGetFail() throws TestException {

    }
    @Test
    public void memoryAuthDeleteSuccess() throws TestException {

    }
    @Test
    public void memoryAuthDeleteFail() throws TestException {

    }
    @Test
    public void memoryAuthClearSuccess() throws TestException {

    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }
}

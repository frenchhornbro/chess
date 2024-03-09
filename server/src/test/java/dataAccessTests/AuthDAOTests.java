package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.*;

public class AuthDAOTests extends DAOTests {
    private final SQLAuthDAO sqlAuthDAO;

    public AuthDAOTests() throws Exception {
        this.sqlAuthDAO = new SQLAuthDAO();
    }

    @BeforeEach
    public void clearDB() throws Exception {
        sqlAuthDAO.clear();
    }

    @Test
    public void sqlAuthCreateSuccess() throws Exception {
        //Create authData
        String username = "Robert'); DROP TABLE authData;";
        String authToken = sqlAuthDAO.createAuth(username);
        //Data is still there
        String queryStatement = "select authToken from authData where username = ?";
        Assertions.assertEquals(authToken, performQuery(queryStatement, username), "AuthTokens are different");
    }

    @Test
    public void sqlAuthCreateFail() throws Exception {
        //Create authData
        String username = "newUsername";
        String authToken = sqlAuthDAO.createAuth(username);

        //Data is still there
        String queryStatement = "select `authToken` from authData where username = ?";
        Assertions.assertEquals(authToken, performQuery(queryStatement, username), "AuthTokens are different");

        //Creating a user with the same username throws an error
        Assertions.assertThrows(DataAccessException.class,() -> sqlAuthDAO.createAuth(username));
    }

    @Test
    public void sqlAuthGetSuccess() throws Exception {
        //Create authData
        String username = "myUserOfTheNameVariety";
        String authToken = sqlAuthDAO.createAuth(username);

        //Getting the authData yields the same authToken
        Assertions.assertEquals(sqlAuthDAO.getAuth(authToken), authToken);
    }

    @Test
    public void sqlAuthGetFail() throws Exception {
        //Getting authData for an invalid authToken returns null
        Assertions.assertNull(sqlAuthDAO.getAuth("randStringNStuff"));
    }
    @Test
    public void sqlAuthDeleteSuccess() throws Exception {
        //Create authData
        String username = "taconator5000";
        String authToken = sqlAuthDAO.createAuth(username);

        //Delete authData
        sqlAuthDAO.deleteAuth(authToken);

        //Getting authData returns null
        Assertions.assertNull(sqlAuthDAO.getAuth(authToken));
    }
    @Test
    //TODO: Figure out how one would fail a delete test
    public void sqlAuthDeleteFail() throws Exception {
        //Delete an authToken that doesn't exist
        sqlAuthDAO.deleteAuth("nonexistentAuthToken");
    }
    @Test
    public void sqlAuthClearSuccess() throws Exception {
        //Create authData
        String[] usernames = {"userOne", "userDos", "userTrois"};
        String[] authTokens = {"", "", ""};
        for (int i = 0; i < usernames.length; i++) {
            authTokens[i] = sqlAuthDAO.createAuth(usernames[i]);
        }

        //The authData is there
        for (String auth : authTokens) {
            Assertions.assertNotNull(sqlAuthDAO.getAuth(auth));
        }

        //Clearing removes all authData
        sqlAuthDAO.clear();
        for (String auth : authTokens) {
            Assertions.assertNull(sqlAuthDAO.getAuth(auth));
        }
    }
}

package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.*;

public class UserDAOTests extends DAOTests {
    private final SQLUserDAO sqlUserDAO;

    public UserDAOTests() throws Exception {
        this.sqlUserDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void clearDB() throws Exception {
        sqlUserDAO.clear();
    }

    @Test
    public void sqlUserCreateSuccess() throws Exception {
        //Create user
        String username = "Robert'); DROP TABLE userData;";
        String password = "password";
        String email = "notAHacker@idk.com";
        sqlUserDAO.createUser(username, password, email);

        //Data is still there
        String queryStatement = "select username from userData where username=?";
        Assertions.assertEquals(username, performQuery(queryStatement, username), "usernames are different");
        queryStatement = "select password from userData where username=?";
        Assertions.assertEquals(password, performQuery(queryStatement, username), "passwords are different");
        queryStatement = "select email from userData where username=? ";
        Assertions.assertEquals(email, performQuery(queryStatement, username), "emails are different");
    }

    @Test
    public void sqlUserCreateFail() throws Exception {
        //Create user
        String username = "newUsername";
        String password = "newPassword";
        String email = "randomguynstuff@gmail.gmail";
        sqlUserDAO.createUser(username, password, email);

        //Data is still there
        String queryStatement = "select username from userData where username=?";
        Assertions.assertEquals(username, performQuery(queryStatement, username), "usernames are different");
        queryStatement = "select password from userData where username=?";
        Assertions.assertEquals(password, performQuery(queryStatement, username), "passwords are different");
        queryStatement = "select email from userData where username=? ";
        Assertions.assertEquals(email, performQuery(queryStatement, username), "emails are different");

        //Creating a user with the same username throws an error
        Assertions.assertThrows(DataAccessException.class,() -> sqlUserDAO.createUser(username, password, email));
    }

    @Test
    public void sqlUserGetSuccess() throws Exception {
        //Create user
        String username = "myUserOfTheNameVariety";
        String password = "myPassOfTheWordVariety";
        String email = "myE@OfTheMailVariety";
        sqlUserDAO.createUser(username, password, email);

        //Data is still there
        String queryStatement = "select username from userData where username=?";
        Assertions.assertEquals(username, performQuery(queryStatement, username), "usernames are different");
        queryStatement = "select password from userData where username=?";
        Assertions.assertEquals(password, performQuery(queryStatement, username), "passwords are different");
        queryStatement = "select email from userData where username=? ";
        Assertions.assertEquals(email, performQuery(queryStatement, username), "emails are different");

        //Getting the password returns the user's password
        Assertions.assertEquals(sqlUserDAO.getUser(username), password);
    }

    @Test
    public void sqlUserGetFail() throws Exception {
        //Getting user for an invalid username returns null
        Assertions.assertNull(sqlUserDAO.getUser("randStringNStuff"));
    }

    @Test
    public void sqlUserClearSuccess() throws Exception {
        //Create users
        String[][] credentials = {
                {"userOne", "userDos", "userTrois"},
                {"passwordUno", "passwordDeux", "passwordThree"},
                {"email@un", "email@two", "email@tres"}};
        for (String[] creds : credentials) {
            sqlUserDAO.createUser(creds[0], creds[1], creds[2]);
        }
        //The users are there
        for (String[] creds : credentials) {
            Assertions.assertNotNull(sqlUserDAO.getUser(creds[0]));
        }

        //Clearing removes all users
        sqlUserDAO.clear();
        for (String[] creds : credentials) {
            Assertions.assertNull(sqlUserDAO.getUser(creds[0]));
        }
    }
}

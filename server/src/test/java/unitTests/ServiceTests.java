package unitTests;

/*
Good tests extensively show that we get the expected behavior. This could be asserting that data put into the database is
really there, or that a function throws an error when it should. Write a positive and a negative JUNIT test case for each
 public method on your Service classes, except for Clear which only needs a positive test case. A positive test case is one
  for which the action happens successfully (e.g., successfully claiming a spot in a game). A negative test case is one for
   which the operation fails (e.g., trying to claim an already claimed spot).

The service unit tests must directly call the methods on your service classes. They should not use the HTTP server pass off
 test code that is provided with the starter code.
 */
import dataAccessTests.DAOTests;
import dataStorage.GamesStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;
import dataAccess.*;

public class ServiceTests extends DAOTests {
    private final SQLGameDAO sqlGameDAO;
    private final SQLAuthDAO sqlAuthDAO;
    private final SQLUserDAO sqlUserDAO;
    public RegistrationService regServ;
    private final static String USER = "username";
    private final static String PWD = "password";
    private final static String EMAIL = "email@email.com";
    public ServiceTests() throws Exception {
        this.sqlGameDAO = new SQLGameDAO();
        this.sqlAuthDAO = new SQLAuthDAO();
        this.sqlUserDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void clear() throws Exception {
        ClearService clearService = new ClearService();
        clearService.clear();
        regServ = new RegistrationService();
        sqlUserDAO.clear();
        sqlAuthDAO.clear();
        sqlGameDAO.clear();
    }

    @Test
    public void registerPositive() throws Exception {
        //Standard registration
        String authToken = regServ.register(USER, PWD, EMAIL);
        String query = "SELECT authToken FROM authData WHERE username=?";
        Assertions.assertEquals(authToken, performQuery(query, USER));
        query = "SELECT password FROM userData WHERE username=?";
        Assertions.assertEquals(PWD, performQuery(query, USER));
        query = "SELECT email FROM userData WHERE username=?";
        Assertions.assertEquals(EMAIL, performQuery(query, USER));
    }

    @Test
    public void registerNegative() {
        //Registering with no inputted password
        try {
            regServ.register(USER, null, EMAIL);
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
            Assertions.assertEquals(400, e.getErrorNum());
        }
    }

    @Test
    public void loginPositive() throws Exception {
        //Standard login
        regServ.register(USER, PWD, EMAIL);
        LoginService logServ = new LoginService();
        String newAuthToken = logServ.login(USER, PWD);
        String query = "SELECT username FROM authData WHERE authToken=?";
        Assertions.assertEquals(USER, performQuery(query, newAuthToken));
    }

    @Test
    public void loginNegative() throws Exception {
        //Logging in with incorrect password
        try {
            regServ.register(USER, PWD, EMAIL);
            LoginService logServ = new LoginService();
            logServ.login(USER, "incorrectPassword");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals("Error: password is incorrect", e.getMessage());
            Assertions.assertEquals(401, e.getErrorNum());
        }
    }

    @Test
    public void logoutPositive() throws Exception {
        //Standard logout
        String authToken = regServ.register(USER, PWD, EMAIL);
        LogoutService logoutService = new LogoutService();
        Assertions.assertDoesNotThrow(() -> logoutService.logout(authToken));
    }

    @Test
    public void logoutNegative() throws Exception {
        //Logging out with invalid authToken
        try {
            String authToken = regServ.register(USER, PWD, EMAIL);
            LogoutService logoutService = new LogoutService();
            logoutService.logout(authToken + "incorrectAuth");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
            Assertions.assertEquals(401, e.getErrorNum());
        }
    }

    @Test
    public void createGamePositive() throws Exception {
        //Standard game creation
        String authToken = regServ.register(USER, PWD, EMAIL);
        CreateGameService createGameService = new CreateGameService();
        String gameName = "MyGame";
        int gameID = createGameService.createGame(gameName, authToken);
        String gameIDString = Integer.toString(gameID);
        String query = "SELECT gameName FROM gameData WHERE gameID=?";
        Assertions.assertEquals(gameName, performQuery(query, gameIDString));
        query = "SELECT whiteUsername FROM gameData WHERE gameID=?";
        Assertions.assertNull(performQuery(query, gameIDString));
        query = "SELECT blackUsername FROM gameData WHERE gameID=?";
        Assertions.assertNull(performQuery(query, gameIDString));
    }

    @Test
    public void createGameNegative() throws Exception {
        //Creating a game with invalid authToken
        try {
            String authToken = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService();
            createGameService.createGame("MyGame", authToken + "incorrectAuth");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals(401, e.getErrorNum());
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void listGamesPositive() throws Exception {
        //Standard game listing
        String authToken = regServ.register(USER, PWD, EMAIL);
        CreateGameService createGameService = new CreateGameService();
        int gameID1 = createGameService.createGame("Game1", authToken);
        int gameID2 = createGameService.createGame("Game2", authToken);
        ListGamesService listGamesService = new ListGamesService();

        GamesStorage games = listGamesService.listGames(authToken);
        Assertions.assertEquals("Game1", games.getGames().getFirst().getGameName());
        Assertions.assertEquals(gameID1, games.getGames().getFirst().getGameID());
        Assertions.assertNull(games.getGames().get(0).getWhiteUsername());
        Assertions.assertNull(games.getGames().get(0).getBlackUsername());
        Assertions.assertEquals("Game2", games.getGames().get(1).getGameName());
        Assertions.assertEquals(gameID2, games.getGames().get(1).getGameID());
        Assertions.assertNull(games.getGames().get(1).getWhiteUsername());
        Assertions.assertNull(games.getGames().get(1).getBlackUsername());
    }

    @Test
    public void listGamesNegative() throws Exception {
        //Game listing with invalid authToken
        try {
            String authToken = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService();
            createGameService.createGame("Game1", authToken);
            createGameService.createGame("Game2", authToken);
            ListGamesService listGamesService = new ListGamesService();
            listGamesService.listGames(authToken + "incorrectAuth");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals(401, e.getErrorNum());
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void joinGamePositive() throws Exception {
        //Standard game joining
        String authToken = regServ.register(USER, PWD, EMAIL);
        CreateGameService createGameService = new CreateGameService();
        int gameID = createGameService.createGame("MyGame", authToken);
        String gameIDString = Integer.toString(gameID);
        JoinGameService joinGameService = new JoinGameService();
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(authToken, "WHITE", gameID));
        String query = "SELECT whiteUsername FROM gameData WHERE gameID=?";
        Assertions.assertEquals(USER, performQuery(query, gameIDString));
    }

    @Test
    public void joinGameNegative() throws Exception {
        //Joining game with a playerColor that is already taken
        try {
            String authToken = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService();
            int gameID = createGameService.createGame("MyGame", authToken);
            JoinGameService joinGameService = new JoinGameService();
            Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(authToken, "WHITE", gameID));

            String newAuthToken = regServ.register("otherUser", PWD, EMAIL);
            joinGameService.joinGame(newAuthToken, "WHITE", gameID);
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals(403, e.getErrorNum());
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }
    }

    @Test
    public void clearPositive() throws Exception {
        //Clear
        CreateGameService createGameService = new CreateGameService();
        String authToken = regServ.register(USER, PWD, EMAIL);
        String newAuthToken = regServ.register("newUser", PWD, EMAIL);
        int gameID1 = createGameService.createGame("Game1", authToken);
        int gameID2 = createGameService.createGame("Game2", newAuthToken);

        ClearService newClear = new ClearService();
        newClear.clear();
        Assertions.assertNull(sqlUserDAO.getUser(USER));
        Assertions.assertNull(sqlUserDAO.getUser("newUser"));
        Assertions.assertNull(sqlAuthDAO.getAuth(authToken));
        Assertions.assertNull(sqlAuthDAO.getAuth(newAuthToken));
        String query = "SELECT gameName FROM gameData WHERE gameID=?";
        Assertions.assertEquals("Not found", performQuery(query, Integer.toString(gameID1)));
        Assertions.assertEquals("Not found", performQuery(query, Integer.toString(gameID2)));
    }

}

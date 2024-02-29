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
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import service.*;
import dataAccess.*;

import java.util.HashMap;

public class ServiceTests {
    private final MemoryGameDAO memoryGameDAO;
    private final MemoryAuthDAO memoryAuthDAO;
    private final MemoryUserDAO memoryUserDAO;
    public RegistrationService regServ;
    private final static String USER = "username";
    private final static String PWD = "password";
    private final static String EMAIL = "email@email.com";
    public ServiceTests() {
        this.memoryGameDAO = new MemoryGameDAO();
        this.memoryAuthDAO = new MemoryAuthDAO();
        this.memoryUserDAO = new MemoryUserDAO();
    }

    @BeforeEach
    public void clear() throws TestException {
        ClearService clearService = new ClearService(memoryUserDAO, memoryAuthDAO, memoryGameDAO);
        clearService.clear();
        regServ = new RegistrationService(memoryUserDAO,memoryAuthDAO);
    }

    @Test
    public void registerSuccess() {
        //Standard registration
        try {
            AuthData retAuthData = regServ.register(USER, PWD, EMAIL);
            Assertions.assertEquals(USER, retAuthData.getUsername());
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void registerFail() {
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
    public void loginSuccess() {
        //Standard login
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            LoginService logServ = new LoginService(memoryUserDAO, memoryAuthDAO);
            AuthData newAuth = logServ.login(USER, PWD);
            Assertions.assertEquals(newAuth.getUsername(), auth.getUsername());
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void loginFail() {
        //Logging in with incorrect password
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            LoginService logServ = new LoginService(memoryUserDAO, memoryAuthDAO);
            AuthData newAuth = logServ.login(USER, "incorrectPassword");
            Assertions.assertEquals(newAuth.getUsername(), auth.getUsername());
        }
        catch(ServiceException e) {
            Assertions.assertEquals("Error: password is incorrect", e.getMessage());
            Assertions.assertEquals(401, e.getErrorNum());
        }
    }

    @Test
    public void logoutSuccess() {
        //Standard logout
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            LogoutService logoutService = new LogoutService(memoryAuthDAO);
            logoutService.logout(auth.getAuthToken());
            Assertions.assertTrue(true);
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void logoutFail() {
        //Logging out with invalid authToken
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            LogoutService logoutService = new LogoutService(memoryAuthDAO);
            logoutService.logout(auth.getAuthToken() + "incorrectAuth");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
            Assertions.assertEquals(401, e.getErrorNum());
        }
    }

    @Test
    public void createGameSuccess() {
        //Standard game creation
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            GameData game = createGameService.createGame("MyGame", auth.getAuthToken());
            Assertions.assertEquals("MyGame", game.getGameName());
            Assertions.assertNull(game.getWhiteUsername());
            Assertions.assertNull(game.getBlackUsername());
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void createGameFail() {
        //Creating a game with invalid authToken
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            createGameService.createGame("MyGame", auth.getAuthToken() + "incorrectAuth");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals(401, e.getErrorNum());
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void listGamesSuccess() {
        //Standard game listing
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            GameData game1 = createGameService.createGame("Game1", auth.getAuthToken());
            GameData game2 = createGameService.createGame("Game2", auth.getAuthToken());
            ListGamesService listGamesService = new ListGamesService(memoryAuthDAO, memoryGameDAO);
            HashMap<Integer, GameData> games = listGamesService.listGames(auth.getAuthToken());

            Assertions.assertEquals("Game1", games.get(game1.getGameID()).getGameName());
            Assertions.assertEquals(game1.getGameID(), games.get(game1.getGameID()).getGameID());
            Assertions.assertEquals("Game2", games.get(game2.getGameID()).getGameName());
            Assertions.assertEquals(game2.getGameID(), games.get(game2.getGameID()).getGameID());
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void listGamesFail() {
        //Game listing with invalid authToken
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            createGameService.createGame("Game1", auth.getAuthToken());
            createGameService.createGame("Game2", auth.getAuthToken());
            ListGamesService listGamesService = new ListGamesService(memoryAuthDAO, memoryGameDAO);
            listGamesService.listGames(auth.getAuthToken() + "incorrectAuth");
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals(401, e.getErrorNum());
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void joinGameSuccess() {
        //Standard game joining
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            GameData game = createGameService.createGame("MyGame", auth.getAuthToken());
            JoinGameService joinGameService = new JoinGameService(memoryAuthDAO, memoryGameDAO);
            joinGameService.joinGame(auth.getAuthToken(), "WHITE", game.getGameID());
            Assertions.assertTrue(true);
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void joinGameFail() {
        //Joining game with a playerColor that is already taken
        try {
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            GameData game = createGameService.createGame("MyGame", auth.getAuthToken());
            JoinGameService joinGameService = new JoinGameService(memoryAuthDAO, memoryGameDAO);
            joinGameService.joinGame(auth.getAuthToken(), "WHITE", game.getGameID());

            AuthData newAuth = regServ.register("otherUser", PWD, EMAIL);
            joinGameService.joinGame(newAuth.getAuthToken(), "WHITE", game.getGameID());
            Assertions.fail();
        }
        catch(ServiceException e) {
            Assertions.assertEquals(403, e.getErrorNum());
            Assertions.assertEquals("Error: already taken", e.getMessage());
        }
    }

    @Test
    public void clearSuccess() {
        //Clear
        try {
            CreateGameService createGameService = new CreateGameService(memoryGameDAO, memoryAuthDAO);
            AuthData auth = regServ.register(USER, PWD, EMAIL);
            AuthData newAuth = regServ.register("newUser", PWD, EMAIL);
            GameData game1 = createGameService.createGame("Game1", auth.getAuthToken());
            GameData game2 = createGameService.createGame("Game2", newAuth.getAuthToken());

            ClearService newClear = new ClearService(memoryUserDAO, memoryAuthDAO, memoryGameDAO);
            newClear.clear();
            Assertions.assertNull(memoryUserDAO.getUser(USER));
            Assertions.assertNull(memoryUserDAO.getUser("newUser"));
            Assertions.assertNull(memoryAuthDAO.getAuth(auth.getAuthToken()));
            Assertions.assertNull(memoryAuthDAO.getAuth(newAuth.getAuthToken()));
            Assertions.assertNull(memoryGameDAO.getGame(game1.getGameID()));
            Assertions.assertNull(memoryGameDAO.getGame(game2.getGameID()));
        }
        catch(Exception e) {
            Assertions.fail();
        }
    }

}

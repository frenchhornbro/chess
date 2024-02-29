package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import handler.*;
import spark.*;

public class Server {

    private final RegistrationHandler regHandler;
    private final ClearHandler clearHandler;
    private final LoginHandler loginHandler;
    private final CreateGameHandler createGameHandler;
    private final LogoutHandler logoutHandler;
    private final JoinGameHandler joinGameHandler;
    private final ListGamesHandler listGamesHandler;

    public static void main (String[] args) {
        new Server().run(8080);
    }

    public Server () {
        MemoryUserDAO memUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memGameDAO = new MemoryGameDAO();
        this.regHandler = new RegistrationHandler(memUserDAO, memAuthDAO);
        this.clearHandler = new ClearHandler(memUserDAO, memAuthDAO, memGameDAO);
        this.loginHandler = new LoginHandler(memUserDAO, memAuthDAO);
        this.createGameHandler = new CreateGameHandler(memGameDAO, memAuthDAO);
        this.logoutHandler = new LogoutHandler(memAuthDAO);
        this.joinGameHandler = new JoinGameHandler(memAuthDAO, memGameDAO);
        this.listGamesHandler = new ListGamesHandler(memAuthDAO, memGameDAO);
    }

    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);
            Spark.staticFiles.location("web");
            createRoutes();
            Spark.awaitInitialization();
        }
        catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
        return Spark.port();
    }

    private void createRoutes() {
        Spark.post("/user", this.regHandler::register);
        Spark.post("/session", this.loginHandler::login);
        Spark.delete("/session", this.logoutHandler::logout);
        Spark.get("/game", this.listGamesHandler::listGames);
        Spark.post("/game",this.createGameHandler::createGame);
        Spark.put("/game", this.joinGameHandler::joinGame);
        Spark.delete("/db", this.clearHandler::clearApplication);

        //To input parameters in the URL:
        //http://localhost:8080?p1=v1&p2=v2
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

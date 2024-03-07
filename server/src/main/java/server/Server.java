package server;

import dataAccess.*;
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
    private final SQLAuthDAO sqlAuthDAO;
    private final SQLGameDAO sqlGameDAO;
    private final SQLUserDAO sqlUserDAO;

    public static void main (String[] args) {
        new Server().run(8080);
    }

    public Server () {
        this.sqlUserDAO = new SQLUserDAO();
        this.sqlAuthDAO = new SQLAuthDAO();
        this.sqlGameDAO = new SQLGameDAO();
        this.regHandler = new RegistrationHandler(sqlUserDAO, sqlAuthDAO);
        this.clearHandler = new ClearHandler(sqlUserDAO, sqlAuthDAO, sqlGameDAO);
        this.loginHandler = new LoginHandler(sqlUserDAO, sqlAuthDAO);
        this.createGameHandler = new CreateGameHandler(sqlGameDAO, sqlAuthDAO);
        this.logoutHandler = new LogoutHandler(sqlAuthDAO);
        this.joinGameHandler = new JoinGameHandler(sqlAuthDAO, sqlGameDAO);
        this.listGamesHandler = new ListGamesHandler(sqlAuthDAO, sqlGameDAO);
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
        //This specifies what method is called based on the HTTP method and path
        Spark.post("/user", this.regHandler::register);
        Spark.post("/session", this.loginHandler::login);
        Spark.delete("/session", this.logoutHandler::logout);
        Spark.get("/game", this.listGamesHandler::listGames);
        Spark.post("/game",this.createGameHandler::createGame);
        Spark.put("/game", this.joinGameHandler::joinGame);
        Spark.delete("/db", this.clearHandler::clearApplication);
    }

    public SQLUserDAO getSqlUserDAO() {
        return this.sqlUserDAO;
    }

    public SQLAuthDAO getSQLAuthDAO() {
        return this.sqlAuthDAO;
    }

    public SQLGameDAO getSqlGameDAO() {
        return this.sqlGameDAO;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

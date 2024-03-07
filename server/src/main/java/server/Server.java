package server;

import dataAccess.*;
import handler.*;
import spark.*;

public class Server {

    private RegistrationHandler regHandler;
    private ClearHandler clearHandler;
    private LoginHandler loginHandler;
    private CreateGameHandler createGameHandler;
    private LogoutHandler logoutHandler;
    private JoinGameHandler joinGameHandler;
    private ListGamesHandler listGamesHandler;
    private SQLAuthDAO sqlAuthDAO;
    private SQLGameDAO sqlGameDAO;
    private SQLUserDAO sqlUserDAO;

    public static void main (String[] args) {
        new Server().run(8080);
    }

    public Server () {
        //TODO: Eventually get rid of these. The services can make their own DAOs.
        try {
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
        catch (Throwable ex) {
            System.out.println("Error: Can't start server\n" + ex.getMessage());
        }
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

package server;

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

    public static void main (String[] args) {
        new Server().run(8080);
    }

    public Server () {
        try {
            this.regHandler = new RegistrationHandler();
            this.clearHandler = new ClearHandler();
            this.loginHandler = new LoginHandler();
            this.createGameHandler = new CreateGameHandler();
            this.logoutHandler = new LogoutHandler();
            this.joinGameHandler = new JoinGameHandler();
            this.listGamesHandler = new ListGamesHandler();
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import handler.ClearHandler;
import handler.RegistrationHandler;
import spark.*;

public class Server {

    private final RegistrationHandler regHandler;
    private final ClearHandler clearHandler;

    public static void main (String[] args) {
        new Server().run(8080);
    }

    public Server () {
        MemoryUserDAO memUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();
        this.regHandler = new RegistrationHandler(memUserDAO, memAuthDAO);
        this.clearHandler = new ClearHandler(memUserDAO, memAuthDAO);
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
        Spark.delete("/db", this.clearHandler::clearApplication);
        Spark.get("/test", (req, res) -> "Test worked");

        //To input parameters in the URL:
        //http://localhost:8080?p1=v1&p2=v2
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

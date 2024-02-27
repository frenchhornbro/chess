package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import service.RegistrationService;
import spark.*;

import java.util.Collection;

public class Server {

    private final RegistrationService regService;

    public static void main (String[] args) {
        new Server().run(8080);
    }

    public Server () {
        MemoryUserDAO memUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memAuthDAO = new MemoryAuthDAO();
        this.regService = new RegistrationService(memUserDAO, memAuthDAO);
    }

    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);
            createRoutes();
            Spark.staticFiles.location("web");
            Spark.awaitInitialization();
            //TODO: Register your endpoints and handle exceptions here.
        }
        catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
        return Spark.port();
    }

    private void createRoutes() {
        Spark.get("/user", (this::register)); //TODO: Maybe change this to be post
        Spark.get("/test", (req, res) -> "Hello");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

/*
    private void exceptionHandler(Exception exception, Request req, Response res) {

    }
*/

    //Request objects are decoded from JSON, Response objects are encoded into JSON

    public Object register(Request req, Response res) throws Exception {
        //[POST] /user {username, password, email}
        try {
            //TODO: Get req from JSON and use this instead of u, p, e
            String authToken = regService.register("u", "p", "e");
            //TODO: Figure out how to serialize authToken into a JSON file to be stored in Response
        }
        catch (Exception exception) {
            //TODO: Based on what is specified in the message (or other data), return the appropriate response
        }
        throw new RuntimeException("Not implemented yet");
    }

    public Object login() {
        throw new RuntimeException("Not implemented yet");
        //return authToken
    }

    public Object logout() {
        throw new RuntimeException("Not implemented yet");
    }

    public Object listGames() {
        throw new RuntimeException("Not implemented yet");
        //return an arraylist or something of games
    }

    public Object createGame() {
        throw new RuntimeException("Not implemented yet");
        //return GameID
    }

    public Object joinGame() {
        throw new RuntimeException("Not implemented yet");
    }

    public Object clear() {
        throw new RuntimeException("Not implemented yet");
    }
}

package handler;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import service.JoinGameService;
import service.ServiceException;
import spark.Request;
import spark.Response;

import java.util.Map;

public class JoinGameHandler {
    private final JoinGameService joinGameService;

    public JoinGameHandler(MemoryAuthDAO memAuthDAO, MemoryGameDAO memGameDAO) {
        this.joinGameService = new JoinGameService(memAuthDAO, memGameDAO);
    }

    public Object joinGame(Request request, Response response) {
        Gson serial = new Gson();
        try {
            Map<String, Object> input = serial.fromJson(request.body(), Map.class);
            String authToken = request.headers("authorization");
            String playerColor = input.get("playerColor").toString();
            double gameIDDouble = (double) input.get("gameID");
            int gameID = (int) Math.floor(gameIDDouble);
            this.joinGameService.joinGame(authToken, playerColor, gameID);
            response.status(200);

            response.body("{}");
        }
        catch(ServiceException exception) {
            ErrorCarrier responder = new ErrorCarrier(exception.getMessage(), exception.getErrorNum());
            response.status(responder.getErrorNum());
            response.body(serial.toJson(responder));
        }
        catch(Exception otherException) {
            response.status(500);
            ErrorCarrier responder = new ErrorCarrier(otherException.getMessage(), 500);
            response.body(serial.toJson(responder));
        }
        return response.body();
        /*
        TODO: Populate the following errors:
         400 - Error: bad request
         401 - Error: unauthorized
         403 - Error: already taken
         500 - Error: description
         */
    }
}
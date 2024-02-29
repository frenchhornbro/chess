package handler;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import service.CreateGameService;
import service.ServiceException;
import spark.Request;
import spark.Response;


import java.util.Map;
import java.util.Set;

public class CreateGameHandler {
    private final CreateGameService createGameService;

    public CreateGameHandler(MemoryGameDAO memGameDao, MemoryAuthDAO memAuthDao) {
        this.createGameService = new CreateGameService(memGameDao, memAuthDao);
    }

    public Object createGame(Request request, Response response) {
        Gson serial = new Gson();
        try {
            Map<String, String> body = serial.fromJson(request.body(), Map.class);
            String gameName = body.get("gameName");
            String authToken = request.headers("authorization");
            GameData gameID = this.createGameService.createGame(gameName, authToken);
            //TODO: extract gameID from GameData
            response.status(200);
            response.body(serial.toJson(gameID));//TODO: Create a class for gameID maybe
        }
        catch (ServiceException exception) {
            ErrorCarrier responder = new ErrorCarrier(exception.getMessage(), exception.getErrorNum());
            response.status(responder.getErrorNum());
            response.body(serial.toJson(responder));
        }
        catch (Exception otherException) {
            response.status(500);
            ErrorCarrier responder = new ErrorCarrier(otherException.getMessage(), 500);
            response.body(serial.toJson(responder));
        }
        return response.body();
    }
}

package handler;

import com.google.gson.Gson;
import service.CreateGameService;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class CreateGameHandler extends Handler {
    public Object createGame(Request request, Response response) {
        //Create a new game and return the gameID
        Gson serial = new Gson();
        try {
            Map<String, String> body = serial.fromJson(request.body(), Map.class);
            String gameName = body.get("gameName");
            String authToken = request.headers("authorization");
            CreateGameService createGameService = new CreateGameService();
            int gameID = createGameService.createGame(gameName, authToken);
            GameID gameIDStorage = new GameID(gameID);

            response.status(200);
            response.body(serial.toJson(gameIDStorage));
        }
        catch (ServiceException exception) {
            response = handleServiceEx(exception, response, serial);
        }
        catch (Exception otherException) {
            response.status(500);
            ErrorCarrier responder = new ErrorCarrier(otherException.getMessage(), 500);
            response.body(serial.toJson(responder));
        }
        return response.body();
    }

    private record GameID(int gameID) {}
}

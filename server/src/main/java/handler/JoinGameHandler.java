package handler;

import com.google.gson.Gson;
import service.JoinGameService;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class JoinGameHandler {
    public Object joinGame(Request request, Response response) {
        //Add the user as the specified player color (or an observer)
        Gson serial = new Gson();
        try {
            Map<String, Object> input = serial.fromJson(request.body(), Map.class);
            String authToken = request.headers("authorization");
            String playerColor = "";
            if (input.get("playerColor") != null) playerColor = input.get("playerColor").toString();
            double gameIDDouble = (double) input.get("gameID");
            int gameID = (int) Math.floor(gameIDDouble);
            JoinGameService joinGameService = new JoinGameService();
            joinGameService.joinGame(authToken, playerColor, gameID);

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
    }
}

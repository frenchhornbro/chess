package handler;

import com.google.gson.Gson;
import service.JoinGameService;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class JoinGameHandler extends Handler {
    public Object joinGame(Request request, Response response) {
        //Add the user as the specified player color (or an observer)
        Gson serial = new Gson();
        try {
            Map<String, Object> input = serial.fromJson(request.body(), Map.class);
            String authToken = request.headers("authorization");
            String playerColor = "";
            if (input.get("playerColor") != null) playerColor = input.get("playerColor").toString();
            int gameID = (int) Math.round((double) input.get("gameID"));

            JoinGameService joinGameService = new JoinGameService();
            joinGameService.joinGame(authToken, playerColor, gameID);

            response.status(200);
            response.body("{}");
        }
        catch(ServiceException exception) {
            response = handleServiceEx(exception, response, serial);
        }
        catch(Exception otherException) {
            response.status(500);
            ErrorCarrier responder = new ErrorCarrier(otherException.getMessage(), 500);
            response.body(serial.toJson(responder));
        }
        return response.body();
    }
}

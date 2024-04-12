package handler;

import com.google.gson.Gson;
import dataStorage.GamesStorage;
import service.ListGamesService;
import service.ServiceException;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {
    public Object listGames(Request request, Response response) {
        //Return a GamesStorage object
        Gson serial = new Gson();
        try {
            String authToken = request.headers("authorization");
            ListGamesService listGamesService = new ListGamesService();
            GamesStorage games = listGamesService.listGames(authToken);

            response.status(200);
            response.body(serial.toJson(games));
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

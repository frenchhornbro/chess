package handler;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import service.ListGamesService;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.HashMap;

public class ListGamesHandler {
    private final ListGamesService listGamesService;

    public ListGamesHandler(MemoryAuthDAO memAuthDAO, MemoryGameDAO memGameDAO) {
        this.listGamesService = new ListGamesService(memAuthDAO, memGameDAO);
    }

    public Object listGames(Request request, Response response) {
        Gson serial = new Gson();
        try {
            String authToken = request.headers("authorization");
            HashMap<Integer, GameData> games = this.listGamesService.listGames(authToken);
            GamesStorage gameStorage = new GamesStorage(games);
            response.status(200);
            response.body(serial.toJson(gameStorage));
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
        System.out.println(response.body());
        return response.body();
    }
}

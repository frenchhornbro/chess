package handler;

import chess.ChessBoard;
import com.google.gson.Gson;
import service.GetBoardService;
import spark.Request;
import spark.Response;
import java.util.Map;

public class GetBoardHandler {

    /** Get the chessboard for the given gameID */
    public Object getBoard(Request request, Response response) {
        Gson serial = new Gson();
        try {
            String authToken = request.headers("authorization");
            Map<String, String> input = serial.fromJson(request.body(), Map.class);
            int gameID = Integer.parseInt(input.get("gameID"));

            GetBoardService getBoardService = new GetBoardService();
            ChessBoard board = getBoardService.getBoard(authToken, gameID);

            response.status(200);
            response.body(serial.toJson(board));
        }
        catch(Exception exception) {
            response.status(500);
            response.body(new Gson().toJson(exception.getMessage()));
        }
        return response.body();
    }
}
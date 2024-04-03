package uiHandler;

import chess.ChessBoard;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.GameplayDrawer;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UIDrawBoardHandler extends UIHandler {
    public ChessBoard drawBoard(int port, String authToken, String gameID, String playerColor, Collection<ChessMove> moves) {
        if (authToken == null) {
            System.out.println("No auth token");
            return null;
        }
        try {
            //Parameters
            ArrayList<String> titles = new ArrayList<>(List.of("gameID"));
            ArrayList<String> params = new ArrayList<>(List.of(gameID));

            //Prepare request
            HttpURLConnection connection = prepareRequest("/board", port, "POST",
                    "authorization", authToken, titles, params);

            //Process request
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream responseBody = connection.getInputStream()) {
                    InputStreamReader reader = new InputStreamReader(responseBody);
                    ChessBoard board = new Gson().fromJson(reader, ChessBoard.class);
                    GameplayDrawer.draw(board, playerColor, moves);
                    return board;
                }
            }
            else {
                try (InputStream responseBody = connection.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(responseBody);
                    Map<String, String> response = new Gson().fromJson(reader, Map.class);
                    for (String error : response.values()) {
                        System.out.println("Error = " + error);
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception caught: " + ex.getMessage());
        }
        return null;
    }
}

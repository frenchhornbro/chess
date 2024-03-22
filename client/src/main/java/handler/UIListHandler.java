package handler;

import com.google.gson.Gson;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.*;

public class UIListHandler extends UIHandler {

    public boolean list(String[] params, String authToken) {
        //TODO: Need to print out every game and it's gameData
        // Assign a tempID to each game for the Client
        // Remember the tempID to be accessed by join and observe
        if (params.length != 0) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printCreate();
            return false;
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return false;
        }
        try {
            String[] blankList = {};
            HttpURLConnection connection = prepareRequest("/game", "GET",
                    "authorization", authToken, blankList, blankList);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream responseBody = connection.getInputStream()) {
                    InputStreamReader reader = new InputStreamReader(responseBody);
                    var response = new Gson().fromJson(reader, HashMap.class);
                    String gamesString = response.get("games").toString();
                    ArrayList<String> games = parseGames(gamesString);
                    return true;
                    //TODO: At some point change the to_String of the chessBoard to be the actual chessboard? Maybe not in this class though.
                }
            }
            else {
                try (InputStream responseBody = connection.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(responseBody);
                    Map<String, ArrayList<String>> response = new Gson().fromJson(reader, Map.class);
                    for (ArrayList<String> error : response.values()) {
                        System.out.println("Error = " + error);
                    }
                    return false;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception caught: " + ex.getMessage());
            return false;
        }
    }

    private ArrayList<String> parseGames(String gamesString) {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < gamesString.length(); i++) {
            System.out.print(gamesString.charAt(i));
            //TODO: ^^^ Parse games out of this (like Assets in Snipe-IT)
        }
        return arr;
    }
}

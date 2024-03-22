package handler;

import com.google.gson.Gson;
import dataStorage.GameStorage;
import dataStorage.GamesStorage;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.*;

public class UIListHandler extends UIHandler {

    public ArrayList<GameStorage> list(String[] params, String authToken) {
        if (params.length != 0) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printList();
            return null;
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return null;
        }
        try {
            String[] blankList = {};
            HttpURLConnection connection = prepareRequest("/game", "GET",
                    "authorization", authToken, blankList, blankList);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream responseBody = connection.getInputStream()) {
                    InputStreamReader reader = new InputStreamReader(responseBody);
                    GamesStorage games = new Gson().fromJson(reader, GamesStorage.class);
                    return games.getGames();
                }
            }
            else {
                try (InputStream responseBody = connection.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(responseBody);
                    Map<String, ArrayList<String>> response = new Gson().fromJson(reader, Map.class);
                    for (ArrayList<String> error : response.values()) {
                        System.out.println("Error = " + error);
                    }
                    return null;
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception caught: " + ex.getMessage());
            return null;
        }
    }
}

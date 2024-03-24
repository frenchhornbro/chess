package handler;

import com.google.gson.Gson;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UICreateHandler extends UIHandler {

    /** Create and process an HTTP request to create a new game. Returns false if an error occurs */
    public boolean create(ArrayList<String> params, String authToken) {
        if (params.isEmpty()) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printCreate();
            return false;
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return false;
        }
        try {
            //Parameters
            ArrayList<String> titles = new ArrayList<>(List.of("gameName"));
            StringBuilder gameNamePrepper = new StringBuilder();
            for (int i = 0; i < params.size(); i++) {
                gameNamePrepper.append(params.get(i));
                if (i != params.size() - 1) gameNamePrepper.append(" ");
            }
            ArrayList<String> gameName = new ArrayList<>(List.of(gameNamePrepper.toString()));

            //Prepare request
            HttpURLConnection http = prepareRequest("/game", "POST",
                    "authorization", authToken, titles, gameName);

            //Process request
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                Map<String, String> responseBody = new Gson().fromJson(reader, Map.class);
                String errorNum = responseBody.get("errorNum");
                if (errorNum != null ) {
                    System.out.print("Error" + errorNum + ": ");
                    String message = responseBody.get("message");
                    if (message != null) System.out.println(message);
                    else System.out.println("error body not found");
                    return false;
                }
                return true;
            }
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return false;
        }
    }
}

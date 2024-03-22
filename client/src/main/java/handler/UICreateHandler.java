package handler;

import com.google.gson.Gson;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class UICreateHandler extends UIHandler {

    /** Create and process an HTTP request to create a new game. Returns false if an error occurs */
    public boolean create(String[] params, String authToken) {
        if (params.length < 1) {
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
            String[] titles = {"gameName"};
            StringBuilder gameNamePrepper = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                gameNamePrepper.append(params[i]);
                if (i != params.length - 1) gameNamePrepper.append(" ");
            }
            String[] gameName = {gameNamePrepper.toString()};

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

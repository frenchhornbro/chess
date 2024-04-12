package uiHandler;

import ui.PrintHelper;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UICreateHandler extends UIHandler {

    /** Create and process an HTTP request to create a new game. Returns false if an error occurs */
    public boolean create(int port, ArrayList<String> params, String authToken) {
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
            HttpURLConnection http = prepareRequest("/game", port, "POST",
                    "authorization", authToken, titles, gameName);

            //Process request
            return processRequestWithError(http);
        }
        catch (Exception exception) {
            printError(exception.getMessage());
            return false;
        }
    }
}

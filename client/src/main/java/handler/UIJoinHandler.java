package handler;

import ui.PrintHelper;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class UIJoinHandler extends UIHandler {

    /** Add a player to a game. Return false if any errors occur.  */
    public boolean join(String[] params, String authToken) {
        //params should be gameID and clientColor
        if (params.length != 2) {
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
            String[] titles = {"gameID", "playerColor"};

            //Prepare request
            HttpURLConnection http = prepareRequest("/game", "PUT",
                    "authorization", authToken, titles, params);

            //Process request
            try (InputStream response = http.getInputStream()) {
                return true;
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            printError(ex.getMessage());
            return false;
        }
    }
}

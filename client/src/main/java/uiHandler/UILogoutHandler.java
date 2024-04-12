package uiHandler;

import ui.PrintHelper;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class UILogoutHandler extends UIHandler {

    /** Create and process an HTTP request to log out a user */
    public boolean logout(int port, ArrayList<String> params, String authToken) {
        if (!params.isEmpty()) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printLogout();
            return false;
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return false;
        }
        try {
            //Prepare request
            ArrayList<String> blankBody = new ArrayList<>();
            HttpURLConnection http = prepareRequest("/session", port, "DELETE", "authorization",
                                                    authToken, blankBody, blankBody);

            //Process request
            return processRequestWithError(http);
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return false;
        }
    }
}

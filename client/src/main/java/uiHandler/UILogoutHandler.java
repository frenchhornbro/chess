package uiHandler;

import com.google.gson.Gson;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

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

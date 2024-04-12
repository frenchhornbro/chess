package uiHandler;

import ui.PrintHelper;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UILoginHandler extends UIHandler {

    /** Create and process an HTTP request to log in a user */
    public String login(int port, ArrayList<String> params) {
        if (params.size() != 2) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printLogin();
            return null;
        }
        try {
            //Parameters
            ArrayList<String> titles = new ArrayList<>(List.of("username", "password"));

            //Prepare request
            HttpURLConnection http = prepareRequest("/session", port, "POST", null,
                                        null, titles, params);

            //Process request
            return processRequest(http);
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return null;
        }
    }
}
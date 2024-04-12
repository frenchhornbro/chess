package uiHandler;

import ui.PrintHelper;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UIRegisterHandler extends UIHandler {

    /** Create and process an HTTP request to register a new user */
    public String register(int port, ArrayList<String> params) {
        if (params.size() != 3) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printRegister();
            return null;
        }
        try {
            //Parameters
            ArrayList<String> titles = new ArrayList<>(List.of("username", "password", "email"));

            //Prepare request
            HttpURLConnection http = prepareRequest("/user", port, "POST", null,
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
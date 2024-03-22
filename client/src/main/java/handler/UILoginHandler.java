package handler;

import com.google.gson.Gson;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class UILoginHandler extends UIHandler {

    /** Create and process an HTTP request to log in a user */
    public String login(String[] params) {
        if (params.length != 2) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printLogin();
            return null;
        }
        try {
            //Parameters
            String[] titles = {"username", "password"};

            //Prepare request
            HttpURLConnection http = prepareRequest("/session", "POST", null,
                                        null, titles, params);

            //Process request
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                Map<String, String> responseBody = new Gson().fromJson(reader, Map.class);
                return responseBody.get("authToken");
            }
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return null;
        }
    }
}

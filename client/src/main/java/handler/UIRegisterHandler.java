package handler;

import com.google.gson.Gson;
import ui.PrintHelper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

public class UIRegisterHandler extends UIHandler {

    /** Create and process an HTTP request to register a new user */
    public String register(String[] params) {
        if (params.length != 3) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printRegister();
            return null;
        }
        try {
            //Parameters
            String[] titles = {"username", "password", "email"};

            //Prepare request
            HttpURLConnection http = prepareRequest("/user", "POST", null,
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

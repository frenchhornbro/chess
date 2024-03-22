package ui;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerFacadeHandlers {
    public String register(String[] params) {
        if (params.length != 3) {
            System.out.println("Incorrect number of parameters");
            ServerFacade.printRegister();
            return null;
        }
        try {
            //Parameters
            String[] titles = {"username", "password", "email"};

            //Prepare request
            HttpURLConnection http = prepareRequest("/user", "POST",
                    true, true, null, null, titles, params);

            //Process request
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                Map <String, String> responseBody = new Gson().fromJson(reader, Map.class);
                return responseBody.get("authToken");
            }
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return null;
        }
    }

    public String login(String[] params) {
        if (params.length != 2) {
            System.out.println("Incorrect number of parameters");
            ServerFacade.printLogin();
            return null;
        }
        try {
            //Parameters
            String[] titles = {"username", "password"};

            //Prepare request
            HttpURLConnection http = prepareRequest("/session", "POST",
                    true, true, null, null, titles, params);

            //Process request
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                Map <String, String> responseBody = new Gson().fromJson(reader, Map.class);
                return responseBody.get("authToken");
            }
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return null;
        }
    }

    public boolean logout(String[] params, String authToken) {
        if (params.length > 0) {
            System.out.println("Incorrect number of parameters");
            ServerFacade.printLogout();
            return false;
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return false;
        }
        try {
            //Prepare request
            String[] blankBody = {};
            HttpURLConnection http = prepareRequest("/session", "DELETE", true, true,
                    "authorization", authToken, blankBody, blankBody);

            //TODO: I think we have to check the response body to see if it contains any errors. Maybe we can parse it from JSON?
            //Process request
            try (InputStream response = http.getInputStream()) { //TODO: Make sure this actually deletes authData
                InputStreamReader reader = new InputStreamReader(response);
                Map <String, String> responseBody = new Gson().fromJson(reader, Map.class);
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

    public void clearScreen() {
        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    public HttpURLConnection prepareRequest(String slug, String requestMethod, boolean doInput, boolean doOutput,
                                            String headerName, String headerValue, String[] fields, String[] params)
                                            throws Exception {
        if (fields.length != params.length) throw new Exception("Error: Fields and params were of different length");
        URI uri = new URI("http://localhost:8080" + slug);
        URL url = uri.toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setConnectTimeout(5000);
        http.setRequestMethod(requestMethod);
        http.setDoInput(doInput);
        http.setDoOutput(doOutput);

        //Header
        if (headerName != null) http.addRequestProperty(headerName, headerValue);

        //Body
        HashMap<String, String> bodyVars = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            bodyVars.put(fields[i], params[i]);
        }
        try (OutputStream outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(bodyVars);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();
        return http;
    }

    public void printError(String errorMsg) {
        //TODO: Do we want to be printing the errorCodes to the users?
        String printStr = "Error";
        String testStr = "Server returned HTTP response code: ";
        String[] errorCodes = {"400", "401", "403", "500"};
        int errorNum = 200;
        for (String errorCode : errorCodes) {
            if (errorMsg.contains(testStr + errorCode)) {
                errorNum = Integer.parseInt(errorCode);
                break;
            }
        }

        switch (errorNum) {
            case (400):
                printStr += " 400: bad request";
                break;
            case (401):
                printStr += " 401: unauthorized";
                break;
            case (403):
                printStr += " 403: already taken";
                break;
            case (500):
                printStr += " 500: system error";
                break;
            default:
                printStr += ": unknown error";
        }
        System.out.println(printStr);
    }
}

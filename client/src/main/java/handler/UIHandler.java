package handler;

import com.google.gson.Gson;
import ui.EscapeSequences;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class UIHandler {
    public void clearScreen() {
        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    public boolean clearData() {
        try {
            HttpURLConnection http = prepareRequest("/db", "DELETE", null, null,
                                                null, null);
            try (InputStream response = http.getInputStream()) {
                return true;
            }
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return false;
        }
    }

    protected HttpURLConnection prepareRequest(String slug, String requestMethod, String headerName, String headerValue,
                                            String[] fields, String[] params) throws Exception {
        URI uri = new URI("http://localhost:8080" + slug);
        URL url = uri.toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setConnectTimeout(5000);
        http.setRequestMethod(requestMethod);
        http.setDoInput(true);
        http.setDoOutput(true);

        //Header
        if (headerName != null) http.addRequestProperty(headerName, headerValue);

        //Body
        if (fields != null && params != null && fields.length != 0 && params.length != 0) {
            if (fields.length != params.length) throw new Exception("Error: Fields and params were of different length");
            HashMap<String, String> bodyVars = new HashMap<>();
            for (int i = 0; i < fields.length; i++) {
                bodyVars.put(fields[i], params[i]);
            }
            try (OutputStream outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(bodyVars);
                outputStream.write(jsonBody.getBytes());
            }
        }

        http.connect();
        return http;
    }

    protected void printError(String errorMsg) {
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

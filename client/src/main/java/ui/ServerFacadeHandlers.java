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
    public boolean register(String[] params) {
        if (params.length != 3) {
            System.out.println("Incorrect number of parameters");
            ServerFacade.printRegister();
            return false;
        }
        try {
            String[] titles = {"username", "password", "email"};

            HttpURLConnection http = getHttpURLConnection("/user", "POST",
                    true, true, titles, params);
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                System.out.println(new Gson().fromJson(reader, Map.class));
            }
            return true;
            //I believe I have to make a Client class and set a public variable authToken to the returned authToken
        }
        catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
            return false;
            //TODO: This is just printing the error code, not the message...
        }
    }

    public boolean login(String[] params) {
        if (params.length != 2) {
            System.out.println("Incorrect number of parameters");
            ServerFacade.printLogin();
            return false;
        }
        try {
            String[] titles = {"username", "password"};

            HttpURLConnection http = getHttpURLConnection("/session", "POST",
                    true, true, titles, params);
            try (InputStream response = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(response);
                System.out.println(new Gson().fromJson(reader, Map.class));
            }
            return true;
            //TODO: I believe I have to make a Client class and set a public variable authToken to the returned authToken
            // Maybe open up the server in the Client class
        }
        catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
            //TODO: This is just printing the error code, not the message...
            return false;
        }
    }

    public HttpURLConnection getHttpURLConnection(String slug, String requestMethod, boolean doInput, boolean doOutput,
                                                   String[] fields, String[] params) throws Exception {
        if (fields.length != params.length) throw new Exception("Error: Fields and params were of different length");
        URI uri = new URI("http://localhost:8080" + slug);
        URL url = uri.toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setConnectTimeout(5000);
        http.setRequestMethod(requestMethod);
        http.setDoInput(doInput);
        http.setDoOutput(doOutput);

        //Header
//        http.addRequestProperty("Content-Type", "application/json");

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
}

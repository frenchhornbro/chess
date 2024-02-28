package handler;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import service.RegistrationService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Map;

public class RegistrationHandler {
    private final RegistrationService regService;

    public RegistrationHandler(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao) {
        this.regService = new RegistrationService(memUserDao, memAuthDao);
    }

    public Object register(Request req, Response res) throws Exception {
        //[POST] /user {username, password, email}
        try {
            Map<String,String> credentials = new Gson().fromJson(req.body(), Map.class);
            String authToken = regService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));
            System.out.println("Username: " + credentials.get("username"));
            System.out.println("Password: " + credentials.get("password"));
            System.out.println("Email: " + credentials.get("email"));

            //TODO: Create the response body, including the authToken in it
            res.body(new Gson().toJson(authToken));
            res.status(200);
            return res.body();
        }
        catch (Exception exception) {
            return new Gson().toJson(exception.getMessage());
        }
    }
}
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

    //[POST] /user {username, password, email}
    public Object register(Request req, Response res) throws Exception {
        try {
            Map<String,String> credentials = new Gson().fromJson(req.body(), Map.class);
            String authToken = regService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));
            res.body(new Gson().toJson(new retObj(credentials.get("username"),authToken)));
            res.status(200);
            return res.body();
        }
        catch (Exception exception) {
            return new Gson().toJson(exception.getMessage());
        }
    }

    private static class retObj {
        private final String username;
        private final String authToken;

        private retObj (String username, String authToken) {
            this.username = username;
            this.authToken = authToken;
        }
    }
}
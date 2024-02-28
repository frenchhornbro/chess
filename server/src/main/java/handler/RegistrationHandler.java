package handler;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
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
            AuthData authData = this.regService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));
//            res.body(new Gson().toJson(new retObj(credentials.get("username"),authToken)));
            res.body(new Gson().toJson(authData));
            res.status(200);
            return res.body();
        }
        catch (Exception exception) {
            return new Gson().toJson(exception.getMessage());
            /*
            TODO: Populate the following errors:
             400 - Error: bad request
             500 - Error: description
             403 - Error: already taken
            */
        }
    }
}
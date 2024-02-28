package handler;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import service.RegistrationService;
import com.google.gson.Gson;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class RegistrationHandler {
    private final RegistrationService regService;

    public RegistrationHandler(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao) {
        this.regService = new RegistrationService(memUserDao, memAuthDao);
    }

    //[POST] /user {username, password, email}
    public Object register(Request request, Response response) {
        Gson serial = new Gson();
        try {
            Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
            AuthData authData = this.regService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));
//            response.body(new Gson().toJson(new retObj(credentials.get("username"),authToken)));
            response.body(serial.toJson(authData));
            response.status(200);
        }
        catch (ServiceException exception) {
            response.status(Integer.parseInt(exception.getErrorNum()));
            response.body(serial.toJson(exception.getMessage()));
        }
        catch (Exception otherException) {
            response.status(500);
            response.body(serial.toJson(otherException.getMessage()));
        }
        return response.body();
            /*
            TODO: Populate the following errors:
             400 - Error: bad request
             403 - Error: already taken
             500 - Error: description
            */
    }
}
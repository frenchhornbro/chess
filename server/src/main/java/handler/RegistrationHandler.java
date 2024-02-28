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
            response.body(serial.toJson(authData));
            response.status(200);
        }
        catch (ServiceException exception) {
            ErrorCarrier responder = new ErrorCarrier(exception.getMessage(), exception.getErrorNum());
            response.status(responder.getErrorNum());
            response.body(serial.toJson(responder));
        }
        catch (Exception otherException) {
            response.status(500);
            ErrorCarrier responder = new ErrorCarrier(otherException.getMessage(), 500);
            response.body(serial.toJson(responder));
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
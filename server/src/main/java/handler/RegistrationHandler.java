package handler;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import model.AuthData;
import service.RegistrationService;
import com.google.gson.Gson;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class RegistrationHandler {
    private final RegistrationService regService;

    public RegistrationHandler(SQLUserDAO sqlUserDAO, SQLAuthDAO sqlAuthDAO) {
        this.regService = new RegistrationService(sqlUserDAO, sqlAuthDAO);
    }

    public Object register(Request request, Response response) {
        //Add UserData to a database (given nothing is null and the username isn't already taken)
        Gson serial = new Gson();
        try {
            Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
            String authToken = this.regService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));

            //TODO: Make sure we're not getting new errors with trying to JSONify a String
            response.body(serial.toJson(authToken));
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
    }
}
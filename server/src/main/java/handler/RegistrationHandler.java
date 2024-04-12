package handler;

import com.google.gson.Gson;
import service.ServiceException;
import spark.Request;
import spark.Response;

public class RegistrationHandler extends Handler {
    public Object register(Request request, Response response) {
        //Add UserData to a database (given nothing is null and the username isn't already taken)
        Gson serial = new Gson();
        try {
            response = verifyCredentials(response, request, serial, true);
        }
        catch (ServiceException exception) {
            response = handleServiceEx(exception, response, serial);
        }
        catch (Exception otherException) {
            response.status(500);
            ErrorCarrier responder = new ErrorCarrier(otherException.getMessage(), 500);
            response.body(serial.toJson(responder));
        }
        return response.body();
    }
}
package handler;

import com.google.gson.Gson;
import service.ServiceException;
import spark.Response;
import spark.Request;

public class LoginHandler extends Handler {
    public Object login(Request request, Response response) {
        // Verify the credentials and return authData
        Gson serial = new Gson();
        try {
            response = verifyCredentials(response, request, serial, false);
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

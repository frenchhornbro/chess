package handler;

import com.google.gson.Gson;
import service.LogoutService;
import service.ServiceException;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public Object logout(Request request, Response response) {
        // Verify authToken is correct and delete authData
        Gson serial = new Gson();
        try {
            String authToken = request.headers("authorization");
            LogoutService logoutService= new LogoutService();
            logoutService.logout(authToken);

            response.status(200);
            response.body("{}");
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

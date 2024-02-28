package handler;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import service.LogoutService;
import service.ServiceException;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private final LogoutService logoutService;

    public LogoutHandler(MemoryAuthDAO memAuthDAO) {
        this.logoutService = new LogoutService(memAuthDAO);
    }

    public Object logout(Request request, Response response) {
        Gson serial = new Gson();
        try {
            String authToken = request.headers("authorization");
            this.logoutService.logout(authToken);
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
        /*
        TODO: Populate the following errors:
          401 - Error: unauthorized
          500 - Error: description
         */
    }
}

package handler;

import service.RegistrationService;
import com.google.gson.Gson;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class RegistrationHandler extends Handler {
    public Object register(Request request, Response response) {
        //Add UserData to a database (given nothing is null and the username isn't already taken)
        Gson serial = new Gson();
        try {
            Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
            RegistrationService registrationService = new RegistrationService();
            String authToken = registrationService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));
            UserAuth userAuthStorage = new UserAuth(credentials.get("username"), authToken);

            response.body(serial.toJson(userAuthStorage));
            response.status(200);
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

    private record UserAuth(String username, String authToken) {}
}
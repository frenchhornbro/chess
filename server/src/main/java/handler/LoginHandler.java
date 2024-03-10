package handler;

import com.google.gson.Gson;
import service.LoginService;
import service.ServiceException;
import spark.Response;
import spark.Request;

import java.util.Map;

public class LoginHandler {
    public Object login(Request request, Response response) {
        // Verify the credentials and return authData
        Gson serial = new Gson();
        try {
            Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
            LoginService loginService = new LoginService();
            String authToken = loginService.login(credentials.get("username"), credentials.get("password"));
            UserAuth userAuthStorage = new UserAuth(credentials.get("username"), authToken);

            response.body(serial.toJson(userAuthStorage));
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

    private record UserAuth(String username, String authToken) {}
}

package handler;

import com.google.gson.Gson;
import service.LoginService;
import service.RegistrationService;
import service.ServiceException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class Handler {
    protected Response handleServiceEx(ServiceException exception, Response response, Gson serial) {
        ErrorCarrier responder = new ErrorCarrier(exception.getMessage(), exception.getErrorNum());
        response.status(responder.getErrorNum());
        response.body(serial.toJson(responder));
        return response;
    }

    protected Response verifyCredentials(Response response, Request request, Gson serial, boolean isRegister) throws Exception {
        Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
        String authToken;
        if (isRegister) {
            RegistrationService registrationService = new RegistrationService();
            authToken = registrationService.register(credentials.get("username"), credentials.get("password"), credentials.get("email"));
        }
        else {
            LoginService loginService = new LoginService();
            authToken = loginService.login(credentials.get("username"), credentials.get("password"));
        }
        UserAuth userAuthStorage = new UserAuth(credentials.get("username"), authToken);
        response.body(serial.toJson(userAuthStorage));
        response.status(200);
        return response;
    }

    private record UserAuth(String username, String authToken) {}
}

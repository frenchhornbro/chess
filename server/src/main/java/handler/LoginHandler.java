package handler;

import com.google.gson.Gson;
import model.AuthData;
import service.LoginService;
import service.ServiceException;
import spark.Response;
import spark.Request;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;

import java.util.Map;

public class LoginHandler {

    private final LoginService loginService;

    public LoginHandler(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao) {
        this.loginService = new LoginService(memUserDao, memAuthDao);
    }

    public Object login(Request request, Response response) {
        // Verify the credentials and return authData
        Gson serial = new Gson();
        try {
            Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
            AuthData authData = this.loginService.login(credentials.get("username"), credentials.get("password"));

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
    }
}

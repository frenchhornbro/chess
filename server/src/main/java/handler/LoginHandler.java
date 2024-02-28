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

    public Object login(Request request, Response response) throws Exception {
        Gson serial = new Gson();
        try {
            Map<String,String> credentials = serial.fromJson(request.body(), Map.class);
            AuthData authData = this.loginService.login(credentials.get("username"), credentials.get("password"));

            response.body(serial.toJson(authData));
            response.status(200);
        }
        catch (ServiceException exception) {
            response.status(Integer.parseInt(exception.getErrorNum()));
            Responder responder = new Responder(exception.getMessage(), exception.getErrorNum());
            response.body(serial.toJson(responder));
        }
        catch (Exception otherException) {
            response.status(500);
            Responder responder = new Responder(otherException.getMessage(), "500");
            response.body(serial.toJson(responder));
        }
        return response.body();
        /*
        TODO: Populate the following errors:
         401 - Error: unauthorized
         500 - Error: description
         */
    }

    private static class Responder {
        private final String message;
        private final int errorNum;

        private Responder(String errorMsg, String errorNum) {
            this.message = errorMsg;
            this.errorNum = Integer.parseInt(errorNum);
        }
    }
}

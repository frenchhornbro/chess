package handler;

import com.google.gson.Gson;
import model.AuthData;
import service.LoginService;
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
        try {
            Map<String,String> credentials = new Gson().fromJson(request.body(), Map.class);
            AuthData authData = this.loginService.login(credentials.get("username"), credentials.get("password"));

            response.body(new Gson().toJson(authData));
            response.status(200);
            return response.body();
        }
        catch (Exception exception) {
            response.status(401);
            return new Gson().toJson(exception.getMessage());/*
        TODO: Populate the following errors:
         401 - Error: unauthorized
         500 - Error: description
         */
        }
    }
}

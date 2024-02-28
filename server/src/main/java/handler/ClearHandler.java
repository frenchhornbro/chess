package handler;

import com.google.gson.Gson;
import dataAccess.MemoryGameDAO;
import service.ClearService;
import spark.Request;
import spark.Response;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao, MemoryGameDAO memGameDAO) {
        this.clearService = new ClearService(memUserDao, memAuthDao, memGameDAO);
    }

    public Object clearApplication(Request req, Response res) {
        try {
            this.clearService.clear();
            res.status(200);
            return "";
        }
        catch(Exception exception) {
            return new Gson().toJson(exception.getMessage());
        }
    }
}

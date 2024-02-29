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
            res.body("{}");
            return res.body();
        }
        catch(Exception exception) {
            res.status(500);
            res.body(new Gson().toJson(exception.getMessage()));
            return res.body();
        }
    }
}

package handler;

import com.google.gson.Gson;
import dataAccess.SQLGameDAO;
import service.ClearService;
import spark.Request;
import spark.Response;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(SQLUserDAO memUserDao, SQLAuthDAO memAuthDao, SQLGameDAO memGameDAO) {
        this.clearService = new ClearService(memUserDao, memAuthDao, memGameDAO);
    }

    public Object clearApplication(Request req, Response res) {
        //Clear all data
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

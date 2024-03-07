package handler;

import com.google.gson.Gson;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public Object clearApplication(Request req, Response res) {
        //Clear all data
        try {
            ClearService clearService = new ClearService();
            clearService.clear();
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

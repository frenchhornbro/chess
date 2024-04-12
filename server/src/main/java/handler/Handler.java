package handler;

import com.google.gson.Gson;
import service.ServiceException;
import spark.Response;

public class Handler {
    protected Response handleServiceEx(ServiceException exception, Response response, Gson serial) {
        ErrorCarrier responder = new ErrorCarrier(exception.getMessage(), exception.getErrorNum());
        response.status(responder.getErrorNum());
        response.body(serial.toJson(responder));
        return response;
    }
}

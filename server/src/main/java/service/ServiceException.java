package service;

public class ServiceException extends Exception {
    private final String errorNum;

    public ServiceException(String message, String errorNum) {
        super(message);
        this.errorNum = errorNum;
    }

    public String getErrorNum() {
        return errorNum;
    }
}

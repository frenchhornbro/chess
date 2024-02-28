package service;

public class ServiceException extends Exception {
    private final int errorNum;

    public ServiceException(String message, int errorNum) {
        super(message);
        this.errorNum = errorNum;
    }

    public int getErrorNum() {
        return errorNum;
    }
}

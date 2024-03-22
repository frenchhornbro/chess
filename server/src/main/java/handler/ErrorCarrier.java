package handler;

public class ErrorCarrier {
    private final String message;
    private final int errorNum;

    public ErrorCarrier(String errorMsg, int errorNum) {
        this.message = errorMsg;
        this.errorNum = errorNum;
    }

    public int getErrorNum() {
        return errorNum;
    }

    @Override
    public String toString() {
        return "Error " + errorNum + ": " + message;
    }
}

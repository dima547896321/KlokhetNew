package nl.klokhet.app.data.network.exception;

public class ApiException extends Exception {

    private Integer mStatusCode;

    private String error;

    public ApiException() {
        super();
        //
    }

    public ApiException(Integer statusCode, String error) {
        super(error);
        this.mStatusCode = statusCode;
        this.error = error;
    }

    public Integer getStatusCode() {
        return mStatusCode;
    }

    public String getError() {
        return error;
    }
}
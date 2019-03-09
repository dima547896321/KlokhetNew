package nl.klokhet.app.data.network.exception;

public class ValidationError {

    private String key;

    private String message;

    public ValidationError() {
        //
    }

    public ValidationError(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

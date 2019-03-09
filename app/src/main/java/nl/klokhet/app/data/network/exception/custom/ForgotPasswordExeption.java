package nl.klokhet.app.data.network.exception.custom;

public class ForgotPasswordExeption {

    private String errorMessage;

    public ForgotPasswordExeption(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

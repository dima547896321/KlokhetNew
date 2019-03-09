package nl.klokhet.app.data.network.exception.custom;

public class EditProfileException {

    private String errorMessage;

    public EditProfileException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

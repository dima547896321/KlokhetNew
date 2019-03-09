package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class Status implements Serializable {
    private Boolean success;
    private String messages;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return messages;
    }

    public void setMessage(String message) {
        this.messages = message;
    }
}

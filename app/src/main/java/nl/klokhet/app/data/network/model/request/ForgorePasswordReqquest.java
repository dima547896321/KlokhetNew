package nl.klokhet.app.data.network.model.request;

import java.io.Serializable;

public class ForgorePasswordReqquest implements Serializable {
    private String email;

    public ForgorePasswordReqquest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

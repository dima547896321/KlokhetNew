package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class LoginResponce implements Serializable {
    private String token;

    public LoginResponce(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

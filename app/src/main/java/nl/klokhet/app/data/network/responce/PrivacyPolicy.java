package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class PrivacyPolicy implements Serializable {
    private String text;

    public PrivacyPolicy(String data) {
        this.text = data;
    }

    public String getData() {
        return text;
    }

    public void setData(String data) {
        this.text = data;
    }
}

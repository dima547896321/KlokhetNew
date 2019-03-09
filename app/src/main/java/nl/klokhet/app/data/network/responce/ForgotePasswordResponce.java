package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class ForgotePasswordResponce implements Serializable {
    Status status;

    public ForgotePasswordResponce() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

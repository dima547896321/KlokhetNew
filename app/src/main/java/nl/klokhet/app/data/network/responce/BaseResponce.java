package nl.klokhet.app.data.network.responce;

public class BaseResponce<S , D> {
    private S status;
    private D data;

    public BaseResponce(S status, D data) {
        this.status = status;
        this.data = data;
    }

    public S getStatus() {
        return status;
    }

    public void setStatus(S status) {
        this.status = status;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}

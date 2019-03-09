package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class LessonsResponce implements Serializable {
    private String token;
    private Lesson current;

    public LessonsResponce() {
    }

    public LessonsResponce(Lesson current) {
        this.current = current;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Lesson getCurrent() {
        return current;
    }

    public void setCurrent(Lesson current) {
        this.current = current;
    }
}

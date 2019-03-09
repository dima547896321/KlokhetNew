package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class UserProfileResponce implements Serializable {
    private UserInGroup student;

    public UserProfileResponce() {
    }

    public UserInGroup getStudent() {
        return student;
    }

    public void setStudent(UserInGroup student) {
        this.student = student;
    }
}

package nl.klokhet.app.data.network.responce;

import java.io.Serializable;

public class TeacherInfoResponce implements Serializable {
    private TeacherBean teacher;

    public TeacherInfoResponce() {
    }

    public TeacherBean getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherBean teacher) {
        this.teacher = teacher;
    }
}

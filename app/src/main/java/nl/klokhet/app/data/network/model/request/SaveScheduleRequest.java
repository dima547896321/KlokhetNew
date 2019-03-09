package nl.klokhet.app.data.network.model.request;

import java.io.Serializable;

public class SaveScheduleRequest implements Serializable {
    private String name;
    private String lessonId;
    private String lessonTypeId;
    private String studentGroupId;
    private String locationId;
    private String dateTimeStart;
    private String duration;

    public SaveScheduleRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTypeId() {
        return lessonTypeId;
    }

    public void setLessonTypeId(String lessonTypeId) {
        this.lessonTypeId = lessonTypeId;
    }

    public String getStudentGroupId() {
        return studentGroupId;
    }

    public void setStudentGroupId(String studentGroupId) {
        this.studentGroupId = studentGroupId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(String dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "SaveScheduleRequest{" +
                "name='" + name + '\'' +
                ", lessonId='" + lessonId + '\'' +
                ", lessonTypeId='" + lessonTypeId + '\'' +
                ", studentGroupId='" + studentGroupId + '\'' +
                ", locationId='" + locationId + '\'' +
                ", dateTimeStart='" + dateTimeStart + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}

package nl.klokhet.app.data.network.responce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheduleContextResponce implements Serializable {
    private List<ItemContextResponce> group = new ArrayList<>();
    private List<ItemContextResponce> location = new ArrayList<>();
    private List<ItemContextResponce> lesson = new ArrayList<>();
    private List<ItemContextResponce> lessonType = new ArrayList<>();

    public SheduleContextResponce() {
    }

    public List<ItemContextResponce> getGroup() {
        return group;
    }

    public void setGroup(List<ItemContextResponce> group) {
        this.group = group;
    }

    public List<ItemContextResponce> getLocation() {
        return location;
    }

    public void setLocation(List<ItemContextResponce> location) {
        this.location = location;
    }

    public List<ItemContextResponce> getLesson() {
        return lesson;
    }

    public void setLesson(List<ItemContextResponce> lesson) {
        this.lesson = lesson;
    }

    public List<ItemContextResponce> getLessonType() {
        return lessonType;
    }

    public void setLessonType(List<ItemContextResponce> lessonType) {
        this.lessonType = lessonType;
    }
}

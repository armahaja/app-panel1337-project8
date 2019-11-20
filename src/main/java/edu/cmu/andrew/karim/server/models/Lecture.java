package edu.cmu.andrew.karim.server.models;

public class Lecture {
    String lectureName = null;
    String groupId = null;
    String userId = null;
    String lectureDescription = null;
    String evenId = null;
    Boolean lectureApproved = null;
    Integer lectureVotes = null;
    Integer duration; // this is in hours
    String lectureId = null;
    public Lecture(){}
    public Lecture(String lectureName, String groupId, String userId, String lectureDescription, String evenId, Boolean lectureApproved, Integer lectureVotes, Integer duration, String lectureId) {
        this.lectureName = lectureName;
        this.groupId = groupId;
        this.userId = userId;
        this.lectureDescription = lectureDescription;
        this.evenId = evenId;
        this.lectureApproved = lectureApproved;
        this.lectureVotes = lectureVotes;
        this.duration = duration;
        this.lectureId = lectureId;
    }


    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLectureDescription(String lectureDescription) {
        this.lectureDescription = lectureDescription;
    }

    public void setEvenId(String evenId) {
        this.evenId = evenId;
    }

    public void setLectureApproved(Boolean lectureApproved) {
        this.lectureApproved = lectureApproved;
    }

    public void setLectureVotes(Integer lectureVotes) {
        this.lectureVotes = lectureVotes;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }
    public String getLectureName() {
        return lectureName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLectureDescription() {
        return lectureDescription;
    }

    public String getEvenId() {
        return evenId;
    }

    public Boolean getLectureApproved() {
        return lectureApproved;
    }

    public Integer getLectureVotes() {
        return lectureVotes;
    }

    public Integer getDuration() {
        return duration;
    }

}

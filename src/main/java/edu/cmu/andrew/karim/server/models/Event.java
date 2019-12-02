package edu.cmu.andrew.karim.server.models;

public class Event {

    String eventId;
    String groupId;
    String userId; //(person who is teaching)
    String eventLocation;
    String eventDuration;
    String eventDatetime;

    public Event() {

    }

    public Event(String eventId, String groupId, String userId, String eventLocation, String eventDuration, String eventDatetime) {
        this.eventId = eventId;
        this.groupId = groupId;
        this.userId = userId;
        this.eventLocation = eventLocation;
        this.eventDuration = eventDuration;
        this.eventDatetime = eventDatetime;
    }

    public String getEventId() { return eventId; }

    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getEventLocation() { return eventLocation; }

    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public String getEventDuration() { return eventDuration; }

    public void setEventDuration(String eventDuration) { this.eventDuration = eventDuration; }

    public String getEventDatetime() { return eventDatetime; }

    public void setEventDatetime(String eventDatetime) { this.eventDatetime = eventDatetime; }

}

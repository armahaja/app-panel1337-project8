package edu.cmu.andrew.karim.server.managers;//package edu.cmu.andrew.karim.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.model.Sorts;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Event;
import edu.cmu.andrew.karim.server.models.User;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import java.lang.String;
import java.util.ArrayList;

public class EventManager extends Manager {
    public static EventManager _self;
    private MongoCollection<Document> eventCollection;

    public EventManager() { this.eventCollection = MongoPool.getInstance().getCollection("events"); }

    public static EventManager getInstance() {
        if (_self == null)
            _self = new EventManager();
        return _self;
    }

    public void createEvent(Event event) throws AppException {
        try{
            JSONObject json = new JSONObject(event);

            Document newDoc = new Document()
                    .append("eventId", event.getEventId())
                    .append("groupId", event.getGroupId())
                    .append("userId", event.getUserId())
                    .append("eventLocation", event.getEventLocation())
                    .append("eventDuration", event.getEventDuration())
                    .append("eventDatetime", event.getEventDatetime());
            if (newDoc != null)
                eventCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0,"Failed to create new event");

        } catch (Exception e) {
            throw handleException("Create Event", e);
        }
    }

    public void updateEvent(Event event) throws AppException {
        try {
            Bson filter = new Document("eventId", event.getEventId());
            Bson newValue = new Document()
                    .append("eventId", event.getEventId())
                    .append("groupId", event.getEventId())
                    .append("userId", event.getEventId())
                    .append("eventLocation", event.getEventLocation())
                    .append("eventDuration", event.getEventDuration())
                    .append("eventDatetime", event.getEventDatetime());

            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                eventCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update event details");

        } catch (Exception e) {
            throw handleException("Update Event", e);
        }
    }

    public void deleteEvent(String eventId) throws AppException {
        try {
            Bson filter = new Document("eventId", eventId);
            eventCollection.deleteOne(filter);
        } catch (Exception e) {
            throw handleException("Delete Event", e);
        }
    }

    public ArrayList<Event> getEventByGroupId(String groupId) throws AppException {
        try{
            ArrayList<Event> eventList = new ArrayList<>();
            FindIterable<Document> eventDocs = eventCollection.find();
            for (Document eventDoc: eventDocs) {
                if (eventDoc.getString("groupId").equals(groupId)) {
                    Event event = new Event(
                            eventDoc.getString("eventId"),
                            eventDoc.getString("groupId"),
                            eventDoc.getString("userId"),
                            eventDoc.getString("eventLocation"),
                            eventDoc.getString("eventDuration"),
                            eventDoc.getString("eventDatetime")
                    );
                    eventList.add(event);
                }
            }
            return new ArrayList<>(eventList);
        } catch(Exception e){
            throw handleException("Get Events in groupid = " + groupId, e);
        }
    }
}

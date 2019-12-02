package edu.cmu.andrew.karim.server.http.interfaces;//package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.CardManager;
import edu.cmu.andrew.karim.server.managers.EventManager;
import edu.cmu.andrew.karim.server.managers.PaymentManager;
import edu.cmu.andrew.karim.server.models.Card;
import edu.cmu.andrew.karim.server.models.Event;
import edu.cmu.andrew.karim.server.models.Payment;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

//        Use Case: Create an event
//        Request URL: http://localhost:8080/events
//
//        Use Case: Update an event
//        Request URL: http://localhost:8080/events
//
//        Use Case: Delete an event
//        Request URL: http://localhost:8080/events
//
//        Use Case: Filter event based on groupid
//        Request URL: http://localhost:8080/events


@Path("/events")
public class EventHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> eventCollection = null;

    public EventHttpInterface() { ow = new ObjectMapper().writer().withDefaultPrettyPrinter(); }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postEvent(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Event event = new Event(
                    json.getString("eventId"),
                    json.getString("groupId"),
                    json.getString("userId"),
                    json.getString("eventLocation"),
                    json.getString("eventDuration"),
                    json.getString("eventDatetime")

            );
            EventManager.getInstance().createEvent(event);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST events", e);
        }

    }

    @PATCH
    @Path("/{eventId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchEvent(Object request, @PathParam("eventId") String eventId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Event event = new Event(
                    eventId,
                    json.getString("groupId"),
                    json.getString("userId"),
                    json.getString("eventLocation"),
                    json.getString("eventDuration"),
                    json.getString("eventDatetime")
            );

            EventManager.getInstance().updateEvent(event);

        }catch (Exception e){
            throw handleException("PATCH events/{eventId}", e);
        }

        return new AppResponse("Update Successful");
    }


    @DELETE
    @Path("/{eventId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteEvent(@PathParam("eventId") String eventId){

        try{
            EventManager.getInstance().deleteEvent(eventId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE events/{eventId}", e);
        }

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getEvents(@Context HttpHeaders headers, @QueryParam("groupId") String groupId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Event> events = null;

            if(groupId != null)

                events = EventManager.getInstance().getEventByGroupId(groupId);

            if(events != null)
                return new AppResponse(events);
            else
                throw new HttpBadRequestException(0, "Problem with getting events");
        }catch (Exception e){
            throw handleException("GET /events", e);
        }
    }

}

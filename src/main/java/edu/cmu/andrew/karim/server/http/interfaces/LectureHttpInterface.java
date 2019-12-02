package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.LectureManager;
import edu.cmu.andrew.karim.server.managers.UserManager;
import edu.cmu.andrew.karim.server.models.Lecture;
import edu.cmu.andrew.karim.server.models.User;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/lectures")
public class LectureHttpInterface  extends HttpInterface {
    private ObjectWriter ow;
    public LectureHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postLecture(@Context HttpHeaders headers,Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Lecture lecture = new Lecture(json.getString("lectureName"),json.getString("groupId"),json.getString("userId"),
                    json.getString("lectureDescription"),json.getString("eventId"),Boolean.parseBoolean(json.getString("lectureApproved")),
                    Integer.parseInt(json.getString("lectureVotes")),Integer.parseInt(json.getString("duration")),json.getString("lectureId"));
            LectureManager.getInstance().createLecture(headers,lecture);
            return new AppResponse("Insert Successful");

        } catch (Exception e) {
            throw handleException("POST lecture ", e);
        }
    }
    @GET
    @Path("/{lectureId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleLectureByLectureId(@Context HttpHeaders headers, @PathParam("lectureId") String lectureId){

        try{
            AppLogger.info("Got an API call");
           Lecture lecture = LectureManager.getInstance().getLectureByLectureId(lectureId);

            if(lecture != null)
                return new AppResponse(lecture);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures by lectureid");
        }catch (Exception e) {
            throw handleException("GET /lectures/{lectureId}", e);
        }
    }
    @GET
    @Path("/event/{eventId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getLecturesByEventId(@Context HttpHeaders headers, @PathParam("eventId") String eventId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Lecture> lectures = LectureManager.getInstance().getLectureByEventId(eventId);

            if(lectures != null)
                return new AppResponse(lectures);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures by event id");
        }catch (Exception e) {
            throw handleException("GET /lectures/{eventId}", e);
        }
    }
    @GET
    @Path("/maxVotesLecture/{eventId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getLecturesWithMaximumVotes(@Context HttpHeaders headers, @PathParam("eventId") String eventId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Lecture> lectures = LectureManager.getInstance().getLecturewithMaxVotesByEvenId(eventId);
            if(lectures != null)
                return new AppResponse(lectures);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures with maximum votes by event id");
        }catch (Exception e) {
            throw handleException("GET /lectures/maxVotesLecture/{eventId}", e);
        }
    }
    @GET
    @Path("/group/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getLecturesByGroupId(@Context HttpHeaders headers, @PathParam("groupId") String groupId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Lecture> lectures = LectureManager.getInstance().getLectureByGroupId(groupId);

            if(lectures != null)
                return new AppResponse(lectures);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures by group id");
        }catch (Exception e) {
            throw handleException("GET /lectures/{groupId}", e);
        }
    }

    @GET
    @Path("/user/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getLecturesByUserId(@Context HttpHeaders headers, @PathParam("userId") String userId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Lecture> lectures = LectureManager.getInstance().getLectureByUserId(userId);
            if(lectures != null)
                return new AppResponse(lectures);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures by user id");
        }catch (Exception e) {
            throw handleException("GET /lectures/{userId}", e);
        }
    }
    @PATCH
    @Path("/{lectureId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchLectures(@Context HttpHeaders headers,Object request, @PathParam("lectureId") String lectureId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Lecture lecture = new Lecture(json.getString("lectureName"),json.getString("groupId"),json.getString("userId"),
                    json.getString("lectureDescription"),json.getString("eventId"),Boolean.parseBoolean(json.getString("lectureApproved")),
                    Integer.parseInt(json.getString("lectureVotes")),Integer.parseInt(json.getString("duration")),lectureId);
            LectureManager.getInstance().updateLecture(headers,lecture,lecture.getUserId());

        }catch (Exception e){
            throw handleException("PATCH lectures/{lectureId}", e);
        }

        return new AppResponse("Lecture Update Successful");
    }
    @POST
    @Path("/addVotes/{lectureId}/userId/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse addVotesToLecture(@Context HttpHeaders headers,Object request,@PathParam("lectureId") String lectureId,@PathParam("userId") String userId) {
        try {
            LectureManager.getInstance().addVotesToLecture(headers,lectureId,userId);
            return new AppResponse("Success: Votes added ");

        } catch (Exception e) {
            throw handleException("POST lecture ", e);
        }
    }
    @POST
    @Path("/approve/{lectureId}/{approved}/userId/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse approveLecture(@Context HttpHeaders headers,Object request,@PathParam("lectureId") String lectureId,@PathParam("approved") Boolean approved,@PathParam("userId") String userId) {
        try {
            LectureManager.getInstance().approveLecture(headers,lectureId,approved,userId);
            return new AppResponse("Lecture updated ");

        } catch (Exception e) {
            throw handleException("POST lecture ", e);
        }
    }
}

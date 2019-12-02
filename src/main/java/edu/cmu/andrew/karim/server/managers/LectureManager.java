package edu.cmu.andrew.karim.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.model.Sorts;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.exceptions.AppUnauthorizedException;
import edu.cmu.andrew.karim.server.models.Lecture;
import edu.cmu.andrew.karim.server.models.Session;
import edu.cmu.andrew.karim.server.models.User;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;


public class LectureManager extends Manager{
    public static LectureManager _self;
    private MongoCollection<Document> lectureCollection;


    public LectureManager() {
        this.lectureCollection = MongoPool.getInstance().getCollection("lectures");
    }

    public static LectureManager getInstance(){
        if (_self == null)
            _self = new LectureManager();
        return _self;
    }
    public void addVotesToLecture(HttpHeaders headers,String lectureId,String userId) throws AppException {
        try {
            Lecture lecture = this.getLectureByLectureId(lectureId);
            lecture.setLectureVotes(lecture.getLectureVotes()+1);
            updateLecture(headers,lecture,userId);
        } catch(Exception e) {
            throw handleException("Add Votes error ", e);
        }
    }
    public void approveLecture(HttpHeaders headers,String lectureId,Boolean approved,String userId) throws AppException {
        try {
            Lecture lecture = this.getLectureByLectureId(lectureId);
            lecture.setLectureApproved(approved);
            updateLecture(headers,lecture,userId);
        } catch(Exception e) {
            throw handleException("Approve Lecture Error ", e);
        }
    }
    public void createLecture(HttpHeaders headers, Lecture lecture) throws AppException {
        try{
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getUserId().equals(lecture.getUserId()))
                throw new AppUnauthorizedException(70,"Invalid user id");
            JSONObject json = new JSONObject(lecture);
            Document newDoc = new Document()
                    .append("eventId", lecture.getEvenId())
                    .append("groupId", lecture.getGroupId())
                    .append("duration", lecture.getDuration())
                    .append("lectureApproved",lecture.getLectureApproved())
                    .append("lectureDescription",lecture.getLectureDescription())
                    .append("lectureName", lecture.getLectureName())
                    .append("lectureVotes", lecture.getLectureVotes())
                    .append("userId",lecture.getUserId())
                    .append("lectureId",lecture.getLectureId());
            if (newDoc != null)
                lectureCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new lecture");

        }catch(Exception e){
            throw handleException("Create Lecture", e);
        }
    }
    public void updateLecture(HttpHeaders headers, Lecture lecture,String userId) throws AppException {
        try {
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getUserId().equals(userId))
                throw new AppUnauthorizedException(70,"Invalid user id");
            Bson filter = new Document("lectureId", lecture.getLectureId());
            Bson newValue = new Document()
                    .append("eventId", lecture.getEvenId())
                    .append("groupId", lecture.getGroupId())
                    .append("duration", lecture.getDuration())
                    .append("lectureApproved",lecture.getLectureApproved())
                    .append("lectureDescription",lecture.getLectureDescription())
                    .append("lectureName", lecture.getLectureName())
                    .append("lectureVotes", lecture.getLectureVotes())
                    .append("userId",lecture.getUserId())
                    .append("lectureId",lecture.getLectureId());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                lectureCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update Lecture details");

        } catch(Exception e) {
            throw handleException("Update Lecture ", e);
        }
    }
    public ArrayList<Lecture> getLectureByGroupId(String groupId) throws AppException {
        try{
            ArrayList<Lecture> lectureList = new ArrayList<>();
            FindIterable<Document> lectureDocs = lectureCollection.find();
            for(Document lectureDoc: lectureDocs) {
                if(lectureDoc.getString("groupId").equals(groupId)) {
                    Lecture lecture = new Lecture(lectureDoc.getString("lectureName"),lectureDoc.getString("groupId"),lectureDoc.getString("userId")
                            ,lectureDoc.getString("lectureDescription"),lectureDoc.getString("eventId"),Boolean.parseBoolean(lectureDoc.getString("lectureApproved"))
                            ,Integer.parseInt(lectureDoc.getString("lectureVotes")),Integer.parseInt(lectureDoc.getString("duration")),lectureDoc.getString("lectureId"));
                    lectureList.add(lecture);
                }
            }
            return new ArrayList<>(lectureList);
        } catch(Exception e){
            throw handleException("Get lectures proposed in a group by groupid", e);
        }

    }
    public Lecture getLectureByLectureId(String lectureId) throws AppException {
        try{
            FindIterable<Document> lectureDocs = lectureCollection.find();
            for(Document lectureDoc: lectureDocs) {
                if(lectureDoc.getString("lectureId").equals(lectureId)) {
                    Lecture lecture = new Lecture(lectureDoc.getString("lectureName"),lectureDoc.getString("groupId"),lectureDoc.getString("userId")
                            ,lectureDoc.getString("lectureDescription"),lectureDoc.getString("eventId"),lectureDoc.getBoolean("lectureApproved")
                            ,lectureDoc.getInteger("lectureVotes"),lectureDoc.getInteger("duration"),lectureDoc.getString("lectureId"));
                   return lecture;
                }
            }
            return new Lecture();
        } catch(Exception e){
            throw handleException("Get lecture by lecture id", e);
        }
    }
    public ArrayList<Lecture> getLectureByUserId(String userId) throws AppException {
        try{
            ArrayList<Lecture> lectureList = new ArrayList<>();
            FindIterable<Document> lectureDocs = lectureCollection.find();
            for(Document lectureDoc: lectureDocs) {
                if(lectureDoc.getString("userId").equals(userId)) {
                    Lecture lecture = new Lecture(lectureDoc.getString("lectureName"),lectureDoc.getString("groupId"),lectureDoc.getString("userId")
                            ,lectureDoc.getString("lectureDescription"),lectureDoc.getString("eventId"),lectureDoc.getBoolean("lectureApproved")
                            ,lectureDoc.getInteger("lectureVotes"),lectureDoc.getInteger("duration"),lectureDoc.getString("lectureId"));
                    lectureList.add(lecture);
                }
            }
            return new ArrayList<>(lectureList);
        } catch(Exception e){
            throw handleException("Get lectures by user id", e);
        }
    }
    public ArrayList<Lecture> getLectureByEventId(String eventId) throws AppException {
        try{
            ArrayList<Lecture> lectureList = new ArrayList<>();
            FindIterable<Document> lectureDocs = lectureCollection.find();
            for(Document lectureDoc: lectureDocs) {
                if(lectureDoc.getString("eventId").equals(eventId)) {
                    Lecture lecture = new Lecture(lectureDoc.getString("lectureName"),lectureDoc.getString("groupId"),lectureDoc.getString("userId")
                            ,lectureDoc.getString("lectureDescription"),lectureDoc.getString("eventId"),lectureDoc.getBoolean("lectureApproved")
                            ,lectureDoc.getInteger("lectureVotes"),lectureDoc.getInteger("duration"),lectureDoc.getString("lectureId"));
                    lectureList.add(lecture);
                }
            }
            return new ArrayList<>(lectureList);
        } catch(Exception e){
            throw handleException("Get lectures by user id", e);
        }
    }
    // get lecture with maximum votes based on event id
    public ArrayList<Lecture> getLecturewithMaxVotesByEvenId(String eventId) throws AppException {
        try{
            ArrayList<Lecture> lectureList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("lectureVotes", 1);
            FindIterable<Document> lectureDocs = lectureCollection.find().sort(sortParams);
            for(Document lectureDoc: lectureDocs) {
                if(lectureDoc.getString("eventId").equals(eventId)) {
                    Lecture lecture = new Lecture(lectureDoc.getString("lectureName"),lectureDoc.getString("groupId"),lectureDoc.getString("userId")
                            ,lectureDoc.getString("lectureDescription"),lectureDoc.getString("eventId"),lectureDoc.getBoolean("lectureApproved")
                            ,lectureDoc.getInteger("lectureVotes"),lectureDoc.getInteger("duration"),lectureDoc.getString("lectureId"));
                    lectureList.add(lecture);
                    break;
                }
            }
            return new ArrayList<>(lectureList);
        } catch(Exception e){
            throw handleException("Get User List", e);
        }
    }
}

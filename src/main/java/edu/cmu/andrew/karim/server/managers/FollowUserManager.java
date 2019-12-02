package edu.cmu.andrew.karim.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.model.Sorts;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.exceptions.AppUnauthorizedException;
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


public class FollowUserManager extends Manager {
    public static FollowUserManager _self;
    private MongoCollection<Document> followUserCollection;


    public FollowUserManager() {
        this.followUserCollection = MongoPool.getInstance().getCollection("followUser");
    }

    public static FollowUserManager getInstance() {
        if (_self == null)
            _self = new FollowUserManager();
        return _self;
    }
    public void createFollower(HttpHeaders headers, String followerUserId, String followingUserId) throws AppException {
        try{
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getUserId().equals(followerUserId))
                throw new AppUnauthorizedException(70,"Invalid user id");
               Document newDoc = new Document()
                    .append("followerUserId", followerUserId)
                    .append("followingUserId", followingUserId);
            if (newDoc != null)
                followUserCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create user following ");

        }catch(Exception e){
            throw handleException("Create user following ", e);
        }

    }
    public void deleteFollowingRelation(HttpHeaders headers,String followerUserId,String followingUserId) throws AppException {
        try {
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getUserId().equals(followerUserId))
                throw new AppUnauthorizedException(70,"Invalid user id");
            Bson filter = new Document()
                    .append("followerUserId", followerUserId)
                    .append("followingUserId", followingUserId);
            followUserCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete follow relation: ", e);
        }
    }
    // all the users who are following followingId
    public ArrayList<String> getAllFollowersByUserId(String followingId) throws AppException {
        try{
            ArrayList<String> followUserListId = new ArrayList<>();
            FindIterable<Document> followUserDocs = followUserCollection.find();
            for(Document followUserDoc : followUserDocs) {
                if(followUserDoc.getString("followingUserId").equals(followingId)) {
                    followUserListId.add(followUserDoc.getString("followerUserId"));
                }
            }
            return new ArrayList<>(followUserListId);
        } catch(Exception e){
            throw handleException("Get lectures proposed in a group by groupid", e);
        }
    }
    //all users which followerId is following
    public ArrayList<String> getAllFollowingByUserId(String followerId) throws AppException {
        try{
            ArrayList<String> followUserListId = new ArrayList<>();
            FindIterable<Document> followUserDocs = followUserCollection.find();
            for(Document followUserDoc : followUserDocs) {
                if(followUserDoc.getString("followerUserId").equals(followerId)) {
                    followUserListId.add(followUserDoc.getString("followingUserId"));
                }
            }
            return new ArrayList<>(followUserListId);
        } catch(Exception e){
            throw handleException("Get lectures proposed in a group by groupid", e);
        }

    }

}
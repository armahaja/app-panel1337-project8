package edu.cmu.andrew.karim.server.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Group;
import edu.cmu.andrew.karim.server.models.User;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class GroupManager extends Manager {
    public static GroupManager _self;
    private MongoCollection<Document> groupCollection;


    public GroupManager() {
        this.groupCollection = MongoPool.getInstance().getCollection("groups");
    }

    public static GroupManager getInstance() {
        if (_self == null)
            _self = new GroupManager();
        return _self;
    }

    public void createGroup(Group group) throws AppException {

        try{
            JSONObject json = new JSONObject(group);

            Document newDoc = new Document()
                    .append("groupname", group.getGroupname())
                    .append("password", group.getPassword())
                    .append("moderatorEmail",group.getModeratorEmail())
                    .append("zipcode", group.getZipcode());
//                    .append("userlist", group.getUserlist());
            if (newDoc != null)
                groupCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new Group");

        }catch(Exception e){
            throw handleException("Create Group", e);
        }

    }

    public void updateGroup( Group group) throws AppException {
        try {


            Bson filter = new Document("_id", new ObjectId(group.getGroupId()));
            Bson newValue = new Document()
                    .append("groupname", group.getGroupname())
                    .append("password", group.getPassword())
                    .append("moderatorEmail",group.getModeratorEmail())
                    .append("zipcode", group.getZipcode());
//                    .append("userlist", group.getUserlist());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                groupCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update group details");

        } catch(Exception e) {
            throw handleException("Update Group", e);
        }
    }

    public void deleteGroup(String groupId) throws AppException {
        try {
            Bson filter = new Document("_id", new ObjectId(groupId));
            userCollection.deleteOne(filter);

        }catch (Exception e){
            throw handleException("Delete Group", e);
        }
    }

    public ArrayList<Group> getGroupList() throws AppException {
        try{
            ArrayList<Group> groupList = new ArrayList<>();
            FindIterable<Document> groupDocs = groupCollection.find();
            for(Document groupDoc: groupDocs) {
                Group group = new Group(
                        groupDoc.getObjectId("_id").toString(),
                        groupDoc.getString("groupname"),
                        groupDoc.getString("password"),
                        groupDoc.getString("moderatorEmail"),
                        groupDoc.getInteger("zipcode")
                        // userList ????
                );
                groupList.add(group);
            }
            return new ArrayList<>(groupList);
        } catch(Exception e){
            throw handleException("Get Group List", e);
        }
    }

    public ArrayList<Group> getGroupListSorted(String sortby) throws AppException {
        try{
            ArrayList<Group> groupList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, 1);
            FindIterable<Document> groupDocs = groupCollection.find().sort(sortParams);
            for(Document groupDoc: groupDocs) {
                Group group = new Group(
                        groupDoc.getObjectId("_id").toString(),
                        groupDoc.getString("groupname"),
                        groupDoc.getString("password"),
                        groupDoc.getString("moderatorEmail"),
                        groupDoc.getInteger("zipcode")
                      //  new ArrayList<User>() // userList ???
                );
                groupList.add(group);
            }
            return new ArrayList<>(groupList);
        } catch(Exception e){
            throw handleException("Get Group List", e);
        }
    }

    public ArrayList<Group> getGroupListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Group> groupList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put("groupId", 1);
            FindIterable<Document> groupDocs = groupCollection.find().sort(sortParams).skip(offset).limit(count);
            for(Document groupDoc: groupDocs) {
                Group group = new Group(
                        groupDoc.getObjectId("_id").toString(),
                        groupDoc.getString("groupname"),
                        groupDoc.getString("password"),
                        groupDoc.getString("moderatorEmail"),
                        groupDoc.getInteger("zipcode")
                );
                groupList.add(group);
            }
            return new ArrayList<>(groupList);
        } catch(Exception e){
            throw handleException("Get Group List", e);
        }
    }

    public ArrayList<Group> getGroupById(String id) throws AppException {
        try{
            ArrayList<Group> groupList = new ArrayList<>();
            FindIterable<Document> groupDocs = groupCollection.find();
            for(Document groupDoc: groupDocs) {
                if(groupDoc.getObjectId("_id").toString().equals(id)) {
                    Group group = new Group(
                            groupDoc.getObjectId("_id").toString(),
                            groupDoc.getString("groupname"),
                            groupDoc.getString("password"),
                            groupDoc.getString("moderatorEmail"),
                            groupDoc.getInteger("zipcode")
                    );
                    groupList.add(group);
                }
            }
            return new ArrayList<>(groupList);
        } catch(Exception e){
            throw handleException("Get Group List", e);
        }
    }

    public ArrayList<Group> getGroupListFiltered(String zipcode) throws AppException {
        try{
            ArrayList<Group> groupList = new ArrayList<>();
            FindIterable<Document> groupDocs = groupCollection.find();
            for(Document groupDoc: groupDocs) {
                if(groupDoc.getInteger("zipcode").toString().equals(zipcode)) {
                    Group group = new Group(
                            groupDoc.getObjectId("_id").toString(),
                            groupDoc.getString("groupname"),
                            groupDoc.getString("password"),
                            groupDoc.getString("moderatorEmail"),
                            groupDoc.getInteger("zipcode")
                    );
                    groupList.add(group);
                }
            }
            return new ArrayList<>(groupList);
        } catch(Exception e){
            throw handleException("Get Group List", e);
        }

    }

}

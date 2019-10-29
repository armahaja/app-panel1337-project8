package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.GroupManager;
import edu.cmu.andrew.karim.server.managers.UserManager;
import edu.cmu.andrew.karim.server.models.Group;
import edu.cmu.andrew.karim.server.models.User;
import edu.cmu.andrew.karim.server.utils.*;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/groups")
public class GroupHttpInterface extends HttpInterface {
    private ObjectWriter ow;
    private MongoCollection<Document> groupCollection = null;

    public GroupHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postGroups(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Group group = new Group(
                    null,
                    json.getString("groupname"),
                    json.getString("password"),
                    json.getString("moderatorEmail"),
                    json.getInt("zipcode")

            );
            GroupManager.getInstance().createGroup(group);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST groups", e);
        }

    }

    //Sorting: http://localhost:8080/groups?sortby=groupname
    //Pagination: http://localhost:8080/groups?offset=1&count=2
    //Filtering: http://localhost:8080/group?zipcode=94086
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getGroups(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                @QueryParam("count") Integer count, @QueryParam("zipcode") String zipcode){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Group> groups = null;

            if(sortby != null)
                groups = GroupManager.getInstance().getGroupListSorted(sortby);
            else if(offset != null && count != null)
                groups = GroupManager.getInstance().getGroupListPaginated(offset, count);
            else if(zipcode != null)
                groups = GroupManager.getInstance().getGroupListFiltered(zipcode);
            else
                groups = GroupManager.getInstance().getGroupList();

            if(groups != null)
                return new AppResponse(groups);
            else
                throw new HttpBadRequestException(0, "Problem with getting groups");
        }catch (Exception e){
            throw handleException("GET /groups", e);
        }
    }

    @GET
    @Path("/{groupId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleGroup(@Context HttpHeaders headers, @PathParam("groupId") String groupId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Group> groups = GroupManager.getInstance().getGroupById(groupId);

            if(groups != null)
                return new AppResponse(groups);
            else
                throw new HttpBadRequestException(0, "Problem with getting groups");
        }catch (Exception e){
            throw handleException("GET /groups/{groupId}", e);
        }


    }


    @PATCH
    @Path("/{groupId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchGroups(Object request, @PathParam("groupId") String groupId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Group group = new Group(
                    groupId,
                    json.getString("groupname"),
                    json.getString("password"),
                    json.getString("moderatorEmail"),
                    json.getInt("zipcode")
            );

            GroupManager.getInstance().updateGroup(group);

        }catch (Exception e){
            throw handleException("PATCH groups/{groupId}", e);
        }

        return new AppResponse("Update Successful");
    }




    @DELETE
    @Path("/{groupId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteGroups(@PathParam("groupId") String groupId){

        try{
            GroupManager.getInstance().deleteGroup(groupId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE groups/{groupId}", e);
        }

    }

    @POST
    @Path("/reset")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse addDummyUsers(){

        try{
            JSONObject json = null;
            for(int i =0;i<10;i++) {
                String groupId = i<5?"group1":"group2";
                Integer zipcode = i<5?94086: 94482;

                Group group = new Group();
                group.setGroupId(null);
                group.setGroupname("group" + i);
                group.setPassword("group" + i + "***");
                group.setModeratorEmail("group" + i + "@gmail.com");
                group.setZipcode(zipcode);

                GroupManager.getInstance().createGroup(group);
            }
            return new AppResponse("Reset Successful");
        }catch (Exception e){
            throw handleException("POST reset groups", e);
        }

    }
}

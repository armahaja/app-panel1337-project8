package edu.cmu.andrew.karim.server.http.interfaces;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.models.User;
import edu.cmu.andrew.karim.server.managers.UserManager;
import edu.cmu.andrew.karim.server.utils.*;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/users")

public class UserHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> userCollection = null;

    public UserHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postUsers(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            User user = new User();
            user.setId(null);
            user.setFirstname(json.getString("firstname"));
            user.setLastname(json.getString("lastname"));
            user.setEmail( json.getString("email"));
            user.setGroupId( json.getString("groupId"));
            user.setPassword(json.getString("password"));
            user.setZipcode(json.getInt("zipcode"));
            user.setAddress(json.getString("address"));
            UserManager.getInstance().createUser(user);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST users", e);
        }

    }

    //Sorting: http://localhost:8080/users?sortby=firstname
    //Pagination: http://localhost:8080/users?offset=1&count=2
    //Filtering: http://localhost:8080/users?groupId=94086
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getUsers(@Context HttpHeaders headers, @QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                @QueryParam("count") Integer count,@QueryParam("groupid") String groupId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<User> users = null;

            if(sortby != null)
                users = UserManager.getInstance().getUserListSorted(sortby);
            else if(offset != null && count != null)
                users = UserManager.getInstance().getUserListPaginated(offset, count);
            else if(groupId != null)
                users = UserManager.getInstance().getUserListFiltered(groupId);

            else
                users = UserManager.getInstance().getUserList();

            if(users != null)
                return new AppResponse(users);
            else
                throw new HttpBadRequestException(0, "Problem with getting users");
        }catch (Exception e){
            throw handleException("GET /users", e);
        }
    }


    @GET
    @Path("/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleUser(@Context HttpHeaders headers, @PathParam("userId") String userId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<User> users = UserManager.getInstance().getUserById(headers,userId);

            if(users != null)
                return new AppResponse(users);
            else
                throw new HttpBadRequestException(0, "Problem with getting users");
        }catch (Exception e){
            throw handleException("GET /users/{userId}", e);
        }


    }


    @PATCH
    @Path("/{userId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchUsers(@Context HttpHeaders headers,Object request, @PathParam("userId") String userId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            User user = new User(
                    userId,
                    json.getString("firstname"),
                    json.getString("lastname"),
                    json.getString("password"),
                    json.getString("email"),
                    json.getString("groupId"),
                    json.getInt("zipcode"),
                    json.getString("address")
            );

            UserManager.getInstance().updateUser(headers,user);

        }catch (Exception e){
            throw handleException("PATCH users/{userId}", e);
        }

        return new AppResponse("Update Successful");
    }




    @DELETE
    @Path("/{userId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteUsers(@PathParam("userId") String userId){

        try{
            UserManager.getInstance().deleteUser( userId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE users/{userId}", e);
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
                User user = new User();
                user.setId(null);
                user.setFirstname("user"+i);
                user.setLastname("dummy"+i);
                user.setEmail("user"+i+"@outlook.com");
                user.setGroupId(groupId);
                user.setPassword("user"+i+"123");
                user.setZipcode(zipcode);
                user.setAddress("dummy address for user"+i);
                UserManager.getInstance().createUser(user);
            }
            return new AppResponse("Insert Successful");
        }catch (Exception e){
            throw handleException("POST users", e);
        }

    }

}

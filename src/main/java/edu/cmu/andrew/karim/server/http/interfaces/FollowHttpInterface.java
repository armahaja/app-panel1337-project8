package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.managers.FollowUserManager;
import edu.cmu.andrew.karim.server.managers.LectureManager;
import edu.cmu.andrew.karim.server.models.FollowUser;
import edu.cmu.andrew.karim.server.models.Lecture;
import edu.cmu.andrew.karim.server.utils.AppLogger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/follow")
public class FollowHttpInterface  extends HttpInterface {
    private ObjectWriter ow;
    public FollowHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    @POST
    @Path("/{followerId}/{followingId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse followUser(@Context HttpHeaders headers,Object request,@PathParam("followerId") String followerId,@PathParam("followingId") String followingId) {
        try {
            FollowUserManager.getInstance().createFollower(headers,followerId,followingId);
            return new AppResponse("User Followed ");

        } catch (Exception e) {
            throw handleException("POST follow//{followerId}/{followingId} ", e);
        }
    }
    @DELETE
    @Path("/{followerId}/{followingId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse unfollowUser(@Context HttpHeaders headers,Object request,@PathParam("followerId") String followerId,@PathParam("followingId") String followingId) {
        try {
            FollowUserManager.getInstance().deleteFollowingRelation(headers,followerId,followingId);
            return new AppResponse("User UnFollowed ");

        } catch (Exception e) {
            throw handleException("POST follow/{followerId}/{followingId} ", e);
        }
    }

    // all the users who are following followingId
    @GET
    @Path("/getFollowers/{followingId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getAllFollowersByUserId(@Context HttpHeaders headers, @PathParam("followingId") String followingId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<String> following = FollowUserManager.getInstance().getAllFollowersByUserId(followingId);

            if(following != null)
                return new AppResponse(following);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures by event id");
        }catch (Exception e) {
            throw handleException("GET /follow/{followingId}", e);
        }
    }
    //all users which are being followed by followerId
    @GET
    @Path("/getFolowing/{followerId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getAllFollowingByUserId(@Context HttpHeaders headers, @PathParam("followerId") String followerId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<String> following = FollowUserManager.getInstance().getAllFollowingByUserId(followerId);

            if(following != null)
                return new AppResponse(following);
            else
                throw new HttpBadRequestException(0, "Problem with getting lectures by event id");
        }catch (Exception e) {
            throw handleException("GET /follow/{followerId}", e);
        }
    }

}

package edu.cmu.andrew.karim.server.models;

public class FollowUser {
    String followerUserId;
    String followingUserId;

    public void setFollowerUserId(String followerUserId) {
        this.followerUserId = followerUserId;
    }

    public void setFollowingUserId(String followingUserId) {
        this.followingUserId = followingUserId;
    }



    public FollowUser(String followerUserId, String followingUserId) {
        this.followerUserId = followerUserId;
        this.followingUserId = followingUserId;
    }

    public FollowUser() {
    }

    public String getFollowerUserId() {
        return followerUserId;
    }

    public String getFollowingUserId() {
        return followingUserId;
    }
}

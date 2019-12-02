package edu.cmu.andrew.karim.server.models;

import java.util.UUID;

public class Session {

    public  String token = null;
    public String userId = null;
    public String username = null;

    public Session(User user) throws Exception{
        this.userId = user.id;
        this.token = UUID.randomUUID().toString();
        this.username = user.email;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

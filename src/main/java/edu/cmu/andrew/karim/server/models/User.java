package edu.cmu.andrew.karim.server.models;


public class User {

    String id = null;
    String firstname = null;
    String lastname = null;
    String password = null;
    String email = null;
    String groupId = null;
    Integer zipcode = null;
    String address = null;
    public User(){

    }
    public User(String id, String firstname,String lastname, String password, String email, String groupId,Integer zipcode,String address) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.groupId = groupId;
        this.zipcode = zipcode;
        this.address = address;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id){ this.id = id; }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPassword() { return password; }

    public String getLastname() { return lastname;}

    public Integer getZipcode() { return zipcode;}

    public String  getAddress() { return address;}

    public String  getGroupId() { return groupId;}

}

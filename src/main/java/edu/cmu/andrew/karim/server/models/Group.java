package edu.cmu.andrew.karim.server.models;

import java.util.List;

public class Group {

/*Group Properties
    Group Name
    Group Id
    Group joining Price
    Group Budget (will be decided based on number of users in a group after they joined a paid subscription)
    Group trial classes
    Group Moderator
    Group Users
    Group Description
    Group Policies
    Group health
*/

//    String moderatorId = null;
//    String groupdetail = null;
//    String groupJoinPrice = null;

    String groupId = null;
    String groupname = null;
    String password = null;
    String moderatorEmail = null;
    Integer zipcode = null;
//    List<User> userlist = null;
    public Group() {

    }


    public Group(String groupId, String groupname, String password, String moderatorEmail, Integer zipcode) {
        this.groupId = groupId;
        this.groupname = groupname;
        this.password = password;
        this.moderatorEmail = moderatorEmail;
//        this.userlist = userlist;
        this.zipcode = zipcode;
//        this.moderatorId = moderatorId;

//        this.groupdetail = groupdetail;
//        this.groupJoinPrice = groupJoinPrice;
    }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGroupname() { return groupname; }

    public void setGroupname(String groupname) { this.groupname = groupname; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getModeratorEmail() { return moderatorEmail; }

    public void setModeratorEmail(String moderatorEmail) { this.moderatorEmail = moderatorEmail; }

//    public List<User> getUserlist() { return userlist; }
//
//    public void setUserlist(List<User> userlist) { this.userlist = userlist; }

    public Integer getZipcode() { return zipcode; }

    public void setZipcode(Integer zipcode) { this.zipcode = zipcode; }

//    public String getGroupdetail() { return groupdetail; }
//
//    public void setGroupdetail(String groupdetail) { this.groupdetail = groupdetail; }
//
//    public String getGroupJoinPrice() { return groupJoinPrice; }
//
//    public void setGroupJoinPrice(String groupJoinPrice) { this.groupJoinPrice = groupJoinPrice; }

//    public String getModeratorId() { return moderatorId; }
//
//    public void setModeratorId(String moderatorId) { this.moderatorId = moderatorId; }
//



}

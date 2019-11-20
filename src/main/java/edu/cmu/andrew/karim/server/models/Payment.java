package edu.cmu.andrew.karim.server.models;

//        User Id
//        Group Id
//        Amount paid

public class Payment {


    String userID = null;
    String groupId = null;
    String amountPaid = null;


    public Payment() {

    }

    public Payment(String userID, String groupId, String amountPaid) {
        this.userID = userID;
        this.groupId = groupId;
        this.amountPaid = amountPaid;

    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getAmountPaid() { return amountPaid; }

    public void setAmountPaid(String amountPaid) { this.amountPaid = amountPaid; }
}
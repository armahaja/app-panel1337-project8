package edu.cmu.andrew.karim.server.models;

public class Card {


    String userID = null;
    String cardNumbers = null;
    String cvv = null;
    String expireDate = null;

    public Card() {

    }

    public Card(String userID, String cardNumbers, String cvv, String expireDate) {
        this.userID = userID;
        this.cardNumbers = cardNumbers;
        this.cvv = cvv;
        this.expireDate = expireDate;
    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getCardNumbers() { return cardNumbers; }

    public void setCardNumbers(String cardNumbers) { this.cardNumbers = cardNumbers; }

    public String getCvv() { return cvv; }

    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getExpireDate() { return expireDate; }

    public void setExpireDate(String expireDate) { this.expireDate = expireDate; }


}


package edu.cmu.andrew.karim.server.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Card;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardManager extends Manager {
    public static CardManager _self;
    private MongoCollection<Document> cardCollection;


    public CardManager() {
        this.cardCollection = MongoPool.getInstance().getCollection("cards");
    }

    public static CardManager getInstance() {
        if (_self == null)
            _self = new CardManager();
        return _self;
    }

    public void createCard(Card card) throws AppException {

        try{
            JSONObject json = new JSONObject(card);

            Document newDoc = new Document()
                    .append("userId",card.getUserID()).append("cardNumber", card.getCardNumbers())
                    .append("cvv", card.getCvv())
                    .append("expireDate",card.getExpireDate());

            if (newDoc != null)
                cardCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new Card");

        }catch(Exception e){
            throw handleException("Create Card", e);
        }

    }

    public void updateCard(Card card) throws AppException {
        try {


            Bson filter = new Document("cardNumber", card.getCardNumbers());
            Bson newValue = new Document()
                    .append("cardNumber", card.getCardNumbers())
                    .append("cvv", card.getCvv())
                    .append("expireDate",card.getExpireDate());

            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                cardCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update card details");

        } catch(Exception e) {
            throw handleException("Update Card", e);
        }
    }

    public void deleteCard(String cardNumber) throws AppException {
        try {
            Bson filter = new Document("cardNumber", cardNumber);
            userCollection.deleteOne(filter);

        }catch (Exception e){
            throw handleException("Delete Card", e);
        }
    }


    public ArrayList<Card> getCardById(String id) throws AppException {
        try{
            ArrayList<Card> cardList = new ArrayList<>();
            FindIterable<Document> cardDocs = cardCollection.find();
            for(Document cardDoc: cardDocs) {
                if(cardDoc.getString("userId").equals(id)) {
                    Card card = new Card(
                            cardDoc.getString("userId"),
                            cardDoc.getString("cardNumber"),
                            cardDoc.getString("cvv"),
                            cardDoc.getString("expireDate")
                    );
                    cardList.add(card);
                }
            }
            return new ArrayList<>(cardList);
        } catch(Exception e){
            throw handleException("Get Card List", e);
        }
    }

}
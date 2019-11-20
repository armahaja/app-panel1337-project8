package edu.cmu.andrew.karim.server.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Card;
import edu.cmu.andrew.karim.server.models.Payment;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;


//        Create
//        Update (update amount paid based on userid)
//        Get amount by user id
//        Total amount by group id

//        String userID = null;
//        String groupId = null;
//        String amountPaid = null;

public class PaymentManager extends Manager {
    public static PaymentManager _self;
    private MongoCollection<Document> paymentCollection;


    public PaymentManager() {
        this.paymentCollection = MongoPool.getInstance().getCollection("payments");
    }

    public static PaymentManager getInstance() {
        if (_self == null)
            _self = new PaymentManager();
        return _self;
    }

    public void CreatePayment(Payment payment) throws AppException {

        try{
            JSONObject json = new JSONObject(payment);

            Document newDoc = new Document()
                    .append("userId",payment.getUserID()).append("groupId", payment.getGroupId())
                    .append("amountPaid", payment.getAmountPaid());

            if (newDoc != null)
                paymentCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new Payment");

        }catch(Exception e){
            throw handleException("Create Payment", e);
        }

    }

    public void updatePayment(Payment payment) throws AppException {
        try {


            Bson filter = new Document("userId", payment.getUserID());
            Bson newValue = new Document()
                    .append("userId", payment.getUserID())
                    .append("groupId", payment.getGroupId())
                    .append("amountPaid",payment.getAmountPaid());

            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                paymentCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update payment details");

        } catch(Exception e) {
            throw handleException("Update Payment", e);
        }
    }

    public Payment getPaymentByUserId(String userId) throws AppException {
        try{
            Payment payment = new Payment();
            FindIterable<Document> paymentDocs = paymentCollection.find();
            for(Document paymentDoc: paymentDocs) {
                if(paymentDoc.getString("userId").equals(userId)) {
                    payment = new Payment(
                            paymentDoc.getString("userId"),
                            paymentDoc.getString("groupId"),
                            paymentDoc.getString("amountPaid")
                    );

                }
            }
            return payment;
        } catch(Exception e){
            throw handleException("Get Payment ", e);
        }
    }

    public ArrayList<Payment> getPaymentByGroupId(String groupId) throws AppException {
        try{
            ArrayList<Payment> paymentsList = new ArrayList<>();
            FindIterable<Document> paymentDocs = paymentCollection.find();
            for(Document paymentDoc: paymentDocs) {
                if(paymentDoc.getString("groupId").equals(groupId)) {
                    Payment payment = new Payment(
                            paymentDoc.getString("userId"),
                            paymentDoc.getString("groupId"),
                            paymentDoc.getString("amountPaid")
                    );
                    paymentsList.add(payment);
                }
            }
            return new ArrayList<>(paymentsList);
        } catch(Exception e){
            throw handleException("Get Group Payments Detail ", e);
        }
    }


}

package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.CardManager;
import edu.cmu.andrew.karim.server.managers.PaymentManager;
import edu.cmu.andrew.karim.server.models.Card;
import edu.cmu.andrew.karim.server.models.Payment;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/payments")
public class PaymentHttpInterface extends HttpInterface {
    private ObjectWriter ow;
    private MongoCollection<Document> cardCollection = null;
    public PaymentHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postCards(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Payment payment = new Payment(
                    json.getString("userId"),
                    json.getString("groupId"),
                    json.getString("amountPaid")

            );
            PaymentManager.getInstance().CreatePayment(payment);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST payment", e);
        }

    }


    @GET
    @Path("/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPayment(@Context HttpHeaders headers, @PathParam("userId") String userId){
        try{
            AppLogger.info("Got an API call");
            Payment payment = null;

            if(userId != null)

                payment = PaymentManager.getInstance().getPaymentByUserId(userId);

            if(payment != null)
                return new AppResponse(payment);
            else
                throw new HttpBadRequestException(0, "Problem with getting payment");
        }catch (Exception e){
            throw handleException("GET /payments", e);
        }
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getPayments(@Context HttpHeaders headers, @QueryParam("groupId") String groupId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Payment> payments = null;

            if(groupId != null)

                payments = PaymentManager.getInstance().getPaymentByGroupId(groupId);

            if(payments != null)
                return new AppResponse(payments);
            else
                throw new HttpBadRequestException(0, "Problem with getting payments");
        }catch (Exception e){
            throw handleException("GET /payments", e);
        }
    }

//    @GET
//    @Path("/{userId}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public AppResponse getCards(@Context HttpHeaders headers, @PathParam("userId") String userId){
//        try{
//            AppLogger.info("Got an API call");
//            ArrayList<Card> cards = null;
//
//            if(userId != null)
//
//                cards = CardManager.getInstance().getCardById(userId);
//
//            if(cards != null)
//                return new AppResponse(cards);
//            else
//                throw new HttpBadRequestException(0, "Problem with getting cards");
//        }catch (Exception e){
//            throw handleException("GET /cards", e);
//        }
//    }

    @PATCH
    @Path("/{userId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchPayment(Object request, @PathParam("userId") String userId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Payment payment = new Payment(
                    userId,
                    json.getString("groupId"),
                    json.getString("amountPaid")
            );

            PaymentManager.getInstance().updatePayment(payment);

        }catch (Exception e){
            throw handleException("PATCH payments/{userId}", e);
        }

        return new AppResponse("Update Successful");
    }







}

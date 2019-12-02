package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.CardManager;
import edu.cmu.andrew.karim.server.models.Card;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/cards")
public class CardHttpInterface extends HttpInterface {
    private ObjectWriter ow;
    private MongoCollection<Document> cardCollection = null;
    public CardHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postCards(@Context HttpHeaders headers,Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Card card = new Card(
                    json.getString("userId"),
                    json.getString("cardNumber"),
                    json.getString("cvv"),
                    json.getString("expireDate")

            );
            CardManager.getInstance().createCard(headers,card);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST cards", e);
        }

    }


    @GET
    @Path("/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCards(@Context HttpHeaders headers, @PathParam("userId") String userId){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Card> cards = null;

            if(userId != null)

                cards = CardManager.getInstance().getCardById(headers,userId);

            if(cards != null)
                return new AppResponse(cards);
            else
                throw new HttpBadRequestException(0, "Problem with getting cards");
        }catch (Exception e){
            throw handleException("GET /cards", e);
        }
    }

    @PATCH
    @Path("/{userId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchCard(@Context HttpHeaders headers,Object request, @PathParam("userId") String userId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Card card = new Card(
                    userId,
                    json.getString("cardNumber"),
                    json.getString("cvv"),
                    json.getString("expireDate")
            );

            CardManager.getInstance().updateCard(headers,card);

        }catch (Exception e){
            throw handleException("PATCH cards/{cardId}", e);
        }

        return new AppResponse("Update Successful");
    }




    @DELETE
    @Path("/{cardNumber}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteCard(@PathParam("cardNumber") String cardNumber){ // cardNO ; userId

        try{
            CardManager.getInstance().deleteCard(cardNumber);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE cards/{cardNumber}", e);
        }

    }


}


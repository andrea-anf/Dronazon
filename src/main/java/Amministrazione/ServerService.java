package Amministrazione;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("Admin")
public class ServerService {

//    #################### SMARTCITY #################################################

    @Path("getSmartCity")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getSmartCity(){
//        return Response.ok(Dictionary.getInstance()).build();
        return Response.ok().build();
    }

//  #################### DRONE #################################################

    @Path("addDrone")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addDrone(/*Word word*/){
        return Response.ok().build();
    }

    @Path("deleteDrone/{drone}")
    @DELETE
    @Produces({"application/json", "application/xml"})
    public Response deleteWord(@PathParam("drone")String word){
/*        Word w = Dictionary.getInstance().getByWord(word);
        if(w != null){
            Dictionary.getInstance().deleteWord(w);
            return Response.ok(word + " deleted").build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }*/
    }

//    #################### STATISTICS #################################################

    @Path("addStatistics")
    @DELETE
    @Consumes({"application/json", "application/xml"})
    public Response removeDrone(/*Word word*/){
        return Response.ok().build();
    }



}
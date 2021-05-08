package Amministrazione;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("drones")
public class DroneAndStatisticsService {

//  #################### DRONE #################################################

    @Path("addDrone")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addDrone(String ID, String IP, String port){
        return Response.ok("nuovo drone in arrivo").build();
    }

    @Path("deleteDrone/{drone}")
    @DELETE
    @Produces({"application/json", "application/xml"})
    public Response deleteWord(@PathParam("drone")String drone){
/*        Word w = Dictionary.getInstance().getByWord(word);
        if(w != null){
            Dictionary.getInstance().deleteWord(w);
            return Response.ok(word + " deleted").build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }*/
        return Response.ok(String.format("elimino il drone %s", drone)).build();
    }

//  #################### STATISTICS #################################################

    @Path("addStats")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addStatistics(){
        return Response.ok("statistiche aggiunte").build();
    }


}
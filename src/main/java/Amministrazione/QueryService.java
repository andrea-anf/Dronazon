package Amministrazione;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("admin")
public class QueryService {

//  #################### SMARTCITY #################################################

//  L’elenco dei droni presenti nella rete
    @Path("getSmartCity")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getSmartCity(){
        return Response.ok("lista dei droni nella smartcity").build();
    }

//  Ultime n statistiche globali (con timestamp) relative alla smart-city
    @Path("getAvgDeliveries")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response getAvgDeliveries() {
        return Response.ok(String.format("media consegne tra ")).build();
    }

//  Media del numero di consegne effettuate dai droni della smart-city tra due timestamp t1 e t2
    @Path("getStats/{lastN}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getStatistics(@PathParam("lastN")int lastN){
        return Response.ok(String.format("ritornate le ultime %d statistiche globali", lastN)).build();
    }
}
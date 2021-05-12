package Amministrazione;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("admin")
public class ServiceAdmin {

//  #################### SMARTCITY #################################################

//  http://localhost:1338/admin/getSmartCity
//  L’elenco dei droni presenti nella rete
    @Path("getSmartCity")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getSmartCity(){
        return Response.ok(SmartCity.getInstance().getSmartCity()).build();
    }

//  Ultime n statistiche globali (con timestamp) relative alla smart-city
    @Path("getAvgDeliveries/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAvgDeliveries(@PathParam("t1")int t1, @PathParam("t2")int t2) {
        return Response.ok(String.format("media consegne tra ")).build();
    }

//  Media del numero di consegne effettuate dai droni della smart-city tra due timestamp t1 e t2
    @Path("getStats/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getStatistics(@PathParam("t1")int t1, @PathParam("t2")int t2){
        return Response.ok(String.format("ritornate le statistiche globali tra %d e %d", t1,t2)).build();
    }
}
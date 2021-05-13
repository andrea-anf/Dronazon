package Amministrazione.Services;

import Amministrazione.Drones.SmartCity;
import Amministrazione.Statistics.StatLists;

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
    @Path("getLastNStats/{lastN}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getLastNStats(@PathParam("lastN")int lastN) {
        if(lastN != 0){
            return Response.ok(StatLists.getInstance().getLastNStats(lastN)).build();
        }
        else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    // media delle consegne effettuate dai droni tra due timestamp
    @Path("getAvgDeliveries/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAvgDeliveries(@PathParam("t1")int t1, @PathParam("t2")int t2) {
        return Response.ok(String.format("media consegne tra %d e %d",t1,t2)).build();
    }

    //  Media del numero di consegne effettuate dai droni della smart-city tra due timestamp t1 e t2
    @Path("getAvgKilometers/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAvgKilometers(@PathParam("t1")int t1, @PathParam("t2")int t2){
        return Response.ok(String.format("ritorna i chilometri percorsi tra %d e %d", t1,t2)).build();
    }
}
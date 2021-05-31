package Amministrazione.Services;

import SmartCity.SmartCity;
import Amministrazione.Statistics.StatLists;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

@Path("admin")
public class ServiceAdmin {

//  #################### SMARTCITY #################################################

    //  L’elenco dei droni presenti nella rete
    //  http://localhost:1338/admin/getSmartCity
    @Path("getSmartCity")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getSmartCity(){
        return Response.ok(SmartCity.getInstance().getSmartCity()).build();
    }

    //  Ultime n statistiche globali (con timestamp) relative alla smart-city
    //  http://localhost:1338/admin/getLastNStats/{lastN}   ---> lastN integer
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
    //  Media del numero di consegne effettuate dai droni della smart-city tra due timestamp t1 e t2
    //  http://localhost:1338/admin/getAvgDeliveries/{t1}/{t2} ---> t1,t2 strings of type yyyy-MM-ddTHH:mm:ss
    @Path("getAvgDeliveries/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAvgDeliveries(@PathParam("t1") String t1, @PathParam("t2")String t2) {
        Timestamp tMin = Timestamp.valueOf(t1.replace("T", " "));
        Timestamp tMax = Timestamp.valueOf(t2.replace("T", " "));
        return Response.ok(StatLists.getInstance().getAvgDeliveries(tMin,tMax)).build();
    }

    //  Media dei chilometri percorsi dai droni della smart-city tra due timestamp t1 e t2
    //  http://localhost:1338/admin/getAvgKilometers/{t1}/{t2} ---> t1,t2 strings of type yyyy-MM-ddTHH:mm:ss
    @Path("getAvgKilometers/{t1}/{t2}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getAvgKilometers(@PathParam("t1")String t1, @PathParam("t2")String t2){
        Timestamp tMin = Timestamp.valueOf(t1.replace("T", " "));
        Timestamp tMax = Timestamp.valueOf(t2.replace("T", " "));
        return Response.ok(StatLists.getInstance().getAvgKilometers(tMin,tMax)).build();    }
}
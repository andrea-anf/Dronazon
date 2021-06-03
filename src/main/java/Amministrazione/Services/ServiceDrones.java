package Amministrazione.Services;

import Amministrazione.Coordinates;
import SmartCity.Drone;
import SmartCity.SmartCity;
import Amministrazione.Statistics.Stat;
import Amministrazione.Statistics.StatLists;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.ThreadLocalRandom;

@Path("drones")
public class ServiceDrones {

//  #################### DRONE #################################################

    /*    http://localhost:1338/drones/addDrone
    {
        "id":"121", --> da far generare al drone
        "port":"1338",
        "server":"localhost"
    }*/
    @Path("addDrone")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addDrone(Drone drone){
        Drone d = SmartCity.getInstance().getById(drone.getId());
        if(d == null) {

            if (SmartCity.getInstance().addDrone(drone)) {

                Coordinates coords = new Coordinates(ThreadLocalRandom.current().nextInt(0, 9 + 1),ThreadLocalRandom.current().nextInt(0, 9 + 1));
                drone.setCoords(coords);
                return Response.ok(SmartCity.getInstance()).build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

//  http://localhost:1338/drones/deleteDrone/{id}
    @Path("deleteDrone/{drone}")
    @DELETE
    public Response deleteDrone(@PathParam("drone")int droneId){
        Drone d = SmartCity.getInstance().getById(droneId);
        if(d != null){
            SmartCity.getInstance().deleteDrone(d);
            return Response.status(Response.Status.OK).build();
        }
        else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

//  #################### STATISTICS #################################################

    @Path("addStats")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response addStatistics(Stat stat){
        StatLists.getInstance().addStat(stat);
        return Response.ok("successfully added").build();
    }


}
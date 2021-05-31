package SmartCity;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DroneStarter {
    public static void main(String[] args) {
        System.out.println("\nStarting new drone:");
        Drone drone = new Drone();
        drone.setLocalAddress("localhost");
        drone.setLocalPort("1338");
        drone.setId(ThreadLocalRandom.current().nextInt(0, 999 + 1));

        //check if it's the first entry
        ClientResponse responseGetSmartCity = drone.getSmartCity();
        System.out.println(responseGetSmartCity.toString());
        SmartCity dronelist = responseGetSmartCity.getEntity(SmartCity.class);

        if(dronelist.getDronelist().size() == 0){
            drone.setMaster(true);
        }

        System.out.println("\nStarting drone:" +
                "\n\tID: " + drone.getId() +
                "\tMaster: " + drone.isMaster());

        //connect the new drone to the ServerAmministratore
        ClientResponse response = drone.connect();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        //returns list with active drones
        SmartCity updatedDronelist = response.getEntity(SmartCity.class);
        System.out.println("\nSmartCity:");
        for (Drone d : updatedDronelist.getDronelist()){
            System.out.println(
                    "\tID: " + d.getId() +
                            "\t | Coords: (" + d.getCoords().getX()+","+ d.getCoords().getY() + ")" +
                            "\tAddress: " + d.getLocalAddress()+
                            "\tPort: " + d.getLocalPort());
        }








    }
}

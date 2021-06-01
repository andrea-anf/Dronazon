package SmartCity;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import dronazon.DronazonSender;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DroneStarter {
    public static void main(String[] args) {
        System.out.println("\nStarting new drone:");
        Drone drone = new Drone();
        drone.setLocalAddress("localhost");
        drone.setLocalPort("1338");
        drone.setId(ThreadLocalRandom.current().nextInt(0, 999 + 1));

        ClientResponse responseGetSmartCity = drone.getSmartCity();
        System.out.println(responseGetSmartCity.toString());
        SmartCity dronelist = responseGetSmartCity.getEntity(SmartCity.class);



        System.out.println("\nStarting drone:" +
                "\n\tID: " + drone.getId());

        //registers the new drone to the ServerAmministratore
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

        //if it's the first entry, make it master
        if(dronelist.getDronelist().size() == 0){
            drone.setMaster(true);
            Runnable sender = new MasterDrone(drone.getId());
            Thread thread = new Thread(sender);
            thread.start();
        }
        else{
            try {
                System.out.println("Hit return to stop...");
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





    }
}

package SmartCity;


import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DronesStarter {
    public static void main(String[] args) {
        System.out.println("\nStarting new drone:");
        Drone drone = new Drone();
        drone.setLocalAddress("localhost");
        drone.setLocalPort("1338");
        ClientResponse response;

        ClientResponse responseGetSmartCity = drone.getSmartCity();
        System.out.println("STATUS: " + responseGetSmartCity.getStatus());

        SmartCity dronelist = responseGetSmartCity.getEntity(SmartCity.class);

//        if(dronelist.size() == 0){
//            System.out.println("CREAZIONE DRONE MASTER");
//            drone.setMaster(true);
//            drone.setId(ThreadLocalRandom.current().nextInt(0, 100 + 1));
//        }
//        else{
//            System.out.println("CREAZIONE DRONE NORMALE");
//            do{
//                drone.setId(ThreadLocalRandom.current().nextInt(0, 100 + 1));
//            }
//            while(!dronelist.contains(drone.getId()));
//        }


//        response = drone.connect();
//        if (response.getStatus() != 200 && response.getStatus() != 406) {
//            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
//        }
//
//        System.out.println(
//                "\tID: " + drone.getId() +
//                        "\tMaster: " + drone.isMaster());

//        SmartCity dronelist = response.getEntity(SmartCity.class);

//        System.out.println("\nSmartCity:");
//        for (Drone d : dronelist.getSmartCity()){
//            System.out.println(
//                    "\tID: " + d.getId() +
//                    "\tCoords: (" + d.getCoords().getX()+", "+ d.getCoords().getY() + ")" +
//                    "\tMaster: " + d.isMaster());
//        }








    }
}

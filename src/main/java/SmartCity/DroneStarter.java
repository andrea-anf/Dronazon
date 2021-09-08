package SmartCity;

import SmartCity.MasterDrone.DispatchingService;
import SmartCity.MasterDrone.MasterDrone;
import SmartCity.RPCServices.DroneRPCListeningService;
import SmartCity.RPCServices.DroneRPCSendingService;
import com.sun.jersey.api.client.ClientResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;



public class DroneStarter {
    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("\nStarting new drone:");
        Drone drone = new Drone();
        drone.setLocalAddress("localhost");
        drone.setLocalPort(""+ThreadLocalRandom.current().nextInt(1000, 1999 + 1));
        drone.setId(ThreadLocalRandom.current().nextInt(1, 999 + 1));

        ClientResponse responseGetSmartCity = drone.getSmartCity();
        System.out.println(responseGetSmartCity.toString());
        SmartCity dronelist = responseGetSmartCity.getEntity(SmartCity.class);
        signUpDrone(drone);


        //if it's the first entry, make it master
        if(dronelist.getDronelist().size() != 0){
            Server listeningService = ServerBuilder.forPort(Integer.parseInt(drone.getLocalPort())).addService(new DroneRPCListeningService(drone)).build();
            listeningService.start();

            DroneRPCSendingService.addDroneRequest(drone, dronelist);

            System.out.println("\n...Press enter to stop...");
            input.nextLine();
            quitDrone(drone);

        }
        else{
            drone.setMaster(true);
            Runnable sender = new MasterDrone(drone);
            Thread thread = new Thread(sender);
            thread.start();
        }


    }

    public static void quitDrone(Drone drone){

        //wait delivery finish to quit
        synchronized (drone.getDeliveryLock()){
            while(drone.isDelivering()){
                try {
                    drone.getDeliveryLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ClientResponse deleteResponse = drone.disconnect();
            System.out.println("\nRESPONSE: " + deleteResponse);

            System.exit(0);
        }

    }

    public static void signUpDrone(Drone drone){
        System.out.println("\nStarting drone:" +
                "\n\tID: " + drone.getId());

        //registers the new drone to the ServerAmministratore
        ClientResponse response = drone.connect();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        //returns list with active drones and sets coords
        SmartCity updatedDronelist = response.getEntity(SmartCity.class);
        System.out.println("\nSmartCity:");
        for (Drone d : updatedDronelist.getDronelist()){
            System.out.println(
                    "\tID: " + d.getId() +
                            "\t | Coords: (" + d.getCoords().getX()+","+ d.getCoords().getY() + ")" +
                            "\tAddress: " + d.getLocalAddress()+
                            "\tPort: " + d.getLocalPort());
            if(d.getId() == drone.getId()){
                drone.setCoords(d.getCoords());
            }
        }

    }
}

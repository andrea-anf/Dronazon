package SmartCity;

import SmartCity.MasterDrone.MasterDrone;
import SmartCity.MasterDrone.StatsSender;
import SmartCity.RPCServices.DroneRPCListeningService;
import SmartCity.RPCServices.DroneRPCSendingService;
import SmartCity.SensoreInquinamento.PM10Simulator;
import com.sun.jersey.api.client.ClientResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;



public class DroneStarter {
    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        Drone drone = new Drone();
        drone.setLocalAddress("localhost");
        drone.setLocalPort(""+ThreadLocalRandom.current().nextInt(1000, 1999 + 1));
        drone.setId(ThreadLocalRandom.current().nextInt(1, 999 + 1));

        System.out.println("\n[DRONE] Starting drone:" + "\n\tID: " + drone.getId());


        ClientResponse responseGetSmartCity = drone.getSmartCity();
        System.out.println("[SERVER] " + responseGetSmartCity.toString());
        SmartCity dronelist = responseGetSmartCity.getEntity(SmartCity.class);
        signUpDrone(drone);


        //if it's the first entry, make it master
        if(dronelist.getDronelist().size() != 0){
            Server listeningService = ServerBuilder.forPort(Integer.parseInt(drone.getLocalPort())).addService(new DroneRPCListeningService(drone)).build();
            listeningService.start();

            DroneRPCSendingService.addDroneRequest(drone, dronelist);

            Thread simulator = new PM10Simulator(drone.getBuff());
            simulator.start();

            Runnable sender = new StatsSender(drone);
            Thread thread = new Thread(sender);
            thread.start();

            System.out.println("\n...Press enter to stop...");
            input.nextLine();
            try {
                drone.quitDrone();
            } catch (MqttException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        else{
            drone.setNextDroneID(drone.getId());
            drone.setMaster(true);
            Thread simulator = new PM10Simulator(drone.getBuff());
            simulator.start();
            Runnable sender = new MasterDrone(drone);
            Thread thread = new Thread(sender);
            thread.start();
        }


    }



    public static void signUpDrone(Drone drone){

        //registers the new drone to the ServerAmministratore
        ClientResponse response = drone.connect();
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        //returns list with active drones and sets coords
        SmartCity updatedDronelist = response.getEntity(SmartCity.class);
        System.out.println("\n[RING] SmartCity:");
        for (Drone d : updatedDronelist.getDronelist()){
            System.out.println(
                    "\tID: " + d.getId() +
                            "\t| Coords: (" + d.getCoords().getX()+","+ d.getCoords().getY() + ")" +
                            "\tAddress: " + d.getLocalAddress()+
                            "\tPort: " + d.getLocalPort());
            if(d.getId() == drone.getNextDroneID()){
                System.out.print("\t[NextDrone]\n");
            }
            else if(d.getId() == drone.getNextNextDroneID()){
                System.out.print("\t[NextNextDrone]\n");

            }

            if(d.getId() == drone.getId()){
                drone.setCoords(d.getCoords());
            }
        }

    }
}

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;



public class DroneStarter {
    public static void main(String[] args) throws IOException, MqttException, InterruptedException {

        Scanner input = new Scanner(System.in);
        Drone drone = new Drone();
        drone.setLocalAddress("localhost");
        drone.setLocalPort(""+ThreadLocalRandom.current().nextInt(1000, 1999 + 1));
        drone.setId(ThreadLocalRandom.current().nextInt(1, 999 + 1));

        System.out.println("\n[DRONE] Starting drone:" + "\n\tID: " + drone.getId());


        ClientResponse responseGetSmartCity = drone.getSmartCity();
        System.out.println("[SERVER] " + responseGetSmartCity.toString());
        SmartCity smartcity = responseGetSmartCity.getEntity(SmartCity.class);
        signUpDrone(drone);


        //Drone starts to listen to other drones
        Server server = ServerBuilder
                .forPort(Integer.parseInt(drone.getLocalPort()))
                .addService(new DroneRPCListeningService(drone))
                .build();
        server.start();



        //if smartcity is not empty, make it a normal drone, otherwise Master
        if(smartcity.getDronelist().size() > 0){
            System.out.println("[INFO] Drone started to listen to other drones");

            DroneRPCSendingService.addDroneRequest(drone);
            Thread simulator = new PM10Simulator(drone.getBuff());
            simulator.start();

            Runnable checkDrone = new CheckDrone(drone);
            Thread threadCheckStatus = new Thread(checkDrone);
            threadCheckStatus.start();

            Runnable statSender = new StatsSender(drone);
            Thread threadStatSender = new Thread(statSender);
            threadStatSender.start();

            System.out.println("\n...Enter 'quit' to stop | re for recharge...");
            while (true) {
                String command = input.next(); // read any token from the input as String
                System.out.println("Command " + command); // optional message to indicate exit
                switch (command) {
                    case "re":
                        drone.rechargeDrone();
                        break;
                    case "quit":
                        System.out.println("Bye!"); // optional message to indicate exit
                        drone.quitDrone();
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }



        }
        else{
            System.out.println("[INFO] Master started to listen to other drones");
            drone.setNextDrone(drone);


            Thread simulator = new PM10Simulator(drone.getBuff());
            simulator.start();

            Runnable statSender = new StatsSender(drone);
            Thread threadStatSender = new Thread(statSender);
            threadStatSender.start();

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
        for (Drone d : updatedDronelist.getDronelist()){
            drone.addToDronelist(d);
            if(d.getId() == drone.getId()){
                drone.setCoords(d.getCoords());
            }
        }
        drone.showDroneList();
    }


}

package SmartCity.MasterDrone;

import SmartCity.Drone;
import SmartCity.RPCServices.DroneRPCListeningService;
import SmartCity.RPCServices.DroneRPCSendingService;
import SmartCity.SmartCity;
import com.sun.jersey.api.client.ClientResponse;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MasterDrone implements Runnable{
    final private String broker = "tcp://localhost:1883" ; // default MQTT broker address
    final private String clientId = MqttClient.generateClientId();
    final private String topic = "dronazon/smartcity/orders/";

    private Drone drone;
    int qos = 2;
    private Drone nextDrone;


    public MasterDrone(Drone drone){
        this.drone = drone;
        drone.addToDronelist(drone);
    }

    public void run(){

        Scanner input = new Scanner(System.in);
        DispatchingService disService = new DispatchingService(drone.getDronelist());



        try {

            //######## STARTS TO LISTEN TO OTHER DRONES ########
            Server server = ServerBuilder
                    .forPort(Integer.parseInt(drone.getLocalPort()))
                    .addService(new DroneRPCListeningService(drone))
                    .build();
            server.start();
            System.out.println("[+] Master started to listen!");


            // CONNECTING AS MQTT CLIENT
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("\n[+] Connecting to broker " + client.getServerURI() + "...");
            // Connect the client to the broker (blocking)
            client.connect(connOpts);
            System.out.println("[+] Successfully connected!");


            client.setCallback(new  MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());

                    System.out.println(
                            "\nMaster drone " + drone.getId() +
                                    " received a new message!" +
                                    "\n\tTime:    " + time +
                                    "\n\tTopic:   " + topic +
                                    "\n\tMessage: " + receivedMessage +
                                    "\n\tQoS:     " + message.getQos());


                    System.out.println("\nDRONELIST: ");
                    for(Drone d : drone.getDronelist()){
                        if(d.getId() == drone.getId()){
                            System.out.print("\t# " + d.getId() + "[MASTER]\n");
                        }
                        else{
                            System.out.print("\t# " + d.getId() + "\n");
                        }
                    }

                    // 1. check if nextdrone is delivering
                    // 2. find closest
                    // 3. find the one with greater ID number
                    nextDrone = disService.findClosest(receivedMessage);
                    if(nextDrone.isDelivering()){
                        drone.addToOrderlist(receivedMessage);
                        System.out.println("Nobody is ready to delivery, list of remaining order: ");
                        int count = 0;
                        for(String order : drone.getOrderlist()) {
                            count++;
                            System.out.println("\t#" + count + ": " + order);
                        }
                    }
                    else{
                        System.out.println("\nSendind order to: ");
                        if(nextDrone.getId() == drone.getId()){
                            System.out.print("\tDrone " + nextDrone.getId() + "[MASTER]\n");
                        }
                        else{
                            System.out.print("\tDrone " + nextDrone.getId() + "\n");
                        }
                        Thread oS = new OrderSender(nextDrone, receivedMessage);
                        oS.start();
                    }



                    System.out.println("\n ***  Press a key to exit *** \n");
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(
                            "Master drone " +
                                    drone.getId() +
                                    " has lost the connection! Cause:"
                                    + cause.getMessage() +
                                    "-  Thread PID: "
                                    + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            //subscribing to broker
            client.subscribe(topic,qos);
            System.out.println(
                    "\nMaster Drone " +
                            drone.getId() +
                            " is now subscribed to topic : " +
                            topic);

            //waiting to break connection with broker
            input.hasNextLine();
            client.disconnect();
            if(client.isConnected()){
                //if still connected force disconnection
                client.disconnectForcibly();
                System.out.println(
                        "\nMaster Drone " +
                                drone.getId() +
                                " is forcing disconnection from broker" +
                                client.getServerURI());
            }

            ClientResponse deleteResponse = drone.disconnect();
            System.out.println("RESPONSE: " + deleteResponse);

            System.out.println(
                    "Master Drone " +
                            drone.getId() +
                            " disconnected from broker " +
                            client.getServerURI());

        } catch (MqttException | IOException me) {
            System.out.println(me.getStackTrace());
        }
    }
}


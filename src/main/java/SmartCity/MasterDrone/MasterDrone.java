package SmartCity.MasterDrone;

import SmartCity.Drone;
import SmartCity.RPCServices.DroneRPCListeningService;
import SmartCity.RPCServices.DroneRPCSendingService;
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
        DispatchingService delivery = new DispatchingService(drone.getDronelist());

        try {
            // Create an Mqtt client
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("\n[+] Connecting to broker " + client.getServerURI() + "...");

            // Connect the client to the broker (blocking)
            client.connect(connOpts);
            System.out.println("[+] Successfully connected!");

            //######## STARTS TO LISTEN TO OTHER DRONES ########
            Server server = ServerBuilder
                    .forPort(Integer.parseInt(drone.getLocalPort()))
                    .addService(new DroneRPCListeningService(drone))
                    .build();
            server.start();
            System.out.println("[+] Master started to listen!");

            client.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());

                    System.out.println(
                            "Master drone " + drone.getId() +
                                    " received a new message!" +
                                    "\n\tTime:    " + time +
                                    "\n\tTopic:   " + topic +
                                    "\n\tMessage: " + receivedMessage +
                                    "\n\tQoS:     " + message.getQos());

                    System.out.println("DRONELIST: ");

                    for(Drone d : drone.getDronelist()){
                        System.out.println(d.getId()+", ");
                    }

                    nextDrone = delivery.findClosest();
                    if(nextDrone.getId() == drone.getId()){
                        System.out.println("YO IT'S ME RECEIVING THE MESSAGE!");
                    }
                    else{
                        DroneRPCSendingService.sendOrder(nextDrone, receivedMessage);
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

            client.subscribe(topic,qos);
            System.out.println(
                    "\nMaster Drone " +
                            drone.getId() +
                            " is now subscribed to topic : " +
                            topic);


            input.hasNextLine();
            client.disconnect();
            if(client.isConnected()){
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

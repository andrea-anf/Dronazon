package SmartCity.MasterDrone;

import SmartCity.CheckDrone;
import SmartCity.Drone;
import SmartCity.MasterDrone.Orders.DispatchingService;

import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.Scanner;

public class MasterDrone implements Runnable{
    final private String broker = "tcp://localhost:1883" ; // default MQTT broker address
    final private String clientId = MqttClient.generateClientId();
    final private String topic = "dronazon/smartcity/orders/";

    private Drone drone;
    int qos = 2;

    public MasterDrone(Drone drone){
        this.drone = drone;
    }

    public void run(){

        Scanner input = new Scanner(System.in);

        try {
            drone.setMaster(true);
            drone.setMasterDrone(drone);

            Runnable checkDrone = new CheckDrone(drone);
            Thread threadCheckStatus = new Thread(checkDrone);
            threadCheckStatus.start();


            // CONNECTING AS MQTT CLIENT
            drone.setClient(new MqttClient(broker, clientId));
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("[BROKER] Connecting to broker " + drone.getClient().getServerURI() + "...");
            // Connect the client to the broker (blocking)
            drone.getClient().connect(connOpts);
            System.out.println("[BROKER] Successfully connected!");


            drone.getClient().setCallback(new  MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    //spostare in DispatchingService per gestire coda
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());

                    System.out.println(
                            "\n\n[BROKER] New order" +
                                    "\n\tTime:    " + time +
                                    "\n\tTopic:   " + topic +
                                    "\n\tMessage: " + receivedMessage +
                                    "\n\tQoS:     " + message.getQos());

                    System.out.println("\n[RING] SmartList:");
                    for(Drone d : drone.getDronelist()){
                        if(d.getId() == drone.getId()){
                            if(d.isDelivering()){
                                System.out.print("\t# " + d.getId() + " [BUSY] [MASTER] \n");
                            }else{
                                System.out.print("\t# " + d.getId() + " [MASTER]\n");
                            }
                        }
                        else{
                            if(d.isDelivering()){
                                System.out.print("\t# " + d.getId() + " [BUSY]\n");
                            }else{
                                System.out.print("\t# " + d.getId() + "\n");
                            }
                        }
                    }



                    DispatchingService disService = new DispatchingService(drone.getDronelist());
                    if(drone.getOrderQueue().size() > 0){
                        drone.addOrderQueue(receivedMessage);
                        String o = drone.getOrderQueue().peek();
                        System.out.println("\n[QUEUE] Trying to send order " + o + " from queue");
                        disService.checkAndSendOrder(drone, drone.takeOneOrderQueue());
                    }
                    else{
                        disService.checkAndSendOrder(drone, receivedMessage);
                    }

                    System.out.println("\n***  Press enter to exit ***\n");
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(
                            "[BROKER] Master drone " +
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
            drone.getClient().subscribe(topic,qos);
            System.out.println(
                    "\n[BROKER] Master Drone " +
                            drone.getId() +
                            " is now subscribed to topic : " +
                            topic);

            System.out.println("\n...Enter 'quit' to stop | 're' for recharge...");
            //waiting to break connection with broker
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

        } catch (MqttException | InterruptedException me) {
            System.out.println(me.getStackTrace());
        }
    }
}


package SmartCity;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.xml.bind.v2.TODO;
import org.eclipse.paho.client.mqttv3.*;

import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.Scanner;

public class MasterDrone implements Runnable{
    private MqttClient client;
    private String broker = "tcp://localhost:1883" ; // default MQTT broker address
    private String clientId = MqttClient.generateClientId();

    private String topic = "dronazon/smartcity/orders/";
    private Drone drone;
    int qos = 2;


    public MasterDrone(Drone drone){
        this.drone = drone;
    }

    public void run(){

        Scanner input = new Scanner(System.in);

        try {
// Create an Mqtt client
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("\nConnecting to broker " + client.getServerURI() + "...");

// Connect the client to the broker (blocking)
            client.connect(connOpts);
            System.out.println("Successfully connected!");

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

                    System.out.println("\n ***  Press a key to exit *** \n");
                }

                //TODO: Creare messaggi RPC per distribuire ordini ai droni

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

        } catch (MqttException me) {
            System.out.println(me.getStackTrace());
        }
    }
}

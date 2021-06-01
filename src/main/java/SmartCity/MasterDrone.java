package SmartCity;

import com.sun.xml.bind.v2.TODO;
import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.Scanner;

public class MasterDrone implements Runnable{
    private MqttClient client;
    private String broker = "tcp://localhost:1883" ; // default MQTT broker address
    private String clientId = MqttClient.generateClientId();

    private String topic = "dronazon/smartcity/orders/";
    private int droneId;
    int qos = 2;


    public MasterDrone(int id){
        this.droneId = id;
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
                            "Master drone " + droneId +
                                    " received a new message!" +
                                    "\n\tTime:    " + time +
                                    "\n\tTopic:   " + topic +
                                    "\n\tMessage: " + receivedMessage +
                                    "\n\tQoS:     " + message.getQos() + "\n");

                    System.out.println("\n ***  Press a key to exit *** \n");
                }

                //TODO: Creare messaggi RPC per distribuire ordini ai droni

                public void connectionLost(Throwable cause) {
                    System.out.println(
                            "Master drone " +
                                    droneId +
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
                            this.droneId +
                            " is now subscribed to topic : " +
                            topic);


            input.hasNextLine();
            client.disconnect();
            System.out.println(
                    "\nMaster Drone " +
                            this.droneId +
                            "Disconnected from broker" +
                            client.getServerURI());




        } catch (MqttException me) {
// handle exceptions
        }
    }
}
